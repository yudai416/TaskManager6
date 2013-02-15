package jp.kk.tm6;

import java.util.List;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class KillAllListener implements OnClickListener, SensorEventListener {
	private Context context;
	private ActivityManager activityManager;
	private List<MainListItem> items;
    private final static float FILTERING=0.1f;
    private Sensor accelerometer;
    private float[] values = new float[3];
	private double oldSum0;
    private KillMyProcess killMyProcess;

	public KillAllListener(Context context, ActivityManager activityManager, List<MainListItem> items, Sensor accelerometer) {
		this.context = context;
		this.activityManager = activityManager;
		this.items = items;
		this.accelerometer = accelerometer;
		this.oldSum0 = 0;
	}

	public void addKillMyProcess(KillMyProcess killMyProcess) {
		this.killMyProcess = killMyProcess;
	}

	public void onClick(View view) {
		killAll();
	}

	public void onSensorChanged(SensorEvent event) {
    	double sum1, ds;
        if (event.sensor == accelerometer) {
            values[0]=(event.values[0]*FILTERING)+(values[0]*(1.0f-FILTERING));
            values[1]=(event.values[1]*FILTERING)+(values[1]*(1.0f-FILTERING));
            values[2]=(event.values[2]*FILTERING)+(values[2]*(1.0f-FILTERING));
        }
        sum1 = Math.pow(values[0], 2) + Math.pow(values[1], 2) + Math.pow(values[2], 2);
        ds = Math.abs(sum1 - oldSum0);
        oldSum0 = sum1;
        if(ds > 15) killAll();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private void killAll() {
		boolean type = true;
		for(MainListItem item: items) {
			if(item.status) {
				if(item.processName.equals(context.getPackageName())) type = false;
				else killTask(item);
			}
		}
    	if(!type) {
    		Thread thread = new Thread() {
    			public void run() {
    				try {
    					Thread.sleep(1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    	    		killMyProcess.finishApp();
    			}
    		};
    		thread.start();
    	}
	}

    private void killTask(final MainListItem item) {
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
    				Toast.makeText(context, item.label + "のサービスを終了できませんでした", Toast.LENGTH_SHORT).show();
    			}
    		}
    	}
    	final int sleepTime = time;
		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				activityManager.killBackgroundProcesses(item.processName);
			}
		};
		thread.start();
    }
}