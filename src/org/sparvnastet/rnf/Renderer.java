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

import org.jbox2d.common.Vec2;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Implement rendering of the game state to a SurfaceView. This class is
 * responsible for determining when a rendering request should actually result
 * in the surface being drawn. It also provides a transformation between game
 * and screen coordinates.
 */
public abstract class Renderer implements IRenderer, SurfaceHolder.Callback {

    private Object lock_ = new Object();
    private SurfaceHolder surfaceHolder_ = null;
    private int surfWidth_;
    private int surfHeight_;

    /**
     * If the surface is ready for drawing, lock the canvas, save its state and
     * call the IoC method draw. After drawing, restore the canvas and release
     * the lock.
     */
    @Override
    public void render(GameState gameState) {
        Canvas c = null;
        synchronized (lock_) {
            if (surfaceHolder_ == null)
                return;

            // Set up the transform game window -> screen
            Vec2 ws = gameState.getWindowSize();
            if (ws.x == 0 || ws.y == 0)
                return;

            try {
                c = surfaceHolder_.lockCanvas();
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

    protected Vec2 screenScale(GameState gs) {
        Vec2 ws = gs.getWindowSize();
        return new Vec2((float) surfWidth_ / ws.x, -(float) surfHeight_ / ws.y);
    }

    protected Vec2 toScreenCoords(GameState gs, Vec2 p) {
        Vec2 ws = gs.getWindowSize();
        float xs = (float) surfWidth_ / ws.x;
        float ys = -(float) surfHeight_ / ws.y;
        float xo = surfWidth_ / 2.0f;
        float yo = surfHeight_ / 2.0f;

        p = gs.worldToWindow(p);
        return new Vec2(p.x * xs + xo, p.y * ys + yo);
    }

    /**
     * This is implemented in specific renderer classes to provide the domain
     * specific rendering of a GameState.
     * 
     * @param canvas
     * @param gameState
     */
    protected abstract void draw(Canvas canvas, GameState gameState);

    // SurfaceHolder.Callback interface implementation

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        synchronized (lock_) {
            surfaceHolder_ = holder;
            surfWidth_ = width;
            surfHeight_ = height;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (lock_) {
            surfaceHolder_ = holder;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (lock_) {
            surfaceHolder_ = null;
            surfWidth_ = 0;
            surfHeight_ = 0;
        }
    }
}
