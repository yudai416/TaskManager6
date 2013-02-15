package jp.kk.tm6;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

abstract public class MyActivity extends Activity{
    private final static int MENU_ITEM0 = 0, MENU_ITEM1 = 1;
	private ActivityManager activityManager;
	private SharedPreferences pref, option;
	private RunApp runApp;
	private List<MainListItem> items;
	private ListArrayAdapter adapter;
	private ListView listView;
	private TextView textView1, textView2;;
    private boolean type = false;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private KillAllListener killAllListener;
    private KillMyProcess killMyProcess;

    protected TextView textView0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        option = getSharedPreferences("option", MODE_PRIVATE);
    	textView0 = makeTextView("", 20);
    	textView0.setGravity(Gravity.CENTER);
    	textView1 = makeTextView("空きメモリ量", 17);
    	textView2 = makeTextView("2828", 17);

    	activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
    	items = new ArrayList<MainListItem>();
    	adapter = new ListArrayAdapter(this, items, pref);

    	runApp = new RunApp(this, activityManager, textView1, items, adapter, pref, option, makeActivityNo());
    	runApp.start();

    	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if (list.size() > 0) accelerometer = list.get(0);

    	killAllListener = new KillAllListener(this, activityManager, items, accelerometer);
    	killMyProcess = new KillMyProcess(sensorManager, killAllListener);
    	killAllListener.addKillMyProcess(killMyProcess);

    	listView = makeListView();
    	setContentView(makeLayout());
    }

    @Override
    public void onResume() {
    	super.onResume();
		String str = option.getString("shake", "");
    	if(str.equals("OFF")) {
            sensorManager.unregisterListener(killAllListener);
            textView2.setText("シェイク: OFF");
    	}
    	else if (accelerometer!=null) {
            sensorManager.registerListener(killAllListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            textView2.setText("シェイク: ON");
        }
    }

    @Override
    public void onRestart() {
    	super.onRestart();
    	runApp = new RunApp(this, activityManager, textView1, items, adapter, pref, option, makeActivityNo());
    	runApp.start();
    }

    @Override
    public void onStop() {
    	super.onStop();
    	runApp.stopThread();
        sensorManager.unregisterListener(killAllListener);
    	if(type) finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem item0 = menu.add(0, MENU_ITEM0, 0, "アプリの設定");
    	MenuItem item1 = menu.add(0, MENU_ITEM1, 0, "アプリ終了");
    	item0.setIcon(R.drawable.star);
    	item1.setIcon(R.drawable.chon);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        type = true;
    	switch (item.getItemId()) {
    	case MENU_ITEM0:
    		startActivity(new Intent(this, Option.class));
    		type = false;
    		break;
    	case MENU_ITEM1:
            killMyProcess.finishApp();
    		break;
    	}
    	return true;
    }

    private LinearLayout makeLayout() {
    	LinearLayout layout = new LinearLayout(this);
    	String str = ruleOfButton();

        layout.setBackgroundColor(Color.BLACK);
        //layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(textView0);
        layout.addView(textView1);
        layout.addView(textView2);
        if(!str.equals("null")) layout.addView(makeButton(str));
        layout.addView(listView);
        return layout;
    }

    private Button makeButton(String str) {
    	Button button = new Button(this);
    	button.setText(str);
    	button.setOnClickListener(killAllListener);
    	return button;
    }

    private TextView makeTextView(String text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(size);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    private ListView makeListView() {
        listView = new ListView(this);
        listView.setScrollingCacheEnabled(false);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new ListviewListener(this, activityManager, items, killMyProcess));
        return listView;
    }

    abstract protected int makeActivityNo();
    abstract protected String ruleOfButton();
}