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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

/**
 * Climb activity is main activity of the game. It shows the climbing view and
 * handles some basic state save/restore.
 */
public class ClimbingActivity extends Activity {
    private static final String LOGTAG = "RnF";

    private static final int MENU_START = 1;
    private static final int MENU_PAUSE = 2;
    private static final int MENU_RESUME = 3;
    private static final int MENU_CANCEL = 4;

    private Game game_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "onCreate");

        game_ = new Game(this, savedInstanceState);

        ClimbView climbView = new ClimbView(this, game_, game_.getInputHandler().getMotionEventBroker());
        climbView.getHolder().addCallback(game_.getInputHandler());
        climbView.getHolder().addCallback(game_.getRenderer());

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(climbView);
        game_.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.i(LOGTAG, "onCreateOptionsMenu");

        menu.add(0, MENU_START, 0, R.string.menu_start);
        menu.add(0, MENU_CANCEL, 0, R.string.menu_cancel);
        menu.add(0, MENU_PAUSE, 0, R.string.menu_pause);
        menu.add(0, MENU_RESUME, 0, R.string.menu_resume);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_START:
            Log.i(LOGTAG, "MENU_START");
            game_.start();
            return true;
        case MENU_CANCEL:
            Log.i(LOGTAG, "MENU_CANCEL");
            game_.cancel();
            return true;
        case MENU_PAUSE:
            Log.i(LOGTAG, "MENU_PAUSE");
            game_.pause(true);
            return true;
        case MENU_RESUME:
            Log.i(LOGTAG, "MENU_RESUME");
            game_.pause(false);
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOGTAG, "in onSIS");
        game_.save(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOGTAG, "in onPause");
        game_.pause(true);

    }

    public GameState.State getGameState() {
        return game_.getGameState().getState();
    }
}
