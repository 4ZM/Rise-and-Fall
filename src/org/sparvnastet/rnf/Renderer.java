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

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

// responsible for drawing the game state and user input 
interface IRenderer {
    void enableRendering(boolean enable);

    boolean isRendering();

    void setSurface(SurfaceHolder holder);

    void render(GameState gameState, MotionEvent[] userInput);
}

class Renderer implements IRenderer {
    private Object lock_ = new Object();
    private boolean isRendering_ = false;
    private SurfaceHolder surfaceHolder_ = null;

    @Override
    public void enableRendering(boolean enable) {
        synchronized (lock_) {
            isRendering_ = enable;
        }
    }

    @Override
    public boolean isRendering() {
        synchronized (lock_) {
            return isRendering_;
        }
    }

    @Override
    public void setSurface(SurfaceHolder holder) {
        synchronized (lock_) {
            surfaceHolder_ = holder;
            // size might have changed or it might be the first call
        }
    }

    @Override
    public void render(GameState gameState, MotionEvent[] userInput) {
        Canvas c = null;
        synchronized (lock_) {
            if (!isRendering_)
                return;

            try {
                c = surfaceHolder_.lockCanvas(null);
                draw(c);
            } finally {
                if (c != null)
                    surfaceHolder_.unlockCanvasAndPost(c);
            }
        }
    }

    private void draw(Canvas canvas) {
        // do some drawing
        canvas.restore();
    }

}