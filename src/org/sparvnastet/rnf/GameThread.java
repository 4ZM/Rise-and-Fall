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

import android.util.Log;
import android.view.MotionEvent;

interface IGameThread extends Runnable {

    public void doStart();

    public void doStop();

    public void pause();

    public void unpause();
}

// responsible for running the game loop and keeping track of time between calls

class GameThread extends Thread implements IGameThread {

    private Object lock_ = new Object(); // dont lock on this (or other public
                                         // types)
    private boolean running_ = false;
    private boolean paused_ = false;

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

    /**
     * Starts the game, setting parameters for the current difficulty.
     */
    @Override
    public void doStart() {
        synchronized (lock_) {
            lastTime_ = System.currentTimeMillis();
            running_ = true;
            start();
        }
    }

    @Override
    public void doStop() {
        synchronized (lock_) {
            running_ = false;
            paused_ = false;
            lock_.notify();
        }
    }

    /**
     * Pauses the physics update & animation.
     */
    @Override
    public void pause() {
        synchronized (lock_) {
            paused_ = true;
        }
    }

    @Override
    public void unpause() {
        // Move the real time clock up to now
        synchronized (lock_) {
            paused_ = false;
            lastTime_ = System.currentTimeMillis();
            lock_.notify();
        }
    }

    @Override
    public void run() {

        while (running_) {

            synchronized (lock_) {
                while (paused_ && running_) {
                    try {
                        lock_.wait();
                    } catch (InterruptedException e) {
                        Log.e("RnF", "InterruptedException waiting for paused lock!");
                        return; // Die on exception
                    }
                }
            }

            // 1. Get input
            // 2. Do physics
            // 3. Render

            MotionEvent[] motionEvents = inputBroker_.takeBundle();

            long now = System.currentTimeMillis();
            double elapsed = (now - lastTime_) / 1000.0; // Elapsed time in
                                                         // seconds
            gameState_ = physicsSimulator_.run(elapsed, gameState_, motionEvents);

            renderer_.render(gameState_, motionEvents);
        }
    }
}
