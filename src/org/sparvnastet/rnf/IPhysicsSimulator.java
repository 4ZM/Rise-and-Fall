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

/**
 * This interface represents a simulation that transform the GameState according
 * to the laws of physics. The simulation is advanced one time step at the time.
 */
public interface IPhysicsSimulator {

    /*
     * Transform the currentState and given inputs to a new state. The
     * currentState represents the world as it was known elapsedTime seconds
     * ago.
     */
    GameState run(float dt, GameState gameState);
}