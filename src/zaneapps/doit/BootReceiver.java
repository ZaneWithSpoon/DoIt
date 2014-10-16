package zaneapps.doit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.app.PendingIntent;

public class BootReceiver extends BroadcastReceiver {

	ArrayList<String> toDoList;

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			generateNotification(context, intent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void load(Context context) throws FileNotFoundException {
		FileInputStream fis;
		try {
			fis = context.openFileInput("toDoListFile");
			ObjectInputStream ois = new ObjectInputStream(fis);
			toDoList = (ArrayList<String>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateNotification(Context context, Intent intent)
			throws FileNotFoundException {
		load(context);

		Intent resultIntent = new Intent(context, MainActivity.class);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Random rand = new Random();

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("You know you should")
				.setContentText(toDoList.get(rand.nextInt(toDoList.size() - 1)))
				// .setContentText("something")
				.setContentIntent(resultPendingIntent);
		long[] pattern = { 500, 500, 500, 500, 500, 500 };
		mBuilder.setVibrate(pattern);
		mBuilder.setLights(13120255, 500, 500);
		// if(rand.nextInt(1)==0)
		// Uri alarmSound = RingtoneManager.(R.raw.quit_wasting_time);
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);

		mBuilder.setContentIntent(resultPendingIntent);

		int mNotificationId = 001;

		NotificationManager mNotifyMgr = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}

}