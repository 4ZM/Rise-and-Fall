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
 *
 */
public class ClimbInputHandler extends InputHandler {

    @Override
    public void process(GameState gameState, MotionEvent[] events) {

        // TODO Use transform

        for (MotionEvent event : events) {

            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int dx = (int) Math.abs(gameState.getPos().x - event.getX());
                int dy = (int) Math.abs(gameState.getPos().y - event.getY());
                if (dx < 40 && dy < 40) {
                    gameState.startMove();
                    gameState.setPos((int) event.getX(), (int) event.getY());
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (gameState.isMoving())
                    gameState.setPos((int) event.getX(), (int) event.getY());
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (gameState.isMoving()) {
                    gameState.setPos((int) event.getX(), (int) event.getY());
                    gameState.stopMove();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (gameState.isMoving()) {
                    gameState.setPos((int) event.getX(), (int) event.getY());
                    gameState.stopMove();
                }
                break;
            }
            }
        }

    }

}
