package jp.kk.tm6;

import java.util.List;

import android.graphics.drawable.Drawable;

public class MainListItem {
	public Drawable icon;
	public String label;
	public String processName;
	public int pid;
	public long memory;
	public int cpu;
	public boolean status;
	public int importance;
	public List<ClassListItem> classNames;
	public String mainClassName;

	public MainListItem(Drawable icon, String label, String processName, int pid, long memory, int cpu, boolean status, int importance, List<ClassListItem> classNames, String mainClassName) {
		this.icon = icon;
		this.label = label;
		this.processName = processName;
		this.pid = pid;
		this.memory = memory;
		this.cpu = cpu;
		this.status = status;
		this.importance = importance;
		this.classNames = classNames;
		this.mainClassName = mainClassName;
	}
}