package com.markm.criticalwakeup;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SoundService extends Service {

    private MediaPlayer player;
    private String Tag = "\nRandom Letter Service,";
    private int critNum;

    class SoundServiceBinder extends Binder {
        public SoundService getService(){
            return SoundService.this;
        }
    }

    private IBinder binder = new SoundServiceBinder();

    public int onStartCommand(Intent intent, int flags, int startId) {
        critNum = intent.getExtras().getInt("crit");
        if(critNum == 1){
            player = MediaPlayer.create(this, R.raw.gymnopedie);
        }
        else if(critNum == 2){
            player = MediaPlayer.create(this, R.raw.rooster);
        }
        else if(critNum == 3){
            player = MediaPlayer.create(this, R.raw.tornado);
        }

        player.setLooping(true);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startService();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }

    public void startService() throws InterruptedException {
        Log.i(Tag, "Start Service!");
        player.start();
        Log.i(Tag, "Started!");


    }

    public void stopService(){
        Log.i(Tag, "Stop Service!");
        player.release();
        player = null;
        Log.i(Tag, "Stopped");
    }

    public void onDestroy(){
        super.onDestroy();
        stopService();
        stopSelf();

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Tag, "In onBind");
        return binder;
    }
}
