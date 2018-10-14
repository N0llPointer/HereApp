package com.nollpointer.hereapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.here.android.mpa.common.GeoCoordinate;
import com.nollpointer.hereapp.activities.OrderDetailsActivity;

public class MessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(!remoteMessage.getData().isEmpty()){
            saveToFile();
            sendNotification("Address",remoteMessage.getData().get("Coords"));
        }
    }

    private void saveToFile(){
        SharedPreferences.Editor editor = this.getSharedPreferences("SETTINGS",Context.MODE_PRIVATE).edit();
        editor.putBoolean("ShowTestRouteInfo",true);
        editor.apply();
    }

    private void sendNotification(String address,String coords){
        String[] c = coords.split(" ");
        double first = Double.valueOf(c[0]);
        double second = Double.valueOf(c[1]);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Address",address);
        intent.putExtra("Coords",coords);
        intent.putExtra("HORK","PORK");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                0);
        String channelId = "channel";
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Новый заказ")
                .setContentText("Доступен новый заказ")
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);


        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title01",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(defaultSound, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            channel.enableVibration(true);
            channel.enableLights(true);
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
            manager.createNotificationChannel(channel);
            Log.wtf("ASD",channel.toString());
        }

        Notification notification = builder.build();
        Log.wtf("ASD",notification.toString());
        notification.sound = defaultSound;

        notification.audioStreamType = AudioManager.STREAM_ALARM;


        //notification.defaults = Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE

        manager.notify(0 /* ID of notification */, notification);


    }
}
