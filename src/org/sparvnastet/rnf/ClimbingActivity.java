/**
 * Copyright (c) 2012 Anders Sundman <anders@4zm.org>
 * 
 * This file is part of 'Rise and Fall' (RnF).
 * 
 * RnF is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * RnF is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with RnF.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sparvnastet.rnf;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Main activity of the application.
 * 
 * Handles user input and system events.
 */
public class ClimbingActivity extends Activity {
    public static final String LOGTAG = "RnF";

    /** Activity class overrides */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "onCreate");
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            ; // no saved instance
        } else {
            ; // saved data
        }

        Intent intent = getIntent();
        resolveIntent(intent);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.reset:
            ; // Do stuff
            break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void resolveIntent(Intent intent) {
        Log.i(LOGTAG, "resolveIntent action=" + intent.getAction());
        String action = intent.getAction();
    }
}
