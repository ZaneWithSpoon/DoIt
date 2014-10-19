package zaneapps.doit;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {

	//creates a blank toDoList to overwrite saved file
	ArrayList<ListItem> toDoList = new ArrayList<ListItem>();
	int frequency;

	//happens when clear cache button is pressed
	public void clearCache(View v) {
		//creates a warning dialog
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked, toDoList is deleted 
					FileOutputStream fos;
					try {
						fos = openFileOutput("toDoListFile",
								Context.MODE_PRIVATE);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(toDoList);
						oos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked, nothing happens
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Are you sure? This will delete your To Do and Completed lists.")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();

	}

	//called when the Notifications off buttin is presses
	public void toggleNot(View v) {
		killNotifications();
	}

	//called when set button is pressed
	public void setFrequency(View v) {
		EditText editText = (EditText) findViewById(R.id.editTextInt);
		String message = editText.getText().toString();
		if (message.equals("")) {
		} else {
			editText.setText("");
			frequency = Integer.parseInt(message);
			makeNotification(frequency);
			updateFrequency();
		}
	}

	//creates an intent for CompletedActivity.java
	public void goToDone() {
		Intent completedActs = new Intent(this, CompletedActivity.class);
		startActivity(completedActs);
	}

	//creates an intent for MainActivity.java
	public void goToList() {
		Intent unfinishedActs = new Intent(this, MainActivity.class);
		startActivity(unfinishedActs);
	}

	public void makeNotification(int minutes) {

		// Schedule the alarm!
		Intent intent = new Intent(this, BootReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);// kills current alarm
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), (minutes * 60 * 1000),
				pendingIntent);
		
		//inform the user of the update
		Toast.makeText(getApplicationContext(),
				"Notifications have been initiated", Toast.LENGTH_LONG).show();

	}

	public void killNotifications() {

		// Schedule the alarm! so it can be killed
		Intent intent = new Intent(this, BootReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);// kills all alarms
		
		//inform the user of the update
		Toast.makeText(getApplicationContext(),
				"Notifications have been terminated", Toast.LENGTH_LONG).show();

	}

	//updates the frequency located at the bottom of the screen
	public void updateFrequency() {
		TextView points = (TextView) findViewById(R.id.textViewFreq);
		points.setText(Integer.toString(frequency));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_done:
			goToDone();
			return true;
		case R.id.action_list:
			goToList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
