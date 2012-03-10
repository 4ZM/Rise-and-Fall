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

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

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

    private boolean isMoving_;

    private float fps_;

    private Vec2 worldSize_;
    private Vec2 windowSize_;
    private Vec2 windowPos_;

    public World world_;
    private Body ground_;
    private Body dot_;
    private MouseJoint mj_;
    private MouseJointDef mjd_;

    private Body torso_;
    private Body leftThigh_;
    private Body leftLeg_;
    private Body rightThigh_;
    private Body rightLeg_;

    /**
     * Create a new default state or restore a saved state.
     * 
     * @param savedState
     *            optional saved state, or null to create a new default state.
     */
    public GameState(Bundle savedState) {
        isMoving_ = false;
        worldSize_ = new Vec2(5.0f, 8.0f);
        Vec2 gravity = new Vec2(0, -9.8f);
        world_ = new World(gravity, true);

        ground_ = createGround();

        mjd_ = new MouseJointDef();
        mjd_.bodyA = ground_;
        mjd_.target.setZero();
        mjd_.maxForce = 3000.0f;

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
        isMoving_ = false;
        windowSize_ = new Vec2(worldSize_.x, worldSize_.y);
        windowPos_ = new Vec2(0.0f, 0.0f);

        // Create the dot circle
        dot_ = createDot(new Vec2(0, 3.0f));
        createClimber();
    }

    private Body createDot(Vec2 pos) {
        CircleShape shape = new CircleShape();
        shape.m_radius = 0.3f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(pos);
        Body dot = world_.createBody(bd);
        dot.setBullet(true);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.5f;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = 0x1;
        fd.filter.maskBits = 0x3;

        dot.createFixture(fd);

        return dot;
    }

    private void createClimber() {

        // Create the limbs
        Vec2 torsoDim = new Vec2(0.45f, 0.55f);
        Vec2 torsoPos = new Vec2(0, 0);
        torso_ = createLimb(torsoPos, torsoDim);

        Vec2 thighDim = new Vec2(0.20f, 0.45f);
        Vec2 leftThighPos = new Vec2(torsoPos.x - torsoDim.x / 4.0f, torsoPos.y - torsoDim.y / 2.0f - thighDim.y / 2.0f);
        leftThigh_ = createLimb(leftThighPos, thighDim);

        Vec2 legDim = new Vec2(0.15f, 0.50f);
        Vec2 leftLegPos = new Vec2(leftThighPos.x, leftThighPos.y - thighDim.y / 2.0f - legDim.y / 2.0f);
        leftLeg_ = createLimb(leftLegPos, legDim);

        Vec2 rightThighPos = new Vec2(torsoPos.x + torsoDim.x / 4.0f, torsoPos.y - torsoDim.y / 2.0f - thighDim.y
                / 2.0f);
        rightThigh_ = createLimb(rightThighPos, thighDim);

        Vec2 rightLegPos = new Vec2(rightThighPos.x, rightThighPos.y - thighDim.y / 2.0f - legDim.y / 2.0f);
        rightLeg_ = createLimb(rightLegPos, legDim);

        // Joints
        Vec2 torsoLeftThighAnchor = new Vec2(leftThighPos.x, leftThighPos.y + thighDim.y / 2.0f);
        createJoint(torso_, leftThigh_, torsoLeftThighAnchor);

        Vec2 leftThighLegAnchor = new Vec2(leftThighPos.x, leftThighPos.y - thighDim.y / 2.0f);
        createJoint(leftThigh_, leftLeg_, leftThighLegAnchor);

        Vec2 torsoRightThighAnchor = new Vec2(rightThighPos.x, rightThighPos.y + thighDim.y / 2.0f);
        createJoint(torso_, rightThigh_, torsoRightThighAnchor);

        Vec2 rightThighLegAnchor = new Vec2(rightThighPos.x, rightThighPos.y - thighDim.y / 2.0f);
        createJoint(rightThigh_, rightLeg_, rightThighLegAnchor);
    }

    private Joint createJoint(Body bodyA, Body bodyB, Vec2 anchor) {
        RevoluteJointDef jdef = new RevoluteJointDef();
        jdef.initialize(bodyA, bodyB, anchor);
        return world_.createJoint(jdef);
    }

    private Body createLimb(Vec2 pos, Vec2 dim) {
        PolygonShape leftLegShape = new PolygonShape();
        leftLegShape.setAsBox(dim.x / 2.0f, dim.y / 2.0f);

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position = pos;
        Body body = world_.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.shape = leftLegShape;
        fd.density = 1.0f;
        /*
         * fd.friction = 0.5f; fd.restitution = 0.5f;
         */
        fd.filter.categoryBits = 0x2;
        fd.filter.maskBits = 0x1;

        body.createFixture(fd);
        return body;
    }

    private Body createGround() {
        BodyDef bd = new BodyDef();
        Body groundBody = world_.createBody(bd);

        PolygonShape shape;

        shape = new PolygonShape();
        shape.setAsEdge(new Vec2(-worldSize_.x / 2.0f, -worldSize_.y / 2.0f), new Vec2(worldSize_.x / 2.0f,
                -worldSize_.y / 2.0f));
        groundBody.createFixture(shape, 0.0f);

        shape = new PolygonShape();
        shape.setAsEdge(new Vec2(-worldSize_.x / 2.0f, worldSize_.y / 2.0f), new Vec2(worldSize_.x / 2.0f,
                worldSize_.y / 2.0f));
        groundBody.createFixture(shape, 0.0f);

        shape = new PolygonShape();
        shape.setAsEdge(new Vec2(-worldSize_.x / 2.0f, -worldSize_.y / 2.0f), new Vec2(-worldSize_.x / 2.0f,
                worldSize_.y / 2.0f));
        groundBody.createFixture(shape, 0.0f);

        shape = new PolygonShape();
        shape.setAsEdge(new Vec2(worldSize_.x / 2.0f, -worldSize_.y / 2.0f), new Vec2(worldSize_.x / 2.0f,
                worldSize_.y / 2.0f));
        groundBody.createFixture(shape, 0.0f);
        return groundBody;
    }

    /**
     * Save the game state in the Bundle. It can later be restored by passing
     * the Bundle to the constructor of a new GameState.
     * 
     * @param outState
     */
    public void save(Bundle outState) {
        outState.putSerializable("GameState::state_", state_);

        outState.putFloat("GameState::pos_.x", dot_.m_xf.position.x);
        outState.putFloat("GameState::pos_.y", dot_.m_xf.position.y);
        outState.putFloat("GameState::windowSize_.x", windowSize_.x);
        outState.putFloat("GameState::windowSize_.y", windowSize_.y);
        outState.putFloat("GameState::windowPos_.x", windowPos_.x);
        outState.putFloat("GameState::windowPos_.y", windowPos_.y);
    }

    /**
     * Populate this instance with the state passed as a parameter.
     * 
     * @param savedState
     */
    private void restore(Bundle savedState) {
        state_ = (State) savedState.getSerializable("GameState::state_");
        if (state_ == State.RUNNING)
            state_ = State.PAUSED;

        windowSize_ = new Vec2(savedState.getFloat("GameState::windowSize_.x"),
                savedState.getFloat("GameState::windowSize_.y"));
        windowPos_ = new Vec2(savedState.getFloat("GameState::windowPos_.x"),
                savedState.getFloat("GameState::windowPos_.y"));

        dot_ = createDot(new Vec2(savedState.getFloat("GameState::pos_.x"), savedState.getFloat("GameState::pos_.y")));
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

    public void startMove(Vec2 pos) {
        isMoving_ = true;
        mjd_.bodyB = dot_;
        mjd_.target.set(pos);
        mj_ = (MouseJoint) world_.createJoint(mjd_);
        dot_.setAwake(true);
    }

    public void stopMove() {
        isMoving_ = false;
        world_.destroyJoint(mj_);
        mj_ = null;
    }

    public boolean isMoving() {
        return isMoving_;
    }

    public void setPos(Vec2 pos) {
        mj_.setTarget(pos);
        dot_.setAwake(true);
    }

    public Vec2 getPos() {
        return dot_.m_xf.position;
    }

    public Body getTorso() {
        return torso_;
    }

    public Body getLeftThigh() {
        return leftThigh_;
    }

    public Body getLeftLeg() {
        return leftLeg_;
    }

    public Body getRightThigh() {
        return rightThigh_;
    }

    public Body getRightLeg() {
        return rightLeg_;
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
        worldSize_.set(worldSize);
    }

    public Vec2 getWindowSize() {
        return worldSize_;
    }

    public void setWindowSize(Vec2 windowSize) {
        windowSize_.set(windowSize);
    }

    public Vec2 getWindowPos() {
        return windowPos_;
    }

    public void setWindowPos(Vec2 windowPos) {
        windowPos_.set(windowPos);
    }

    public Vec2 worldToWindow(Vec2 p) {
        return new Vec2(p.x - windowPos_.x, p.y - windowPos_.y);
    }

    public Vec2 windowToWorld(Vec2 p) {
        return new Vec2(windowPos_.x + p.x, windowPos_.y + p.y);
    }
}
