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

import org.sparvnastet.rnf.GameRunner;
import org.sparvnastet.rnf.GameState;
import org.sparvnastet.rnf.test.helpers.TestInputHandler;
import org.sparvnastet.rnf.test.helpers.TestPhysicsSimulator;
import org.sparvnastet.rnf.test.helpers.TestRenderer;
import org.sparvnastet.rnf.test.helpers.TestSurfaceHolder;

import android.test.AndroidTestCase;

public class GameRunnerTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    public void testAllStagesAreCalled() {
        TestPhysicsSimulator sim = new TestPhysicsSimulator();
        TestRenderer renderer = new TestRenderer();
        TestInputHandler handler = new TestInputHandler();

        GameState gs = new GameState(null);

        // Set up the surface (else the handler and renderer won't process
        TestSurfaceHolder sh = new TestSurfaceHolder();
        sh.addCallback(renderer);
        sh.addCallback(handler);
        sh.announceCreated();

        GameRunner runner = new GameRunner(sim, renderer, handler);

        // Not run right away
        assertFalse(handler.processFlag_);
        assertFalse(renderer.drawFlag_);
        assertFalse(sim.runFlag_);

        // Start the runner
        boolean startRes = runner.start(gs);
        assertTrue(startRes);

        sleep(10); // Give the thread a chance to run

        // Wait for each of the flags to get set..
        long timeout = 100;
        long startTime = System.currentTimeMillis();
        while (!handler.processFlag_ && System.currentTimeMillis() - startTime < timeout)
            ;
        assertTrue(System.currentTimeMillis() - startTime < timeout);

        while (!sim.runFlag_ && System.currentTimeMillis() - startTime < timeout)
            ;
        assertTrue(System.currentTimeMillis() - startTime < timeout);

        while (!renderer.drawFlag_ && System.currentTimeMillis() - startTime < timeout)
            ;
        assertTrue(System.currentTimeMillis() - startTime < timeout);

        // Stop and make sure we don't get any more calls
        boolean stopRes = runner.stop();
        assertTrue(stopRes);

        handler.reset();
        renderer.drawFlag_ = false;
        sim.runFlag_ = false;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}
