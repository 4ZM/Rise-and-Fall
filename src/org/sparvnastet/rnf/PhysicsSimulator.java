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

public class PhysicsSimulator implements IPhysicsSimulator {

    @Override
    public GameState run(float dt, GameState currentState) {
        if (dt < 0)
            throw new IllegalArgumentException();

        currentState.world_.step(1.0f/50.0f, 8, 3);

        // Update FPS
        if (dt > 0)
            currentState.setFps(1.0f / dt);
        else
            currentState.setFps(0);

        return currentState;
    }

}
