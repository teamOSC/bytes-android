package com.greplr.bytes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconDetectionService extends Service implements BeaconConsumer {

    protected static final String TAG = "BeaconService";
    private BeaconManager beaconManager;
    private boolean temp = false;

    public BeaconDetectionService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "" + beacons + beacons.iterator().next().getDistance());
                    if (beacons.iterator().next().getDistance() < 0.75) {
                        Log.d(TAG, "Going to start notification");
                        Intent intent = new Intent(getApplicationContext(), FoodCourtActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(temp == false){
                            startActivity(intent);
                            temp = true;
                        }
//                        Intent openActIntent = new Intent(getApplicationContext(), FoodCourtActivity.class);
//                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, openActIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        Notification notification = new Notification.Builder(getApplicationContext())
//                                .setTicker("Food Court")
//                                .setContentTitle("Food Court")
//                                .setContentText("Tap to order food")
//                                .setContentIntent(pi)
//                                .setAutoCancel(true)
//                                .build();
//
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        notificationManager.notify(69, notification);
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", Identifier.parse("b9407f30-f5f8-466e-aff9-25556b57fe6d"),
                    Identifier.parse("43"), Identifier.parse("22317")));
        } catch (RemoteException e) {
        }
    }
}

