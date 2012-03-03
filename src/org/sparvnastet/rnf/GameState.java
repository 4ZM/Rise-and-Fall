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

import org.jbox2d.common.Vec2;

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

    private Vec2 pos_;
    private boolean isMoving_;

    private float fps_;

    private Vec2 worldSize_;
    private Vec2 windowSize_;
    private Vec2 windowPos_;

    // Transient data (derived)

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
        pos_ = new Vec2(0.0f, 0.0f);
        isMoving_ = false;
        worldSize_ = new Vec2(20.0f, 20.0f);
        windowSize_ = new Vec2(20.0f, 20.0f);
        windowPos_ = new Vec2(0.0f, 0.0f);
    }

    /**
     * Save the game state in the Bundle. It can later be restored by passing
     * the Bundle to the constructor of a new GameState.
     * 
     * @param outState
     */
    public void save(Bundle outState) {
        outState.putSerializable("GameState::state_", state_);
        outState.putFloat("GameState::pos_.x", pos_.x);
        outState.putFloat("GameState::pos_.y", pos_.y);
        outState.putFloat("GameState::worldSize_.x", worldSize_.x);
        outState.putFloat("GameState::worldSize_.y", worldSize_.y);
        outState.putFloat("GameState::windowSize_.x", windowSize_.x);
        outState.putFloat("GameState::windowSize_.y", windowSize_.y);
        outState.putFloat("GameState::windowPos_.x", windowPos_.x);
        outState.putFloat("GameState::windowPos_.y", windowPos_.y);
        outState.putBoolean("GameState::isMoving_", isMoving_);
    }

    /**
     * Populate this instance with the state passed as a parameter.
     * 
     * @param savedState
     */
    private void restore(Bundle savedState) {
        state_ = (State) savedState.getSerializable("GameState::state_");
        pos_ = new Vec2(savedState.getFloat("GameState::pos_.x"), savedState.getFloat("GameState::pos_.y"));
        worldSize_ = new Vec2(savedState.getFloat("GameState::worldSize_.x"),
                savedState.getFloat("GameState::worldSize_.y"));
        windowSize_ = new Vec2(savedState.getFloat("GameState::windowSize_.x"),
                savedState.getFloat("GameState::windowSize_.y"));
        windowPos_ = new Vec2(savedState.getFloat("GameState::windowPos_.x"),
                savedState.getFloat("GameState::windowPos_.y"));

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

    public void setPos(Vec2 pos) {
        setVec2(pos_, pos);
    }

    public Vec2 getPos() {
        return pos_;
    }

    public float getFps() {
        return fps_;
    }

    public void setFps(float fps) {
        fps_ = fps;
    }

    public Vec2 getWorldSize() {
        return worldSize_;
    }

    public void setWorldSize(Vec2 worldSize) {
        setVec2(worldSize_, worldSize);
    }

    public Vec2 getWindowSize() {
        return worldSize_;
    }

    public void setWindowSize(Vec2 windowSize) {
        setVec2(windowSize_, windowSize);
    }

    public Vec2 getWindowPos() {
        return windowPos_;
    }

    public void setWindowPos(Vec2 windowPos) {
        setVec2(windowPos_, windowPos);
    }

    public Vec2 worldToWindow(Vec2 p) {
        return new Vec2(p.x - windowPos_.x, p.y - windowPos_.y);
    }

    public Vec2 windowToWorld(Vec2 p) {
        return new Vec2(windowPos_.x + p.x, windowPos_.y + p.y);
    }

    private void setVec2(Vec2 dst, Vec2 src) {
        dst.x = src.x;
        dst.y = src.y;
    }
}
