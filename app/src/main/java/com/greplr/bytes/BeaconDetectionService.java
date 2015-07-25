package com.greplr.bytes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BeaconDetectionService extends Service {

    private BeaconManager beaconManager;
    private com.estimote.sdk.Region region;

    public BeaconDetectionService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        region = new Region("regionId", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 43, 22317);
        beaconManager = new BeaconManager(this);

        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                Log.d("Main", "Entered region");

            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d("Main", "Exited region");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(region);
                } catch (RemoteException e) {
                    Log.d("MainActivity", "Error while starting monitoring");
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
