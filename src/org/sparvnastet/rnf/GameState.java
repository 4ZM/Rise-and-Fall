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

import android.graphics.Point;
import android.os.Bundle;

/**
 * The GameState class represents the momentary state of a game. It contains all
 * information required to fully represent the game logic. It does not contain
 * visualization state.
 */
public class GameState {

    public enum State {
        READY, RUNNING, PAUSED, CANCELLED, WON, LOST,
    }

    private State state_;

    private Point pos_;
    private boolean isMoving_;

    /**
     * Create a new default state or restore a saved state.
     * 
     * @param savedState
     *            optional saved state, or null to create a new default state.
     */
    public GameState(Bundle savedState) {
        if (savedState == null) {
            initialize();
        } else {
            restore(savedState);
        }
    }

    /**
     * Create a new default state
     */
    private void initialize() {
        state_ = State.READY;
        pos_ = new Point();
        isMoving_ = false;
    }

    /**
     * Save the game state in the Bundle. It can later be restored by passing
     * the Bundle to the constructor of a new GameState.
     * 
     * @param outState
     */
    public void save(Bundle outState) {
        outState.putSerializable("GameState::state_", state_);
        outState.putInt("GameState::pos_.x", pos_.x);
        outState.putInt("GameState::pos_.y", pos_.y);
        outState.putBoolean("GameState::isMoving_", isMoving_);
    }

    /**
     * Populate this instance with the state passed as a parameter.
     * 
     * @param savedState
     */
    private void restore(Bundle savedState) {
        state_ = (State) savedState.getSerializable("GameState::state_");
        pos_ = new Point(savedState.getInt("GameState::pos_.x"), savedState.getInt("GameState::pos_.y"));
        isMoving_ = savedState.getBoolean("GameState::isMoving_");
    }

    /**
     * Change the playing state (paused, won, etc.).
     * 
     * @param state
     * @return true if state was changed, false on invalid transition request
     */
    public boolean setState(State state) {
        // Assert valid state transitions
        if ((state_ == State.READY && state != State.RUNNING && state != State.CANCELLED)
                || (state_ == State.RUNNING && state != State.PAUSED && state != State.CANCELLED && state != State.WON && state != State.LOST)
                || (state_ == State.PAUSED && state != State.RUNNING && state != State.CANCELLED)
                || (state_ == State.CANCELLED || state_ == State.LOST || state_ == State.WON))
            return false;

        state_ = state;
        return true;
    }

    /**
     * 
     * @return The playing state of the GameState
     */
    public State getState() {
        return state_;
    }

    public void startMove() {
        isMoving_ = true;
    }

    public void stopMove() {
        isMoving_ = false;
    }

    public boolean isMoving() {
        return isMoving_;
    }

    public void setPos(int x, int y) {
        pos_.x = x;
        pos_.y = y;
    }

    public Point getPos() {
        return pos_;
    }
}
