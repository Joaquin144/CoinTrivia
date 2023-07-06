package com.joaquin.quizapp.cointrivia.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.joaquin.quizapp.cointrivia.MainActivity;
import com.joaquin.quizapp.cointrivia.R;
import com.joaquin.quizapp.cointrivia.utils.Constants;

import kotlin.random.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager);

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification == null) return;
        String title = notification.getTitle();
        String body = notification.getBody();

        pushNotification(title, body);
    }

    private void pushNotification(String title, String body) {
        int notificationId = Random.Default.nextInt();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, Constants.APP_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon_two)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .build();//todo: set autoCancel  = true in production phase

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(Constants.APP_NOTIFICATION_CHANNEL_ID, Constants.APP_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("General App Notifications");
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        notificationManager.createNotificationChannel(channel);
    }
}
