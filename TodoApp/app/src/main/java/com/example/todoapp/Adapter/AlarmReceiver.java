package com.example.todoapp.Adapter;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todoapp.MainActivity;
import com.example.todoapp.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri soundUri = Uri.parse("android.resource://todoapp/raw/ringtone");
        Intent nextActivity = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextActivity,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "todo")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Cánh bảo!")
                .setContentText("Có công việc sắp quá hạn")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
