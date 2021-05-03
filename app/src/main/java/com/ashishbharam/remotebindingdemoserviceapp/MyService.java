package com.ashishbharam.remotebindingdemoserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {

    private int mRandomNumber;
    private boolean isRandomGeneratorOn;
    public static final int GET_RANDOM_NUMBER_FLAG = 0;

    private class RandomNumberRequestHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GET_RANDOM_NUMBER_FLAG) {
                Message messageSendRandomNumber = Message.obtain(null, GET_RANDOM_NUMBER_FLAG);
                messageSendRandomNumber.arg1 = getRandomNumber();
                try {
                    msg.replyTo.send(messageSendRandomNumber);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    }

    private Messenger mRandomNumberMessenger = new Messenger(new RandomNumberRequestHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRandomNumberMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("TAG", "In onUnbind(): Executes only once, you can not stop service if don't Unbind it.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "Service Destroyed");
        stopRandomNumberGenerator();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "In onStartCommand, MyService thread ID: " + Thread.currentThread().getId());
        isRandomGeneratorOn = true;

        new Thread(() -> {
            startRandomNumberGenerator();
        }).start();
        return START_STICKY;
    }

    private void startRandomNumberGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (isRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(999 - 99) ;
                    Log.i("TAG", "startRandomNumberGenerator Thread id: " + Thread.currentThread().getId()
                            + " Random num:" + mRandomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("TAG", "Thread Interrupted :" + e.getLocalizedMessage());

            }
        }
    }

    public void stopRandomNumberGenerator() {
        isRandomGeneratorOn = false;
    }

    private int getRandomNumber() {
        return mRandomNumber;
    }
}
