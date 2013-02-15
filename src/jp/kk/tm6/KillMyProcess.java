package jp.kk.tm6;

import android.hardware.SensorManager;
import android.os.Process;

public class KillMyProcess {
    private SensorManager sensorManager;
    private KillAllListener killAllListener;

    public KillMyProcess(SensorManager sensorManager, KillAllListener killAllListener) {
    	this.sensorManager = sensorManager;
    	this.killAllListener = killAllListener;
    }

    protected void finishApp() {
        sensorManager.unregisterListener(killAllListener);
		Process.killProcess(Process.myPid());
    }
}