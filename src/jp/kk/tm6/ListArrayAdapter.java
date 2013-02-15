package jp.kk.tm6;

import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListArrayAdapter extends ArrayAdapter<MainListItem> {
	private List<MainListItem> items;
	private SharedPreferences pref;

	public ListArrayAdapter(Context context, List<MainListItem> items, SharedPreferences pref) {
		super(context, -1, items);
		this.items = items;
		this.pref = pref;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		Context context = getContext();
		MainListItem item = items.get(position);

		if(convertView == null) {
			LinearLayout layout = new LinearLayout(context);
			layout.setPadding(0, 5, 0, 5);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			convertView = layout;

	        CheckBox checkBox = new CheckBox(context);
	        checkBox.setTag("check");
	        checkBox.setFocusable(false);
	        layout.addView(checkBox);

			ImageView imageView = new ImageView(context);
			imageView.setTag("icon");
			imageView.setAdjustViewBounds(true);
			imageView.setMaxHeight(60);
			imageView.setMaxWidth(60);
			layout.addView(imageView);

			TextView textView = new TextView(context);
			textView.setTag("text");
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(16);
			layout.addView(textView);
		}

		ImageView imageView = (ImageView)convertView.findViewWithTag("icon");
		imageView.setImageDrawable(item.icon);
		TextView textView = (TextView)convertView.findViewWithTag("text");
		textView.setText(item.label + "\n" + item.memory + "kB, " + item.cpu + "%, " + kind(item.importance));
		CheckBox checkBox = (CheckBox)convertView.findViewWithTag("check");
        checkBox.setChecked(item.status);
        checkBox.setOnClickListener(new CheckBoxListener(pref, item));

		convertView.setTag(item.processName);

		return convertView;
	}

	private String kind(int importance) {
		String str = "";
		if(importance == 100) str = "foreground";
		else if(importance == 130) str = "activity";
		else if(importance == 200) str = "activity";
		else if(importance == 300) str = "service";
		else if(importance == 400) str = "background";
		else if(importance == 500) str = "empty";
		return str;
	}

}