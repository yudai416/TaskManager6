package jp.kk.tm6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
//import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.widget.TextView;

public class RunApp extends Thread {
	private ActivityManager activityManager;
	private PackageManager packageManager;
	private ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	private TextView textView;
	private NumberFormat numberFormat = NumberFormat.getNumberInstance();
	private Handler handler= new Handler();
	private List<MainListItem> items, newItems;
	private ListArrayAdapter adapter;
	private List<CpuListItem> cpuData;
	private SharedPreferences pref, option;
	private boolean loopStatus;
	private int activityNo, sortNo;

	public RunApp(Context context, ActivityManager activityManager, TextView textView, List<MainListItem> items, ListArrayAdapter adapter, SharedPreferences pref, SharedPreferences option, int activityNo) {
		this.activityManager = activityManager;
		packageManager = context.getPackageManager();
		this.textView = textView;
		this.items = items;
		this.adapter = adapter;
		this.pref = pref;
		this.option = option;
		this.loopStatus = true;
		this.activityNo = makeNo(activityNo);
		this.sortNo = makeNo(sortNo);
	}

	public void run() {
		cpuData = new ArrayList<CpuListItem>();

		while(loopStatus){
			try {
				makeCpuData(items);
				newItems = new ArrayList<MainListItem>();
				runApp(newItems);
				activityManager.getMemoryInfo(memoryInfo);
				changeStatus(newItems);
				changeCpu(newItems);

				Collections.sort(newItems, new Comparator<MainListItem>(){
					public int compare(MainListItem item0, MainListItem item1) {
						String str = option.getString("sort", "");
						if(str.equals("memory")) return (int)(item1.memory - item0.memory);
						else if(str.equals("name")) return item0.label.compareTo(item1.label);
						else return item1.cpu - item0.cpu;
					}
			    });

				handler.post(new Runnable() {
					public void run() {
						//画面のテキストに取得した文字列をセット
						copyItems(items, newItems);
						textView.setText("空きメモリ量: " + numberFormat.format(memoryInfo.availMem/1024) + "kB");
						adapter.notifyDataSetChanged();
					}
				});
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopThread() {
		this.loopStatus = false;
	}

	private void runApp(List<MainListItem> items) {
		// 起動中のアプリ情報を取得
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_LAUNCHER);
		List<RunningAppProcessInfo> runningApp = activityManager.getRunningAppProcesses();
		List<RunningServiceInfo> runningService = activityManager.getRunningServices(100);
		List<ResolveInfo> appList =  packageManager.queryIntentActivities(it, 0);

		if(runningApp != null) {
			for(RunningAppProcessInfo app: runningApp) {
				try {
					// アプリ名をリストに追加
					ApplicationInfo appInfo = packageManager.getApplicationInfo(app.processName, 0);
					items.add(new MainListItem(packageManager.getApplicationIcon(appInfo), returnLabel(app.processName), app.processName, app.pid, usageMemory(app.pid), 0, true, app.importance, serviceClassNames(runningService, app), mainClassName(appList, app)));
				}
				catch(NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<ClassListItem> serviceClassNames(List<RunningServiceInfo> runningService, RunningAppProcessInfo app) {
		List<ClassListItem> classNames = new ArrayList<ClassListItem>();
		if(runningService != null) {
			for(RunningServiceInfo srv : runningService) {
				if(app.processName.equals(srv.process)) {
					classNames.add(new ClassListItem(srv.service.getClassName()));
				}
			}
		}
		return classNames;
	}

	private String mainClassName(List<ResolveInfo> appList, RunningAppProcessInfo app) {
		if(appList != null) {
			for(ResolveInfo resInfo : appList) {
				if(app.processName.equals(resInfo.activityInfo.processName)) {
					return resInfo.activityInfo.name;
				}
			}
		}
		return "";
	}

	private int makeUsageCpu(int pid) {
		try{
			String buf = "/proc/" + String.valueOf(pid) + "/stat";
			FileReader in = new FileReader(buf);
			BufferedReader br = new BufferedReader(in);
			String line;
			int result = 0;

			while ((line = br.readLine()) != null) {
				String[] strAry = line.split(" ");
				result = Integer.valueOf(strAry[12]) + Integer.valueOf(strAry[13]);
			}
			br.close();
			in.close();
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void makeCpuData(List<MainListItem> items) {
		for(final MainListItem item: items) {
			Thread thread = new Thread() {
				public void run() {
					int min = 1000, oldData, newData, result;
					boolean type = true;
					oldData = makeUsageCpu(item.pid);
					try {
						Thread.sleep(min);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					newData = makeUsageCpu(item.pid);
					result = (newData - oldData) * 1000 / min;
					if(result < 0) result = 0;
					for(CpuListItem item2: cpuData) {
						if(item2.pid == item.pid) {
							if(result == 0) cpuData.remove(item2);
							else item2.cpu = result;
							type = false;
							break;
						}
					}
					if(type && result != 0) cpuData.add(new CpuListItem(item.pid, result));
				}
			};
			thread.start();
		}
	}

	private int usageMemory(int pid) {
		int pids[] = new int[1];
		pids[0] = pid;
		MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(pids);

		return memoryInfos[0].getTotalPss();
	}


	private void changeCpu(List<MainListItem> items) {
		for(CpuListItem item2: cpuData) {
			for(MainListItem item1: items) {
				if(item1.pid == item2.pid){
					item1.cpu = item2.cpu;
					break;
				}
			}
		}
	}

	private void changeStatus(List<MainListItem> items){
		for(MainListItem item: items) {
			String str = pref.getString(item.processName, "");
			if(str.equals("false")) item.status = false;
		}
	}

	protected void copyItems(List<MainListItem> items1, List<MainListItem> items2) {
		items1.clear();
		for(MainListItem item: items2) {
			if(activityNo == 0) items1.add(item);
			else if(activityNo == 1 && item.status == false) items1.add(item);
			else if(activityNo == 2 && item.status == true) items1.add(item);
		}
	}

	protected String returnLabel(String tag) {
		try {
			return (String)packageManager.getApplicationLabel(packageManager.getApplicationInfo(tag, 0));
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private int makeNo(int no) {
		if(no > -1 && no < 3) return no;
		else return 0;
	}
}