package h5adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import com.blowfire.app.framework.BFApplication;
import com.blowfire.app.framework.activity.BFActivity;
import com.blowfire.common.utils.BFLog;
import com.sportbrain.jewelpuzzle.R;
import com.sportbrain.jewelpuzzle.UnityPlayerActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import h5adapter.utils.Badges;
import h5adapter.utils.Dates;

public class LocalPush extends Worker {

    private static final int NOTIFICATION_ID = 20001;
    private static final String CHANEL_ID = "local_push_id";
    private static final String LOCAL_PUSH_KEY = "local_push_flag";

    public LocalPush(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull @Override public Result doWork() {
        try {

            int type = getInputData().getInt("Type", 1001);

            StringBuilder title = new StringBuilder();
            StringBuilder content = new StringBuilder();
            StringBuilder duration = new StringBuilder();
            Map<String, Object> event = new HashMap<>();
            switch (type) {
                case 1001:
                    title.append("Brain exercise time!");
                    content.append("Awaken your brain. Let's train your brain today!");
                    event.put("duration", "ten_minutes");
                    duration.append("ten_minutes");
                    break;
                case 1002:
                    title.append("Top enjoy for you!");
                    content.append("Most popular and addictive puzzle game!Once you start, you will never stop! ");
                    event.put("duration", "one_hour");
                    duration.append("one_hour");
                    break;
                case 1003:
                    title.append("It's relaxation time!");
                    content.append("Relax and wait for the Jewels to break!");
                    event.put("duration", "eight_hours");
                    duration.append("eight_hours");
                    break;
                case 1004:
                    event.put("duration", "one_day");
                    title.append("Challenge for Higher Score!");
                    content.append("Use your wisdom to get higher scores!");
                    duration.append("one_day");
                    break;
                case 1005:
                    event.put("duration", "two_days");
                    title.append("Prepare before going to bed!");
                    content.append("Relaxing your brain with Jewel Puzzle is more helpful for sleep!");
                    duration.append("two_days");
                    break;
                default:
                    if (type < 3000) {
                        enQueue(type + 1000);
                    }
                    writeContent(title, content, duration, event);
                    break;
            }

            CCNativeAPIProxy.logEvent("Show_Notification", false, true, event);

            Context context = BFApplication.getContext();
            Intent intent = new Intent(context, UnityPlayerActivity.class);
            intent.putExtra(LOCAL_PUSH_KEY, duration.toString());
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANEL_ID);

            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.notification_image_small)
                    .setTicker("This time belongs to Legend Stone!")
                    .setContentTitle(title.toString())
                    .setContentText(content.toString())
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_image))
                    .setContentInfo("Enjoy it!");


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANEL_ID,
                        "Local Push Chanel",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(NOTIFICATION_ID, b.build());
            Badges.setBadge(context, 1);

            BFLog.d("LocalPush", "work executed" + System.currentTimeMillis() / 1000);
            BFLog.d("LocalPush", "success");
            return Result.success();
        } catch (Exception e) {
            BFLog.d("LocalPush", "failed but retry");
            return Result.retry();
        }
    }

    private void writeContent(StringBuilder title, StringBuilder content, StringBuilder duration, Map<String, Object> event) {
        event.put("duration", getInputData().getString("Duration"));
        title.append(getInputData().getString("Title"));
        content.append(getInputData().getString("Des"));
        duration.append(event.get("duration"));
    }

    private void enQueue(int id) {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(LocalPush.class,
                Dates.TIME_DAY, TimeUnit.MILLISECONDS,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS)
                .addTag("SpecificLocalPush")
                .setInputData(new Data.Builder()
                        .putInt("Type", id)
                        .putString("Duration", getInputData().getString("Duration"))
                        .putString("Title", getInputData().getString("Title"))
                        .putString("Des", getInputData().getString("Des"))
                        .build())
                .build();

        WorkManager.getInstance().enqueue(request);
    }

    public static String getPushType(Intent intent) {
        if (intent == null) {
            return "";
        }

        String duration = intent.getStringExtra(LOCAL_PUSH_KEY);
        if (TextUtils.isEmpty(duration)) {
            return "";
        }

        return duration;
    }
}
