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
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.app.PendingIntent;

public class BootReceiver extends BroadcastReceiver {

	ArrayList<ListItem> toDoList;

	//code starts here for this class
	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			generateNotification(context, intent);
		} catch (FileNotFoundException e) {
			Toast.makeText(context,
					"Notification Failed", Toast.LENGTH_LONG).show();
		}

	}

	public void load(Context context) throws FileNotFoundException {
		FileInputStream fis;
		try {
			fis = context.openFileInput("toDoListFile");
			ObjectInputStream ois = new ObjectInputStream(fis);
			toDoList = (ArrayList<ListItem>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			Toast.makeText(context,
					"Notification Failed to load", Toast.LENGTH_LONG).show();
		}
	}

	public void generateNotification(Context context, Intent intent)
			throws FileNotFoundException {
		//loads the list
		load(context);

		//sets the rnotification to lead to MainActivity.java
		Intent resultIntent = new Intent(context, MainActivity.class);

		//pending intent is created out of that intent
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	//builds a notification without contentText
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("You know you should")
				.setContentIntent(resultPendingIntent);
		//custom vibration pattern
		mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		mBuilder.setVibrate(new long[]{500, 500, 500, 500, 500, 500});
        mBuilder.setLights(Color.RED, 3000, 3000);

		//Pulls a random item from the unfinished list 
		int unfinishedCount = 0;
		for (int i = 0; i < toDoList.size(); i++) {
			if (toDoList.get(i).timesOnList > 0)
				unfinishedCount++;
		}

		Random rand = new Random();
		int random = rand.nextInt(unfinishedCount);
		String contentText = null;

		for (int i = 0; i < toDoList.size(); i++) {
			if (toDoList.get(i).timesOnList > 0) {

				if (random == 0) {
					contentText = toDoList.get(i).title;
					//sets the random list item as contextText
					mBuilder.setContentText(contentText);
					break;
				} else
					random--;

			}

		}



		//creates the notification
		NotificationManager mNotifyMgr = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		mNotifyMgr.notify(0, mBuilder.build());

	}

}