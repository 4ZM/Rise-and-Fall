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

import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Implement rendering of the game state to a SurfaceView. This class is
 * responsible for determining when a rendering request should actually result
 * in the surface being drawn. It also provides a transformation between game
 * and screen coordinates.
 */
public abstract class InputHandler implements IInputHandler, SurfaceHolder.Callback {

    // TODO add transform

    private Object lock_ = new Object();
    private SurfaceHolder surfaceHolder_ = null;
    private MotionEventBroker motionEventBroker_ = new MotionEventBroker();
    private int surfWidth_;
    private int surfHeight_;

    @Override
    public IMotionEventBroker getMotionEventBroker() {
        return motionEventBroker_;
    }

    @Override
    public GameState handleInput(GameState gameState) {
        synchronized (lock_) {
            if (surfaceHolder_ != null && surfHeight_ != 0.0f && surfWidth_ != 0.0f) {
                process(gameState, motionEventBroker_.takeBundle());
            }
            return gameState;
        }
    }

    protected Vec2 toWorldCoords(GameState gs, Vec2 p) {
        Vec2 ws = gs.getWindowSize();
        float xs = ws.x / surfWidth_;
        float ys = -ws.y / surfHeight_;
        float xo = -ws.x / 2.0f;
        float yo = ws.y / 2.0f;

        return gs.windowToWorld(new Vec2(p.x * xs + xo, p.y * ys + yo));
    }

    protected abstract void process(GameState gameState, MotionEvent[] events);

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
        // TODO Set transform here
        synchronized (lock_) {
            surfaceHolder_ = holder;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (lock_) {
            surfaceHolder_ = null;
            surfHeight_ = 0;
            surfWidth_ = 0;
        }
    }
}
