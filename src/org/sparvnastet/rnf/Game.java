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

// Represents a climbing game. Controls the games life time and owns the game components.
public class Game {
    private GameState gameState_;
    private IInputBroker inputBroker_;
    private IRenderer renderer_;
    private IPhysicsSimulator physicsSimulator_;
    private IGameThread gameThread_;

    public Game(Context context) {
        //Resources res = context.getResources();

        gameState_ = new GameState();
        inputBroker_ = new InputBroker();
        renderer_ = new Renderer();
        physicsSimulator_ = new PhysicsSimulator();

        gameThread_ = new GameThread(physicsSimulator_, renderer_, inputBroker_, gameState_);
    }

    public void start() {
        gameThread_.doStart();
    }

    public IInputBroker getInputBroker() {
        return inputBroker_;
    }

    public IRenderer getRenderer() {
        return renderer_;
    }

    // public Bundle saveState(Bundle map) {
    // synchronized (lock_) {
    // if (map != null) {
    //
    // }
    // }
    // return map;
    // }
    //
    // public synchronized void restoreState(Bundle savedState) {
    // synchronized (lock_) {
    // // setState(STATE_PAUSE);
    // }
    // }
}
