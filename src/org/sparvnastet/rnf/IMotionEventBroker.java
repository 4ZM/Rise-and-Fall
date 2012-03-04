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

import android.view.MotionEvent;

/**
 * The input broker is a thread safe, aggregating proxy of input events that is
 * pushed from the view to some other, non UI, thread.
 */
public interface IMotionEventBroker {

    /**
     * Add a new event to the brokers queue.
     * 
     * @param motionEvent
     */
    void put(MotionEvent motionEvent);

    /**
     * Get all enqueued events and remove them from the broker. The events are
     * ordered in the same way as they were pushed into the broker, the oldest
     * event first and the latest event last.
     * 
     * @return
     */
    MotionEvent[] takeBundle();
}