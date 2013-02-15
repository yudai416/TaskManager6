package jp.kk.tm6;

import java.lang.Thread.UncaughtExceptionHandler;

public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler{

	public void uncaughtException(Thread thread, Throwable ex) {
        //catchされなかった例外は最終的にココに渡される
	}

}