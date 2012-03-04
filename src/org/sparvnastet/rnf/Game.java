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

import org.sparvnastet.rnf.GameState.State;

import android.content.res.Resources;
import android.os.Bundle;

/**
 * The Game class represents one climbing game. It owns all the components of
 * the game and is supposed to be the single point of interaction with the game
 * that e.g. an Activity use.
 */
public class Game implements IGameCtrl {
    private GameState gameState_;
    private ClimbInputHandler inputHandler_;
    private ClimbRenderer renderer_;
    private PhysicsSimulator physicsSimulator_;
    private IGameRunner gameRunner_;

    public Game(Resources resources, Bundle savedState) {
        gameState_ = new GameState(savedState);
        inputHandler_ = new ClimbInputHandler();
        renderer_ = new ClimbRenderer(resources);
        physicsSimulator_ = new PhysicsSimulator();

        gameRunner_ = new GameRunner(physicsSimulator_, renderer_, inputHandler_);
    }

    public GameState getGameState() {
        return gameState_;
    }

    public ClimbInputHandler getInputHandler() {
        return inputHandler_;
    }

    public ClimbRenderer getRenderer() {
        return renderer_;
    }

    @Override
    public void start() {
        /*
         * TODO if (gameState_.getState() != State.READY) throw XX?
         */
        gameRunner_.start(gameState_);
        gameState_.setState(GameState.State.RUNNING);
    }

    @Override
    public void pause(boolean pause) {
        // TODO Test for invalid states

        GameState.State state = gameState_.getState();

        // Let ready state remain ready if paused
        if (pause && state == State.READY)
            return;

        if (pause) {
            gameRunner_.stop();
            gameState_.setState(State.PAUSED);
        } else {
            gameRunner_ = new GameRunner(physicsSimulator_, renderer_, inputHandler_);
            gameState_.setState(State.RUNNING);
            gameRunner_.start(gameState_);
        }
    }

    @Override
    public void cancel() {
        // TODO stop game runner!
        gameRunner_.stop();
        gameState_.setState(State.CANCELLED);
    }

    public void save(Bundle outState) {
        gameState_.save(outState);
    }
}
