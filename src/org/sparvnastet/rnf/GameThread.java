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

import java.util.concurrent.atomic.AtomicBoolean;

import android.view.MotionEvent;

interface IGameThread extends Runnable {

    public void doStart() throws Exception;

    public void doStop();
}

// responsible for running the game loop and keeping track of time between calls

class GameThread extends Thread implements IGameThread {

    private AtomicBoolean running_ = new AtomicBoolean(false);

    private IRenderer renderer_;
    private IInputBroker inputBroker_;
    private IPhysicsSimulator physicsSimulator_;
    private GameState gameState_;

    private long lastTime_;

    public GameThread(IPhysicsSimulator physicsSimulator, IRenderer renderer, IInputBroker inputBroker,
            GameState gameState) {

        physicsSimulator_ = physicsSimulator;
        renderer_ = renderer;
        inputBroker_ = inputBroker;
        gameState_ = gameState;
    }

    @Override
    public void doStart() {
        running_.set(true);
        lastTime_ = System.currentTimeMillis();
        start();
    }

    @Override
    public void doStop() {
        running_.set(false);
    }

    @Override
    public void run() {
        while (running_.get()) {

            // 1. Get input
            // 2. Do physics
            // 3. Render

            MotionEvent[] motionEvents = inputBroker_.takeBundle();

            // Check how much to advance the simulation
            long now = System.currentTimeMillis();
            double elapsed = (now - lastTime_) / 1000.0;
            gameState_ = physicsSimulator_.run(elapsed, gameState_, motionEvents);

            renderer_.render(gameState_, motionEvents);
        }
    }
}
