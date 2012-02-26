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

package org.sparvnastet.rnf.test;

import org.sparvnastet.rnf.ClimbingActivity;
import org.sparvnastet.rnf.GameState;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

public class ClimbingActivityTest extends ActivityInstrumentationTestCase2<ClimbingActivity> {

    ClimbingActivity activity_;

    public ClimbingActivityTest() {
        super("org.sparvnastet.rnf.ClimbingActivityTest", ClimbingActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);

        activity_ = getActivity();
    }

    public void testStateDestroy() {
        assertEquals(GameState.State.READY, activity_.getGameState());

        activity_.finish();
        activity_ = getActivity();

        assertEquals(GameState.State.READY, activity_.getGameState());
    }

    public void testStatePause() {
        Instrumentation instrumentation = this.getInstrumentation();

        assertEquals(GameState.State.READY, activity_.getGameState());
        instrumentation.callActivityOnPause(activity_);
        instrumentation.callActivityOnResume(activity_);
        assertEquals(GameState.State.READY, activity_.getGameState());
    }

}
