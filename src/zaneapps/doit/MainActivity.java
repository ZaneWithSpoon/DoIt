package zaneapps.doit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	ArrayList<ListItem> toDoList = new ArrayList<ListItem>();
	int frequency;
	Calendar cal = Calendar.getInstance();

	public ArrayList<ListItem> getToDoList() {
		return toDoList;

	}

	public int getItemsUnfinished() {
		int z = 0;
		for (int i = 0; i < toDoList.size(); i++) {
			if (!(toDoList.get(i).isFinished()))
				z++;
		}
		return z;
	}

	public void goToDone() {
		 Intent completedActs = new Intent(this, CompletedActivity.class);
		 startActivity(completedActs);
	}

	public void save() throws FileNotFoundException {
		FileOutputStream fos;
		try {
			fos = openFileOutput("toDoListFile", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(toDoList);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() throws FileNotFoundException {
		FileInputStream fis;
		try {
			fis = openFileInput("toDoListFile");
			ObjectInputStream ois = new ObjectInputStream(fis);
			toDoList = (ArrayList<ListItem>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createScreen() {

		// removing all entries from the page
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.listView);
		linearLayout.removeAllViews();

		// populating the page
		try {
			load();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int length = getItemsUnfinished();

		if (length == 0) {

			LinearLayout weightIt = new LinearLayout(this);
			((LinearLayout) linearLayout).addView(weightIt);

			// adding the new tasks in the new linear layout
			TextView valueTV = new TextView(this);
			valueTV.setText(R.string.empty);
			valueTV.setId(0);
			// valueTV.setLayoutParams(new
			// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT, 1.0f));
			((LinearLayout) weightIt).addView(valueTV);

		}

		else {

			for (int i = 0; i < toDoList.size(); i++) {

				LinearLayout weightIt = new LinearLayout(this);
				// weightIt.setLayoutParams(new
				// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				// LayoutParams.WRAP_CONTENT,1));
				LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
						1.0f);
				textParams.weight = 1.0f;
				// textParams.gravity=Gravity.RIGHT;

				LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				// textParams.weight = 1.0f;
				textParams.gravity = Gravity.LEFT;

				// weightIt.setLayoutParams(layoutParams);

				((LinearLayout) linearLayout).addView(weightIt);

				// adding the new tasks in the new linear layout
				TextView valueTV = new TextView(this);
				valueTV.setText(toDoList.get(i).getTitle());
				valueTV.setId(i);
				valueTV.setMaxWidth(425);
				valueTV.setLayoutParams(textParams);

				if (toDoList.get(i).isFinished())
					valueTV.setVisibility(View.GONE);

				((LinearLayout) weightIt).addView(valueTV);

				Button completed = new Button(this);
				completed.setText(R.string.completed);
				completed.setId(i);
				completed.setLayoutParams(buttonParams);
				if (toDoList.get(i).isFinished())
					completed.setVisibility(View.GONE);

				((LinearLayout) weightIt).addView(completed);

				completed.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							moveToDone(v);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

				Button delete = new Button(this);
				delete.setText(R.string.delete);
				delete.setId(i);
				if (toDoList.get(i).isFinished())
					delete.setVisibility(View.GONE);
				// delete.setLayoutParams(buttonParams);
				((LinearLayout) weightIt).addView(delete);

				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							removeItem(v);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createScreen();

		// makeNotification();

		// Schedule the alarm!
		// Calendar cal = Calendar.getInstance();
		// startService(new Intent(getApplicationContext(), MyService.class));
		// Intent intent = new Intent(this, BootReceiver.class);
		// intent.setData((Uri.parse("notifications")));
		// PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
		// intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// AlarmManager alarmManager = (AlarmManager)
		// getSystemService(ALARM_SERVICE);
		// alarmManager.cancel(pendingIntent);
		// if(frequency != 60)
		// frequency = 120;
		// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		// System.currentTimeMillis(), (frequency*60*1000), pendingIntent);

	}

	// when the addItem button is pressed
	public void addItem(View view) throws FileNotFoundException {

		// do something in response to button
		EditText editText = (EditText) findViewById(R.id.newItem);
		String message = editText.getText().toString();
		if (message.equals("")) {
		} else {
			editText.setText("");
			ListItem li = new ListItem(message, cal.getTime());
			toDoList.add(li);

			save();

			createScreen();

		}
	}

	public void removeItem(View view) throws FileNotFoundException {

		// does something when delete button is pressed

		toDoList.remove(view.getId());
		save();

		createScreen();

	}

	public void moveToDone(View view) throws FileNotFoundException {
		// does something when completed button is pressed

		toDoList.get(view.getId()).finished();

		save();

		createScreen();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_done:
			goToDone();
			return true;
		case R.id.action_settings:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
