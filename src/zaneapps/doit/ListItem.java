package zaneapps.doit;

import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;

public class ListItem implements Serializable {

	/**
	 * @param args
	 */
	public String title;
	public Date date;
	public boolean completed;

	public ListItem(String name, Date d) {

		title = name;
		date = d;
		completed = false;

	}

	public String getTitle() {
		return title;
	}

	public void finished() {
		completed = true;
	}
	
	public void unfinished() {
		completed = false;
	}

	public boolean isFinished() {
		return completed;
	}

}
