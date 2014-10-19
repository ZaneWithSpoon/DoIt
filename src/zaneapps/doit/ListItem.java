package zaneapps.doit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;

public class ListItem implements Serializable {

	/**
	 * @param args
	 */
	public String title;
	public Date dateStarted;
	public Date dateCompleted;
	public boolean completed;
	public int timesCompleted;
	public int timesOnList;

	public ListItem(String name, Date d) {

		title = name;
		dateStarted = d;
		completed = false;
		timesOnList++;

	}

	public String getStartDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		String date = sdf.format(dateStarted);
		return date;
	}

	public String getEndDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		String date = sdf.format(dateCompleted);
		return date;
	}

	public void finished(Date finished) {
		dateCompleted = finished;
		timesOnList--;
		timesCompleted++;
	}

	public void unfinished() {
		timesOnList++;
		timesCompleted--;
	}

}
