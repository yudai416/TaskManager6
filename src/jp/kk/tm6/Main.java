package jp.kk.tm6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Main extends Activity {
	private SharedPreferences option;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        option = getSharedPreferences("option", MODE_PRIVATE);
		String str = option.getString("firstActivity", "");
		if(str.equals("IgnoreList")) startActivity(new Intent(this, IgnoreList.class));
		else if(str.equals("KillList")) startActivity(new Intent(this, KillList.class));
		else startActivity(new Intent(this, AllList.class));
    }

}