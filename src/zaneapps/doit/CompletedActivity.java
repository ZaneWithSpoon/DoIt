package zaneapps.doit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CompletedActivity extends ActionBarActivity {

	ArrayList<ListItem> toDoList = new ArrayList<ListItem>();

	// created an intent for MainActivity.java
	public void goToList() {
		Intent unfinishedActs = new Intent(this, MainActivity.class);
		startActivity(unfinishedActs);
	}

	// creates an intent for MoreActivity.java
	public void goToMore(int i) {
		Intent moreInfo = new Intent(this, MoreActivity.class);
		moreInfo.putExtra("index", i);
		startActivity(moreInfo);
	}

	// creates an intent for SettingsActivity.java
	public void goToSettings() {
		Intent settingActs = new Intent(this, SettingsActivity.class);
		startActivity(settingActs);
	}

	// returns the number of Finished Items
	public int getItemsFinished() {
		int count = 0;
		for (int i = 0; i < toDoList.size(); i++) {
			if ((toDoList.get(i).timesCompleted) > 0)
				count++;
		}
		return count;
	}

	// saves
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

	public void createScreen() throws FileNotFoundException {

		// removing all entries from the page
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.listView);
		linearLayout.removeAllViews();

		load();

		
		// calculating productivity points
		int score = 0;
		for (int i = 0; i < toDoList.size(); i++) {
			score += toDoList.get(i).timesCompleted;
		}
		TextView points = (TextView) findViewById(R.id.points);
		points.setText(Integer.toString(score));

		//populating the page
		int length = getItemsFinished();//getting the number of finished items
		if (length == 0) {

			LinearLayout weightIt = new LinearLayout(this);
			((LinearLayout) linearLayout).addView(weightIt);

			// adding the new tasks in the new linear layout
			TextView valueTV = new TextView(this);
			//if there are no finished items, add the string R.string.emptyDone
			valueTV.setText(R.string.emptyDone);
			valueTV.setId(0);
			((LinearLayout) weightIt).addView(valueTV);

		}

		else { //more that 0 finished items

			// a linear view is created for every to do list item
			// the ones that are finished are set to invisible
			for (int i = 0; i < toDoList.size(); i++) {

				//new linearlayout added
				LinearLayout weightIt = new LinearLayout(this);
				((LinearLayout) linearLayout).addView(weightIt);

				//creating parameters for text
				LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
						1.0f);

				// adding the new views to the new linear layout
				TextView valueTV = new TextView(this);
				//if this task was completed more than once, print how many
				if (toDoList.get(i).timesCompleted > 1){
					valueTV.setText(toDoList.get(i).title + " ("
							+ toDoList.get(i).timesCompleted + ")");
				}
				else//else print title
					valueTV.setText(toDoList.get(i).title);
				valueTV.setId(i);
				valueTV.setLayoutParams(textParams);

				
				Button notFinished = new Button(this);
				notFinished.setText(R.string.more);
				notFinished.setId(i);
				//if this item isn't finished, set to invisible
				if (toDoList.get(i).timesCompleted < 1){
					notFinished.setVisibility(View.GONE);
					valueTV.setVisibility(View.GONE);
				}
				
				((LinearLayout) weightIt).addView(valueTV);
				((LinearLayout) weightIt).addView(notFinished);

				notFinished.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						goToMore(v.getId());
					}
				});
			}
		}
	}

	
	//code for this activity starts here
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completed);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		try {
			createScreen();//creates screen
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(),
					"FileNotFoundException in createScreen",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.completed, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_list:
			goToList();
			return true;
		case R.id.action_settings:
			goToSettings();
			return true;
		case android.R.id.home:
			goToList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
