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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	// declaring variables
	ArrayList<ListItem> toDoList = new ArrayList<ListItem>();
	Calendar cal = Calendar.getInstance();

	// returns the number of unfinished items in toDoList arrayList
	public int getItemsUnfinished() {
		int count = 0;
		for (int i = 0; i < toDoList.size(); i++) {
			if (toDoList.get(i).timesOnList > 0)
				count++;
		}
		return count;
	}

	// creates an intent for CompletedActivity.java
	public void goToDone() {
		Intent completedActs = new Intent(this, CompletedActivity.class);
		startActivity(completedActs);
	}

	// creates an intent for SettingsActivity.java
	public void goToSettings() {
		Intent settingActs = new Intent(this, SettingsActivity.class);
		startActivity(settingActs);
	}

	// saves ArrayList toDoList to file
	public void save() throws FileNotFoundException {
		FileOutputStream fos;
		try {
			fos = openFileOutput("toDoListFile", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(toDoList);
			oos.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"FileNotFoundException in save",
					Toast.LENGTH_LONG).show();
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
			Toast.makeText(getApplicationContext(),
					"FileNotFoundException in load",
					Toast.LENGTH_LONG).show();
		}
	}

	// populates the suggestions for the AutoComplete view item
	public void populateAutoComplete() throws FileNotFoundException{

		// loads the arrayList incase of updates
		load();

		// creates a string array of all
		String[] itemsArray = new String[toDoList.size()];
		for (int i = 0; i < toDoList.size(); i++) {
			itemsArray[i] = toDoList.get(i).title;
		}

		// setting Array Adapter for stringArray itemsArray
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, itemsArray);
		// Getting the instance of AutoCompleteTextView
		AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.newItem);
		actv.setThreshold(1);// will start working from first character
		actv.setAdapter(adapter);// setting the adapter data into the view
	}

	// creates most views on screen
	public void createScreen() throws FileNotFoundException {

		// removing all entries from the page
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.listView);
		linearLayout.removeAllViews();

		load(); // loading in case of updates

		// populates the auto-complete view
		populateAutoComplete();

		// populating the page
		int length = getItemsUnfinished(); // checking how many unfinished items there are
		if (length == 0) { // there are no unfinished items

			LinearLayout weightIt = new LinearLayout(this);
			((LinearLayout) linearLayout).addView(weightIt);

			TextView valueTV = new TextView(this);
			valueTV.setText(R.string.empty); // print out R.string.empty
			valueTV.setId(0);
			((LinearLayout) weightIt).addView(valueTV);

		}

		else { // there are unfinished items

			// a linear view is created for every to do list item
			// the ones that are finished are set to invisible
			for (int i = 0; i < toDoList.size(); i++) {

				LinearLayout weightIt = new LinearLayout(this);
				((LinearLayout) linearLayout).addView(weightIt);

				// setting the layout parameters for the textView items
				LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
						1.0f);

				// setting layout parameters for the buttons
				LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				textParams.gravity = Gravity.LEFT;

				// adding the new views in the new linear layout
				TextView valueTV = new TextView(this);
				// if there is more than 1 copy of an item, show how  many
				if (toDoList.get(i).timesOnList > 1)
					valueTV.setText(toDoList.get(i).title + " ("
							+ toDoList.get(i).timesOnList + ")");
				else
					// otherwise, just print the title
					valueTV.setText(toDoList.get(i).title);
				valueTV.setId(i);
				valueTV.setLayoutParams(textParams);

				// creating the completed button
				Button completed = new Button(this);
				completed.setText(R.string.completed);
				completed.setId(i);
				completed.setLayoutParams(buttonParams);

				completed.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {


							try {
								moveToDone(v); // move items to done if button is pressed
							} catch (FileNotFoundException e) {
								Toast.makeText(getApplicationContext(),
										"FileNotFoundException in moveToDone",
										Toast.LENGTH_LONG).show();
							} 
											
						

					}
				});

				// creating the delete button
				Button delete = new Button(this);
				delete.setText(R.string.delete);
				delete.setId(i);

				if (toDoList.get(i).timesOnList < 1) { // if they are finished make them invisible
					delete.setVisibility(View.GONE);
					completed.setVisibility(View.GONE);
					valueTV.setVisibility(View.GONE);
				}
				// delete.setLayoutParams(buttonParams);
				((LinearLayout) weightIt).addView(valueTV);
				((LinearLayout) weightIt).addView(completed);
				((LinearLayout) weightIt).addView(delete);

				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {


							try {
								removeItem(v);// delete items if button is pressed
							} catch (FileNotFoundException e) {
								Toast.makeText(getApplicationContext(),
										"FileNotFoundException in removeItem",
										Toast.LENGTH_LONG).show();
							}
						
						}

					}
			
				);
			}
	
		}

	}

	// The Activity starts in this method
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


			try {
				createScreen();
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(),
						"FileNotFoundException in createScreen",
						Toast.LENGTH_LONG).show();
			}


	}

	// when the addItem button is pressed
	public void addItem(View view) throws FileNotFoundException {

		AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.newItem);
		String message = autoCompleteTextView.getText().toString();
		if (message.equals("")) {// if message is blank, do nothing
		}
		else {//check to see if item has been entered before
			boolean newItem = true;
			autoCompleteTextView.setText("");
			for (int i = 0; i < toDoList.size(); i++) {
				if (message.equals(toDoList.get(i).title)) {
					toDoList.get(i).timesOnList++; //if item is not new, add a timesOnList counter to it
					newItem = false;
				}
			}

			if (newItem) {//if item is new, create new object
				ListItem li = new ListItem(message, cal.getTime());
				toDoList.add(li);
			}

			save();//save changes to list

			createScreen();//reprint the screen

		}
	}

	public void removeItem(View view) throws FileNotFoundException{

		// when delete button is pressed, remove a timesOnList counter

		toDoList.get(view.getId()).timesOnList--;
		
		save();
		
		createScreen();

	}

	public void moveToDone(View view) throws FileNotFoundException{
		//when completed button is pressed, run the ListItem method, finished(Date)

		toDoList.get(view.getId()).finished(cal.getTime());

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
			goToSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
