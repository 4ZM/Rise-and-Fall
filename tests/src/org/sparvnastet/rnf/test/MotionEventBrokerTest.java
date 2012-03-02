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

import org.sparvnastet.rnf.MotionEventBroker;

import android.test.AndroidTestCase;
import android.view.MotionEvent;

public class MotionEventBrokerTest extends AndroidTestCase {

    private MotionEventBroker broker_ = new MotionEventBroker();

    @Override
    protected void setUp() throws Exception {
    }

    public void testTakeEmpty() {
        assertNotNull(broker_.takeBundle());
        assertEquals(0, broker_.takeBundle().length);
    }

    public void testInvalidPut() {
        broker_.put(null);
        assertNotNull(broker_.takeBundle());
        assertEquals(0, broker_.takeBundle().length);
    }

    public void testOrdering() {
        MotionEvent me1 = createMotionEvent(1);
        MotionEvent me2 = createMotionEvent(2);
        MotionEvent me3 = createMotionEvent(3);
        broker_.put(me1);
        broker_.put(me2);
        broker_.put(me3);

        MotionEvent[] bundle = broker_.takeBundle();
        assertNotNull(bundle);
        assertEquals(3, bundle.length);
        assertEquals(me1, bundle[0]);
        assertEquals(me2, bundle[1]);
        assertEquals(me3, bundle[2]);

        bundle = broker_.takeBundle();
        assertNotNull(bundle);
        assertEquals(0, bundle.length);

        broker_.put(me1);
        bundle = broker_.takeBundle();
        assertNotNull(bundle);
        assertEquals(1, bundle.length);
        assertEquals(me1, bundle[0]);
    }

    private MotionEvent createMotionEvent(long downTime) {
        return MotionEvent.obtain(downTime, 0, MotionEvent.ACTION_DOWN, 0, 0, 0, 9, 0, 0, 0, 0, 0);
    }
}
