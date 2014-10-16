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

public class CompletedActivity extends ActionBarActivity {

	ArrayList<ListItem> toDoList = new ArrayList<ListItem>();

	public void goToList() {
		Intent unfinishedActs = new Intent(this, MainActivity.class);
		startActivity(unfinishedActs);
	}

	public int getItemsFinished() {
		int z = 0;
		for (int i = 0; i < toDoList.size(); i++) {
			if ((toDoList.get(i).isFinished()))
				z++;
		}
		return z;
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

		int length = getItemsFinished();

		if (length == 0) {

			LinearLayout weightIt = new LinearLayout(this);
			((LinearLayout) linearLayout).addView(weightIt);

			// adding the new tasks in the new linear layout
			TextView valueTV = new TextView(this);
			valueTV.setText(R.string.emptyDone);
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

				if (!(toDoList.get(i).isFinished()))
					valueTV.setVisibility(View.GONE);

				((LinearLayout) weightIt).addView(valueTV);

				Button notFinished = new Button(this);
				notFinished.setText(R.string.notFinished);
				notFinished.setId(i);
				if (!(toDoList.get(i).isFinished()))
					notFinished.setVisibility(View.GONE);
				((LinearLayout) weightIt).addView(notFinished);

				notFinished.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							moveToDo(v);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

			}
		}

		TextView points = (TextView) findViewById(R.id.points);
		points.setText(Integer.toString(length));
	}

	public void moveToDo(View view) throws FileNotFoundException {

		toDoList.get(view.getId()).unfinished();
		save();
		createScreen();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completed);

		createScreen();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
