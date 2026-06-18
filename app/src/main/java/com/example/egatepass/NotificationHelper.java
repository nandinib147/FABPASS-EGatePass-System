package com.example.egatepass;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    Context context;

    public NotificationHelper(Context context) {

        this.context = context;

    }

    public void showNotification(

            String title,
            String message

    ) {

        NotificationManager manager =
                (NotificationManager)

                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        String channelId = "gatepass_channel";

        // ANDROID 8+

        if(Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(

                            channelId,

                            "GatePass Notifications",

                            NotificationManager.IMPORTANCE_HIGH

                    );

            manager.createNotificationChannel(
                    channel
            );

        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        channelId
                )

                        .setSmallIcon(
                                android.R.drawable.ic_dialog_info
                        )

                        .setContentTitle(title)

                        .setContentText(message)

                        .setAutoCancel(true);

        manager.notify(
                1,
                builder.build()
        );

    }

}