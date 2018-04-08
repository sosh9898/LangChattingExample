package jy.sopt.chattingsample.setting;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import jy.sopt.chattingsample.MainActivity;
import jy.sopt.chattingsample.R;
import jy.sopt.chattingsample.SharedPreferencesService;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    int badgeCount=0;
    int roomCount=0;

    public static final String SERVER_KEY = "AAAAfmPK2OQ:APA91bFn9ay9MSPUptniUxnV9Gv_alSU1HLzI7T0uUxye8EiRl21g3_sH-Prtuw5d2t-OSxpcDB58sDaLKFLNNt5LiIQMNnBFjv-2SApC9UfgqeTGnBtjcQ7cWmQTVu1hjMA_O3zWTx7";

    private String msg, title, name;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferencesService.getInstance().load(this);
        if(SharedPreferencesService.getInstance().getPrefIntegerData("badgeCount") != 0) {
            badgeCount = SharedPreferencesService.getInstance().getPrefIntegerData("badgeCount");
        }
        else
            SharedPreferencesService.getInstance().setPrefData("badgeCount", badgeCount);

        badgeCount++;

        SharedPreferencesService.getInstance().setPrefData("badgeCount", badgeCount);


        if (remoteMessage.getData().size() > 0) {
        }

        if (remoteMessage.getNotification() != null) {
        }

        title = remoteMessage.getData().get("title");
        msg = remoteMessage.getData().get("body");
        name = remoteMessage.getData().get("name");

        if(SharedPreferencesService.getInstance().getPrefIntegerData(name) != 0) {
            roomCount = SharedPreferencesService.getInstance().getPrefIntegerData(name);
        }
        else
            SharedPreferencesService.getInstance().setPrefData(name, roomCount);

        roomCount++;

        SharedPreferencesService.getInstance().setPrefData(name, roomCount);





            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());

        mBuilder.setContentIntent(contentIntent);

        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", badgeCount);
        badgeIntent.putExtra("badge_count_package_name", getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(getApplicationContext()));
        sendBroadcast(badgeIntent);
    }

    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

}
