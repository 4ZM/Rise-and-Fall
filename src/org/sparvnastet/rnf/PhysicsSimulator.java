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
 * This interface represents a simulation that transform the GameState according
 * to the laws of physics. The simulation is advanced one time step at the time.
 */
interface IPhysicsSimulator {

    /*
     * Transform the currentState and given inputs to a new state. The
     * currentState represents the world as it was known elapsedTime seconds
     * ago.
     */
    GameState run(float elapsedTime, GameState currentState, MotionEvent[] userInput);
}

class PhysicsSimulator implements IPhysicsSimulator {

    @Override
    public GameState run(float elapsedTime, GameState currentState, MotionEvent[] userInput) {
        if (elapsedTime > 0)
            currentState.setFps(1.0f / elapsedTime);
        else
            currentState.setFps(0);

        for (MotionEvent event : userInput) {

            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int dx = (int) Math.abs(currentState.getPos().x - event.getX());
                int dy = (int) Math.abs(currentState.getPos().y - event.getY());
                if (dx < 40 && dy < 40) {
                    currentState.startMove();
                    currentState.setPos((int) event.getX(), (int) event.getY());
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (currentState.isMoving())
                    currentState.setPos((int) event.getX(), (int) event.getY());
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (currentState.isMoving()) {
                    currentState.setPos((int) event.getX(), (int) event.getY());
                    currentState.stopMove();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (currentState.isMoving()) {
                    currentState.setPos((int) event.getX(), (int) event.getY());
                    currentState.stopMove();
                }
                break;
            }
            }
        }
        return currentState;
    }

}
