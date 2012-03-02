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

import org.sparvnastet.rnf.GameState;
import org.sparvnastet.rnf.IMotionEventBroker;
import org.sparvnastet.rnf.test.helpers.TestInputHandler;
import org.sparvnastet.rnf.test.helpers.TestSurfaceHolder;

import android.test.AndroidTestCase;
import android.view.MotionEvent;

public class InputHandlerTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    public void testInputHandlerSurfaceSync() {

        TestInputHandler handler = new TestInputHandler();
        GameState state = new GameState(null);

        TestSurfaceHolder sh = new TestSurfaceHolder();
        sh.addCallback(handler);

        // Don't process inputs before surface is created
        handler.processFlag_ = false;
        handler.handleInput(state);
        assertFalse(handler.processFlag_);

        // Create surface and handle input once surface is created
        sh.announceCreated();
        handler.processFlag_ = false;
        handler.handleInput(state);
        assertTrue(handler.processFlag_);

        // Still processing after surface is changed
        sh.announceChanged();
        handler.processFlag_ = false;
        handler.handleInput(state);
        assertTrue(handler.processFlag_);

        // Don't process after surface is destroyed
        sh.annouceDestroyed();
        handler.processFlag_ = false;
        handler.handleInput(state);
        assertFalse(handler.processFlag_);
    }

    public void testInputHandlerGetsInputs() {
        TestInputHandler handler = new TestInputHandler();
        GameState state = new GameState(null);

        // Create the test surface and register input handler as listener.
        TestSurfaceHolder sh = new TestSurfaceHolder();
        sh.addCallback(handler);

        // Put a single event in the pipe
        IMotionEventBroker broker = handler.getMotionEventBroker();
        assertNotNull(broker);
        broker.put(MotionEvent.obtain(1, 0, MotionEvent.ACTION_DOWN, 0, 0, 0, 9, 0, 0, 0, 0, 0));

        // Don't process inputs before surface is created
        handler.reset();
        handler.handleInput(state);
        assertFalse(handler.processFlag_);
        assertNull(handler.events_);

        // Create surface and handle input once surface is created
        // should now get the original input.
        sh.announceCreated();
        handler.reset();
        handler.handleInput(state);
        assertTrue(handler.processFlag_);
        assertNotNull(handler.events_);
        assertEquals(1, handler.events_.length);

        // Process two inputs at once
        broker.put(MotionEvent.obtain(1, 0, MotionEvent.ACTION_DOWN, 0, 0, 0, 9, 0, 0, 0, 0, 0));
        broker.put(MotionEvent.obtain(1, 0, MotionEvent.ACTION_DOWN, 0, 0, 0, 9, 0, 0, 0, 0, 0));
        handler.reset();
        handler.handleInput(state);
        assertTrue(handler.processFlag_);
        assertNotNull(handler.events_);
        assertEquals(2, handler.events_.length);

        // Don't process after surface is destroyed
        sh.annouceDestroyed();
        handler.reset();
        handler.handleInput(state);
        assertFalse(handler.processFlag_);
    }
}
