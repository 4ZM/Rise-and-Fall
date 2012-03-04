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

import org.sparvnastet.rnf.Game;
import org.sparvnastet.rnf.GameState;

import android.test.AndroidTestCase;
import android.test.mock.MockResources;

public class GameTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {

    }

    public void testStartStop() {
        Game g = new Game(new GameResouces(), null);
        assertEquals(GameState.State.READY, g.getGameState().getState());
        g.start();
        assertEquals(GameState.State.RUNNING, g.getGameState().getState());
        g.pause(true);
        assertEquals(GameState.State.PAUSED, g.getGameState().getState());
        g.pause(false);
        assertEquals(GameState.State.RUNNING, g.getGameState().getState());
        g.cancel();
        assertEquals(GameState.State.CANCELLED, g.getGameState().getState());
    }

    class GameResouces extends MockResources {
    }
}
