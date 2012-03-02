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

import junit.framework.Assert;

import org.sparvnastet.rnf.GameState;
import org.sparvnastet.rnf.PhysicsSimulator;

import android.test.AndroidTestCase;

public class PhysicsSimulatorTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    public void testTimeDiff() {
        PhysicsSimulator sim = new PhysicsSimulator();
        GameState gs = new GameState(null);

        // Step twice with allowed time diff
        sim.run(1, gs);
        sim.run(0, gs);

        // Don't allow negative time
        try {
            sim.run(-1, gs);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            Assert.fail();
        }

    }
}
