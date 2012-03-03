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

/**
 *
 */
public class ClimbInputHandler extends InputHandler {

    @Override
    public void process(GameState gameState, MotionEvent[] events) {

        for (MotionEvent event : events) {

            Vec2 p = toWorldCoords(gameState, new Vec2(event.getX(), event.getY()));

            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float dx = Math.abs(gameState.getPos().x - p.x);
                float dy = Math.abs(gameState.getPos().y - p.y);
                if (dx < 1.0f && dy < 1.0f) {
                    gameState.startMove();
                    gameState.setPos(p);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (gameState.isMoving())
                    gameState.setPos(p);
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (gameState.isMoving()) {
                    gameState.setPos(p);
                    gameState.stopMove();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (gameState.isMoving()) {
                    gameState.setPos(p);
                    gameState.stopMove();
                }
                break;
            }
            }
        }
    }
}
