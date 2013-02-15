package jp.kk.tm6;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AllList extends MyActivity {
    private final static int MENU_ITEM2 = 2, MENU_ITEM3 = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView0.setText(StringRes.allListJP);
    }

    @Override
    protected int makeActivityNo() {
    	return 0;
    }

    @Override
    protected String ruleOfButton() {
    	return "除外以外すべてKILL";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem item2 = menu.add(0, MENU_ITEM2, 0, StringRes.ignoreListJP);
    	MenuItem item3 = menu.add(0, MENU_ITEM3, 0, StringRes.killListJP);
    	item2.setIcon(R.drawable.two);
    	item3.setIcon(R.drawable.three);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
    	case MENU_ITEM2:
    		startActivity(new Intent(this, IgnoreList.class));
    		break;
    		case MENU_ITEM3:
    			startActivity(new Intent(this, KillList.class));
    			break;
    	}
    	return true;
    }

}