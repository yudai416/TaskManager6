package jp.kk.tm6;

import java.util.List;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListviewListener implements OnItemClickListener, OnTimeSetListener {
	private Context context;
	private ActivityManager activityManager;
	private List<MainListItem> items;
	private MainListItem item;
	private int kind;
	private int msec;
	private KillMyProcess killMyProcess;

	public ListviewListener(Context context, ActivityManager activityManager, List<MainListItem> items, KillMyProcess killMyProcess) {
		this.context = context;
		this.activityManager = activityManager;
		this.items = items;
		this.killMyProcess = killMyProcess;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.item = returnItem((String)view.getTag());
    	if(item.processName.equals(context.getPackageName())) {
    		showYesNoDialog(context, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				killMyProcess.finishApp();
    			}
    		});
    	}
    	else {
        	CharSequence[] functions = { "アプリの終了", "アプリの起動", "オフタイマー" };
    		showDialog(context, functions, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				kind = which;
    				if(which == 0) {
    					showYesNoDialog(context, new DialogInterface.OnClickListener() {
    		    			public void onClick(DialogInterface dialog, int which) {
    		    				killTask();
    		    			}
    		    		});
    				}
    				else if(which == 1) {
    					startApp();
    				}
    				else if(which == 2) {
    					offTimer();
    				}
    			}
    		});
    	}
	}

    private MainListItem returnItem(String tag) {
    	for(MainListItem item: items) {
    		if(item.processName.equals(tag)) {
    			return item;
    		}
    	}
    	return null;
    }

    private void showYesNoDialog(Context context, DialogInterface.OnClickListener listener) {
    	AlertDialog.Builder ad = new AlertDialog.Builder(context);
    	ad.setTitle("終了確認");
    	ad.setMessage(item.label + "を終了しますか？");
    	ad.setPositiveButton("はい", listener);
    	ad.setNegativeButton("いいえ", null);
    	ad.show();
    }

    private void showDialog(Context context, CharSequence[] functions, DialogInterface.OnClickListener listener) {
    	AlertDialog.Builder ad = new AlertDialog.Builder(context);
    	//ad.setTitle("タイトル");
    	ad.setItems(functions, listener);
    	ad.show();
    }

    private void killTask() {
    	int time = 0;
    	if(!item.classNames.isEmpty()) {
    		for(ClassListItem name: item.classNames) {
    			Intent intent = new Intent();
    			intent.setClassName(item.processName, name.className);
    			try{
    				time = 1000;
    				context.stopService(intent);
    			}
    			catch(SecurityException e) {
    				makeToast(item.label + "のサービスを終了できませんでした");
    			}
    		}
    	}
    	final int sleepTime = time;
    	makeToast(item.label + "を終了します");
    	Thread thread = new Thread() {
    		public void run() {
    			try {
    				Thread.sleep(sleepTime);
    			}
    			catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			activityManager.killBackgroundProcesses(item.processName);
    		}
    	};
    	thread.start();
    }

    private void startApp() {
    	if(item.mainClassName.equals("")) {
    		makeToast(item.label + "は実行できません");
    	}
    	else {
    		Intent intent = new Intent();
    		intent.setComponent(new ComponentName(item.processName, item.mainClassName));
    		context.startActivity(intent);
    	}
    }

    private void offTimer() {
    	TimerDialog timerDialog = new TimerDialog(context, this, 0, 0, true);
    	timerDialog.setTitle("オフタイマー[分, 秒]");
    	timerDialog.show();
    }

    private void makeToast(String text) {
    	if(kind != 2) {
	    	Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    	}
    }

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String str;
		if(hourOfDay == 0) str = String.valueOf(minute) + "秒後に";
		else str = String.valueOf(hourOfDay) + "分" + String.valueOf(minute) + "秒後に";
		msec = (hourOfDay * 60 + minute) * 1000;
    	Toast.makeText(context, str + "秒後に" + item.label + "を終了します", Toast.LENGTH_SHORT).show();
    	Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(msec);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	killTask();
			}
		};
		thread.start();
	}
}