package com.msoma.mmeks.common;

/**
 * Created by gikundi on 2/17/15.
 */

import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.msoma.mmeks.R;


public class EnhancedMenuInflater {

    public static void inflate(MenuInflater inflater, Menu menu, boolean forceVisible) {
        inflater.inflate(R.menu.menu_main, menu);

        if (!forceVisible) {
            return;
        }

        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            // check if app:showAsAction = "ifRoom"
            if (((MenuItemImpl) item).requestsActionButton()) {
                item.setShowAsAction(SupportMenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
    }
}