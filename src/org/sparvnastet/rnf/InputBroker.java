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

import java.util.ArrayList;

import android.view.MotionEvent;

// Responsible for aggregating user input until it can be handled as a batch 
interface IInputBroker {
    void push(MotionEvent motionEvent);

    MotionEvent[] takeBundle(); // <- Output event type should be customized?!
}

class InputBroker implements IInputBroker {

    private ArrayList<MotionEvent> motionEvents_ = new ArrayList<MotionEvent>();

    @Override
    public void push(MotionEvent motionEvent) {
        synchronized (motionEvents_) {
            motionEvents_.add(motionEvent);
        }
    }

    @Override
    public MotionEvent[] takeBundle() {
        synchronized (motionEvents_) {
            MotionEvent[] inputBundle = new MotionEvent[motionEvents_.size()];
            inputBundle = motionEvents_.toArray(inputBundle);
            motionEvents_.clear();
            return inputBundle;
        }
    }
}