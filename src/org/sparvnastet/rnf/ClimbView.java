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

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The surface view to draw the climbing game on.
 */
class ClimbView extends SurfaceView implements SurfaceHolder.Callback {

    private IGameCtrl game_;
    private IMotionEventBroker inputBroker_;

    public ClimbView(Context context, IGameCtrl game, IMotionEventBroker inputBroker) {
        super(context);

        game_ = game;
        inputBroker_ = inputBroker;

        // Listen to changes in the surface
        getHolder().addCallback(this);

        // The surface should receive key events
        setFocusable(true);
    }

    /**
     * Move touch events to the input broker and signal that they have been
     * handled.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        inputBroker_.put(event);
        return true;
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus)
            game_.pause(true);
    }

    /**
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
    }

    /**
     * Callback invoked when the surface dimensions change.
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched.
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}