package jp.kk.tm6;

import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Option extends Activity implements OnClickListener {
	private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private TextView textView0, textView1, textView2, textView3, textView4, textView5;
	private Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
	private Button button0, button1, button2;
	private SharedPreferences option;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);
        option = getSharedPreferences("option", MODE_PRIVATE);
        /*
        textView0 = makeTextView("アプリの設定", 22);
    	textView0.setGravity(Gravity.CENTER);
        textView1 = makeTextView("初期起動リスト", 17);
        textView2 = makeTextView("　　ソート　　", 17);
        textView3 = makeTextView("　シェイク　　", 17);
        textView4 = makeTextView("除外リストレベル　", 17);
        textView5 = makeTextView("　　言語　　　", 17);
    	spinner1 = makeSpinner(R.id.spinner1, R.array.sp1_jp);
    	spinner2 = makeSpinner(R.id.spinner2, R.array.sp1_jp);
    	spinner3 = makeSpinner(R.id.spinner3, R.array.sp1_jp);
    	spinner4 = makeSpinner(R.id.spinner4, R.array.sp1_jp);
    	spinner5 = makeSpinner(R.id.spinner5, R.array.sp1_jp);
    	*/
    	spinner1 = makeSpinner1();
    	spinner2 = makeSpinner2();
    	spinner3 = makeSpinner3();
    	spinner4 = makeSpinner4();
    	spinner5 = makeSpinner5();
    	/*
    	button0 = makeButton("設定保存");
    	button1 = makeButton("リセット");
    	button2 = makeButton("除外リスト作成");

    	LinearLayout layout0 = makeLayout(false);
    	LinearLayout layout1 = makeLayout(true);
    	LinearLayout layout2 = makeLayout(true);
    	LinearLayout layout3 = makeLayout(true);
    	LinearLayout layout4 = makeLayout(true);
    	LinearLayout layout5 = makeLayout(true);
    	LinearLayout layout6 = makeLayout(true);

        layout1.addView(textView1);
        layout1.addView(spinner0);
        layout2.addView(textView2);
        layout2.addView(spinner1);
        layout3.addView(textView3);
        layout3.addView(spinner2);
        layout4.addView(textView4);
        layout4.addView(spinner3);
        layout5.addView(textView5);
        layout5.addView(spinner4);
        layout6.addView(button0);
        layout6.addView(button1);
        layout6.addView(button2);
        layout0.addView(textView0);
        layout0.addView(layout1);
        layout0.addView(layout2);
        layout0.addView(layout3);
        layout0.addView(layout4);
        layout0.addView(layout5);
        layout0.addView(layout6);
    	 */
        //setContentView(layout0);
        //setContentView(R.layout.option);
    }

    private LinearLayout makeLayout(boolean type) {
    	LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.BLACK);
    	if(type) {
            layout.setOrientation(LinearLayout.HORIZONTAL);
    		layout.setGravity(Gravity.CENTER);
    	}
    	else layout.setOrientation(LinearLayout.VERTICAL);
    	return layout;
    }

    private TextView makeTextView(String text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(size);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    private Spinner makeSpinner(int spinnerId, int arrayId, int first) {
    	Spinner spinner = (Spinner)findViewById(spinnerId);
    	String[] names = getResources().getStringArray(arrayId);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(adapter);
    	spinner.setSelection(first);
    	return spinner;
    }

    private Spinner makeSpinner1() {
		String str = option.getString("firstActivity", "");
    	int first = 0;
		if(str.equals("IgnoreList")) first = 1;
		else if(str.equals("KillList")) first = 2;
		else first = 0;
    	return makeSpinner(R.id.spinner1, R.array.sp1_jp, first);
    }

    private Spinner makeSpinner2() {
		String str = option.getString("sort", "");
    	int first = 0;
		if(str.equals("memory")) first = 1;
		else if(str.equals("name")) first = 2;
		else first = 0;
    	return makeSpinner(R.id.spinner2, R.array.sp2_jp, first);
    }

    private Spinner makeSpinner3() {
		String str = option.getString("shake", "");
    	int first = 0;
		if(str.equals("OFF")) first = 1;
		else first = 0;
    	return makeSpinner(R.id.spinner3, R.array.sp3, first);
    }

    private Spinner makeSpinner4() {
		String str = option.getString("security", "");
    	int first = 4;
		if(str.equals("max")) first = 0;
		else if(str.equals("high")) first = 1;
		else if(str.equals("middle")) first = 2;
		else if(str.equals("low")) first = 3;
		else first = 4;
    	return makeSpinner(R.id.spinner4, R.array.sp4_jp, first);
    }

    private Spinner makeSpinner5() {
    	int first = 0;
		String str = option.getString("language", "");
		if(str.equals("English")) first = 1;
		else first = 0;
    	return makeSpinner(R.id.spinner5, R.array.sp5, first);
    }
    
    /*
    private Spinner makeSpinner(String[] strs, int first) {
    	String[] names = getResources().getStringArray(R.array.sp1);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strs);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	//for(int i = 0; i < strs.length; i++) adapter.add(strs[i]);
    	Spinner spinner = new Spinner(this);
    	spinner.setAdapter(adapter);
    	spinner.setSelection(first);
    	spinner.setLayoutParams(new LinearLayout.LayoutParams(WC,WC));
    	return spinner;
    }
    */
    
    private Button makeButton(String text) {
    	Button button = new Button(this);
    	button.setText(text);
    	button.setLayoutParams(new LinearLayout.LayoutParams(WC,WC));
    	button.setTag(text);
    	button.setOnClickListener(this);
    	return button;
    }

    private void makeIgnoreList(String str) {
    	ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApp = activityManager.getRunningAppProcesses();

		if(runningApp != null) {
    		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        	Editor editor1 = pref.edit();
        	editor1.clear();
			for(RunningAppProcessInfo app : runningApp) {
				if(str.equals("first")) {
					break;
				}
				else if(str.equals("max")) {
					editor1.putString(app.processName, "false");
				}
				else if(str.equals("high")) {
					if(app.importance != 500) editor1.putString(app.processName, "false");
				}
				else if(str.equals("middle")) {
					if(app.importance != 500 && app.importance != 400) editor1.putString(app.processName, "false");
				}
				else if(str.equals("low")) {
					if(app.importance != 500 && app.importance != 400) {
						if(app.importance != 300) editor1.putString(app.processName, "true");
					}
				}
			}
        	editor1.commit();
		}
		Toast.makeText(this, "除外リストを新たに作成しました", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view){
		String tag = (String)view.getTag();
    	Editor editor0 = option.edit();

    	if(tag.equals("設定保存")) {
    		String buf;
    		String str0 = String.valueOf(spinner1.getSelectedItem());
    		if(str0.equals("除去リスト")) buf = "IgnoreList";
    		else if(str0.equals("終了リスト")) buf = "KillList";
    		else buf = "AllList";
    		editor0.putString("firstActivity", buf);

    		String str1 = String.valueOf(spinner2.getSelectedItem());
    		if(str1.equals("メモリ使用量")) buf = "memory";
    		else if(str1.equals("アプリ名")) buf = "name";
    		else buf = "cpu";
    		editor0.putString("sort", buf);

    		String str2 = String.valueOf(spinner3.getSelectedItem());
    		editor0.putString("shake", str2);

        	editor0.commit();
        	finish();
    	}
    	else if(tag.equals("リセット")){
    		editor0.clear();
        	editor0.commit();
        	spinner1.setSelection(0);
    	}
    	else {
    		String str = String.valueOf(spinner4.getSelectedItem());
    		String buf;
    		if(str.equals("最高")) buf = "max";
    		else if(str.equals("高")) buf = "high";
    		else if(str.equals("中")) buf = "middle";
    		else if(str.equals("低")) buf = "low";
    		else buf = "first";
    		editor0.putString("security", buf);
        	editor0.commit();
        	makeIgnoreList(buf);
    	}
    }

}