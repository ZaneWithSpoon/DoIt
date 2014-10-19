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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreActivity extends ActionBarActivity {

	ArrayList<ListItem> toDoList = new ArrayList<ListItem>();
	int index;

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

	public void moveToDo(View v) throws FileNotFoundException {

		toDoList.get(index).unfinished();
		save();
		goToDone();
	}

	public void goToDone() {
		Intent completedActs = new Intent(this, CompletedActivity.class);
		startActivity(completedActs);
	}

	public void createScreen() throws FileNotFoundException {

		load();

		TextView textViewTitle = (TextView) findViewById(R.id.textViewName);
		textViewTitle.setText(toDoList.get(index).title);

		TextView textViewTimesComp = (TextView) findViewById(R.id.textViewTimes);
		textViewTimesComp
				.setText(Integer.toString(toDoList.get(index).timesCompleted));

		TextView textViewStarted = (TextView) findViewById(R.id.textViewStarted);
		textViewStarted.setText(toDoList.get(index).getStartDate());

		TextView textViewFinished = (TextView) findViewById(R.id.textViewCompleted);
		textViewFinished.setText(toDoList.get(index).getEndDate());

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		index = extras.getInt("index");

		try {
			createScreen();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.more, menu);
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
	    case android.R.id.home:
	        goToDone();
	        return true;

		default:return super.onOptionsItemSelected(item);
	}
}
}
