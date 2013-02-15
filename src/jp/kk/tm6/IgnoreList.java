package jp.kk.tm6;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class IgnoreList extends MyActivity {
    private final static int MENU_ITEM2 = 2, MENU_ITEM3 = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView0.setText(StringRes.ignoreListJP);
    }

    @Override
    protected int makeActivityNo() {
    	return 1;
    }

    @Override
    protected String ruleOfButton() {
    	return "null";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem item2 = menu.add(0, MENU_ITEM2, 0, StringRes.allListJP);
    	MenuItem item3 = menu.add(0, MENU_ITEM3, 0, StringRes.killListJP);
    	item2.setIcon(R.drawable.one);
    	item3.setIcon(R.drawable.three);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
    	case MENU_ITEM2:
    		startActivity(new Intent(this, AllList.class));
    		break;
    	case MENU_ITEM3:
    		startActivity(new Intent(this, KillList.class));
    		break;
    	}
    	return true;
    }
}