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

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Render (draw) the game state to a surface.
 */
interface IRenderer {

    /**
     * Enable or disable rendering. When disabled (once this method returns),
     * calls to render won't have any effect.
     * 
     * This function is used to prevent the view from being touched during
     * shutdown when the game thread might try to render as it runs it's last
     * iteration.
     * 
     * @param enable
     */
    void enableRendering(boolean enable);

    /**
     * @return true if rendering is enabled, i.e. if calls to render will have
     *         any effect.
     */
    boolean isEnabled();

    /**
     * Set the surface, through the holder proxy, that this renderer will draw
     * on.
     * 
     * @param holder
     */
    void setSurface(SurfaceHolder holder);

    /**
     * Draw the game state on the designated surface.
     * 
     * @param gameState
     */
    void render(GameState gameState);
}

abstract class Renderer implements IRenderer {
    private Object lock_ = new Object();
    private boolean enabled_ = false;
    private SurfaceHolder surfaceHolder_ = null;

    @Override
    public void enableRendering(boolean enable) {
        synchronized (lock_) {
            enabled_ = enable;
        }
    }

    @Override
    public boolean isEnabled() {
        synchronized (lock_) {
            return enabled_;
        }
    }

    @Override
    public void setSurface(SurfaceHolder holder) {
        synchronized (lock_) {
            surfaceHolder_ = holder;
        }
    }

    /**
     * If the enabled flag is set, get a lock to draw to the canvas, save its
     * state and call the IoC method draw. When after drawing, restore the
     * canvas and release the lock.
     */
    @Override
    public void render(GameState gameState) {
        Canvas c = null;
        synchronized (lock_) {
            if (!enabled_)
                return;

            try {
                c = surfaceHolder_.lockCanvas(null);
                if (c == null)
                    return;

                c.save();
                draw(c, gameState);
                c.restore();

            } finally {
                if (c != null)
                    surfaceHolder_.unlockCanvasAndPost(c);
            }
        }
    }

    /**
     * This is implemented in specific renderer classes to provide the domain
     * specific rendering of a GameState.
     * 
     * @param canvas
     * @param gameState
     */
    protected abstract void draw(Canvas canvas, GameState gameState);

}

/**
 * Render the climbing game state.
 */
class ClimbingRenderer extends Renderer {
    private Resources resources_;

    public ClimbingRenderer(Resources resources) {
        resources_ = resources;
    }

    @Override
    protected void draw(Canvas canvas, GameState gameState) {
        canvas.drawColor(Color.BLACK);

        Paint circlePaint = new Paint();

        Point p = gameState.getPos();

        if (gameState.isMoving()) {
            circlePaint.setARGB(255, 200, 40, 40);
            canvas.drawCircle(p.x, p.y, 40, circlePaint);
            circlePaint.setARGB(255, 0, 0, 0);
            canvas.drawCircle(p.x, p.y, 36, circlePaint);
        }

        circlePaint.setARGB(255, 200, 40, 40);
        canvas.drawCircle(p.x, p.y, 20, circlePaint);
        circlePaint.setARGB(255, 0, 0, 0);
        canvas.drawCircle(p.x, p.y, 16, circlePaint);
    }
}
