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

public class GameRunner implements IGameRunner {
    private IRenderer renderer_;
    private IInputHandler inputHandler_;
    private IPhysicsSimulator physicsSimulator_;

    private AtomicBoolean running_ = new AtomicBoolean(false);
    private GameState gameState_;

    private Thread thread_;
    private long lastTime_;

    public GameRunner(IPhysicsSimulator physicsSimulator, IRenderer renderer, IInputHandler inputHandler) {
        physicsSimulator_ = physicsSimulator;
        renderer_ = renderer;
        inputHandler_ = inputHandler;
    }

    @Override
    public boolean start(GameState initialState) {
        gameState_ = initialState;
        lastTime_ = System.currentTimeMillis();

        running_.set(true);
        thread_ = new Thread(new GameLoop());
        thread_.start();
        return true;
    }

    @Override
    public boolean stop() {
        running_.set(false);

        try {
            thread_.join(500);
        } catch (InterruptedException e) {
            return false;
        }

        return true;

    }

    /**
     * A concrete game thread that handles input, advances the game state
     * through a physics simulation and renders the state to a renderer.
     * 
     * Once started and stopped, the same instance can not be started again. A
     * new instance is required to continue.
     */
    private class GameLoop implements Runnable {

        /**
         * Run the game loop. 1. Get input, 2. Do physics, 3. Render.
         * 
         * While this method is public, it should not be called directly. Use
         * the doStart/doStop to control execution.
         */
        @Override
        public void run() {
            while (running_.get()) {

                gameState_ = inputHandler_.handleInput(gameState_);

                // Check how much to advance the simulation
                long now = System.currentTimeMillis();
                float elapsed = (float) ((now - lastTime_) / 1000.0);
                lastTime_ = now;

                gameState_ = physicsSimulator_.run(elapsed, gameState_);

                renderer_.render(gameState_);
            }
        }
    }
}
