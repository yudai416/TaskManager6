package jp.kk.tm6;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

public class CheckBoxListener implements OnClickListener {
	private String tag;
	private SharedPreferences pref;
	private MainListItem item;

	public CheckBoxListener(SharedPreferences pref, MainListItem item) {
		this.pref = pref;
		this.item = item;
	}

	public void onClick(View view) {;
		addIgnore();
	}

	private void addIgnore(){
		tag = item.processName;
		String str = pref.getString(tag, "");
		Editor e = pref.edit();
		if(str.equals("false")) e.remove(tag);
		else e.putString(tag, "false");
		e.commit();
	}

}