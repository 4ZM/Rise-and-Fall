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

import org.jbox2d.common.Vec2;
import org.sparvnastet.rnf.GameState;
import org.sparvnastet.rnf.GameState.State;

import android.test.AndroidTestCase;

public class GameStateTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {

    }

    public void testInitiallyReady() {
        GameState gs = new GameState(null);
        assertEquals(State.READY, gs.getState());
    }

    public void testGameModeTransitions() {
        GameState gs;

        gs = new GameState(null);
        assertFalse(gs.setState(State.READY));
        assertFalse(gs.setState(State.PAUSED));
        assertFalse(gs.setState(State.LOST));
        assertFalse(gs.setState(State.WON));
        assertTrue(gs.setState(State.RUNNING));
        assertEquals(State.RUNNING, gs.getState());

        gs = new GameState(null);
        assertTrue(gs.setState(State.CANCELLED));
        assertEquals(State.CANCELLED, gs.getState());
        assertFalse(gs.setState(State.CANCELLED));
        assertFalse(gs.setState(State.READY));
        assertFalse(gs.setState(State.RUNNING));
        assertFalse(gs.setState(State.PAUSED));
        assertFalse(gs.setState(State.LOST));
        assertFalse(gs.setState(State.WON));

        gs = new GameState(null);
        assertTrue(gs.setState(State.RUNNING));
        assertTrue(gs.setState(State.PAUSED));
        assertEquals(State.PAUSED, gs.getState());
        assertTrue(gs.setState(State.RUNNING));
        assertEquals(State.RUNNING, gs.getState());
        assertTrue(gs.setState(State.PAUSED));
        assertEquals(State.PAUSED, gs.getState());

        gs = new GameState(null);
        assertTrue(gs.setState(State.RUNNING));
        assertTrue(gs.setState(State.WON));
        assertEquals(State.WON, gs.getState());

        gs = new GameState(null);
        assertTrue(gs.setState(State.RUNNING));
        assertTrue(gs.setState(State.LOST));
        assertEquals(State.LOST, gs.getState());
    }

    public void testWorldWindow() {
        GameState gs = new GameState(null);

        gs.setWorldSize(new Vec2(10.0f, 10.0f));
        gs.setWindowPos(new Vec2(2.5f, -2.5f));
        gs.setWindowSize(new Vec2(3.0f, 3.0f));

        Vec2 winToWorld = gs.windowToWorld(new Vec2(0.0f, 0.0f));
        assertEquals(2.5f, winToWorld.x);
        assertEquals(-2.5f, winToWorld.y);

        Vec2 worldToWin = gs.worldToWindow(new Vec2(0.0f, 0.0f));
        assertEquals(-2.5f, worldToWin.x);
        assertEquals(2.5f, worldToWin.y);
    }
}
