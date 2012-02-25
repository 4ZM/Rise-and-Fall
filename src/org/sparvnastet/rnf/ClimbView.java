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

class ClimbView extends SurfaceView implements SurfaceHolder.Callback {

    Context mContext;
    private IRenderer renderer_;
    private IInputBroker inputBroker_;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        inputBroker_.put(event);
        return false;
    }

    public ClimbView(Context context, IRenderer renderer, IInputBroker inputBroker) {
        super(context);

        renderer_ = renderer;

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // // create thread only; it's started in surfaceCreated()
        // mGameThread = new GameThread(this, holder, context, new Handler() {
        // @Override
        // public void handleMessage(Message m) {
        // ;
        // }
        // });

        setFocusable(true); // make sure we get key events
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        /*
         * if (!hasWindowFocus) mRenderingThread.setRendering(false);
         */

        // This should be a user input event!
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        renderer_.setSurface(holder);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        renderer_.setSurface(holder);
        renderer_.enableRendering(true);
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        renderer_.enableRendering(false);
    }
}