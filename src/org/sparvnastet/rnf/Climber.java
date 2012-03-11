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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import android.os.Bundle;

/**
 * The GameState class represents the momentary state of a game. It contains all
 * information required to fully represent the game logic. It does not contain
 * visualization state.
 */
public class Climber {
    private World world_;

    private Body torso_;
    private Body leftThigh_;
    private Body leftLeg_;
    private Body rightThigh_;
    private Body rightLeg_;
    private Body leftUpperArm_;
    private Body leftLowerArm_;
    private Body rightUpperArm_;
    private Body rightLowerArm_;
    private Body leftHand_;
    private Body rightHand_;

    private static final Vec2 TORSO_DIM = new Vec2(0.45f, 0.55f);
    private static final Vec2 THIGH_DIM = new Vec2(0.20f, 0.45f);
    private static final Vec2 LEG_DIM = new Vec2(0.15f, 0.50f);
    private static final Vec2 UPPER_ARM_DIM = new Vec2(0.20f, 0.10f);
    private static final Vec2 LOWER_ARM_DIM = new Vec2(0.25f, 0.05f);
    private static final Vec2 HAND_DIM = new Vec2(0.1f, 0.1f);

    /**
     * Create a new default state or restore a saved state.
     * 
     * @param savedState
     *            optional saved state, or null to create a new default state.
     */
    public Climber(World world, Bundle savedState) {
        world_ = world;

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
        Vec2 torsoPos = new Vec2(0, 0);
        torso_ = createLimb(torsoPos, TORSO_DIM);

        Vec2 leftThighPos = new Vec2(torsoPos.x - TORSO_DIM.x / 4.0f, torsoPos.y - TORSO_DIM.y / 2.0f - THIGH_DIM.y
                / 2.0f);
        leftThigh_ = createLimb(leftThighPos, THIGH_DIM);

        Vec2 leftLegPos = new Vec2(leftThighPos.x, leftThighPos.y - THIGH_DIM.y / 2.0f - LEG_DIM.y / 2.0f);
        leftLeg_ = createLimb(leftLegPos, LEG_DIM);

        Vec2 rightThighPos = new Vec2(torsoPos.x + TORSO_DIM.x / 4.0f, torsoPos.y - TORSO_DIM.y / 2.0f - THIGH_DIM.y
                / 2.0f);
        rightThigh_ = createLimb(rightThighPos, THIGH_DIM);

        Vec2 rightLegPos = new Vec2(rightThighPos.x, rightThighPos.y - THIGH_DIM.y / 2.0f - LEG_DIM.y / 2.0f);
        rightLeg_ = createLimb(rightLegPos, LEG_DIM);

        Vec2 leftUpperArmPos = new Vec2(torsoPos.x - TORSO_DIM.x / 2.0f - UPPER_ARM_DIM.x / 2.0f, torsoPos.y
                + TORSO_DIM.y / 2.0f - UPPER_ARM_DIM.y / 2.0f);
        leftUpperArm_ = createLimb(leftUpperArmPos, UPPER_ARM_DIM);

        Vec2 leftLowerArmPos = new Vec2(leftUpperArmPos.x - UPPER_ARM_DIM.x / 2.0f - LOWER_ARM_DIM.x / 2.0f,
                leftUpperArmPos.y);
        leftLowerArm_ = createLimb(leftLowerArmPos, LOWER_ARM_DIM);

        Vec2 leftHandPos = new Vec2(leftLowerArmPos.x - LOWER_ARM_DIM.x / 2.0f - HAND_DIM.x / 2.0f, leftLowerArmPos.y);
        leftHand_ = createLimb(leftHandPos, HAND_DIM);

        Vec2 rightUpperArmPos = new Vec2(torsoPos.x + TORSO_DIM.x / 2.0f + UPPER_ARM_DIM.x / 2.0f, torsoPos.y
                + TORSO_DIM.y / 2.0f - UPPER_ARM_DIM.y / 2.0f);
        rightUpperArm_ = createLimb(rightUpperArmPos, UPPER_ARM_DIM);

        Vec2 rightLowerArmPos = new Vec2(rightUpperArmPos.x + UPPER_ARM_DIM.x / 2.0f + LOWER_ARM_DIM.x / 2.0f,
                rightUpperArmPos.y);
        rightLowerArm_ = createLimb(rightLowerArmPos, LOWER_ARM_DIM);

        Vec2 rightHandPos = new Vec2(rightLowerArmPos.x + LOWER_ARM_DIM.x / 2.0f + HAND_DIM.x / 2.0f,
                rightLowerArmPos.y);
        rightHand_ = createLimb(rightHandPos, HAND_DIM);

        // Joints
        Vec2 torsoLeftThighAnchor = new Vec2(leftThighPos.x, leftThighPos.y + THIGH_DIM.y / 2.0f);
        createJoint(torso_, leftThigh_, torsoLeftThighAnchor);

        Vec2 leftThighLegAnchor = new Vec2(leftThighPos.x, leftThighPos.y - THIGH_DIM.y / 2.0f);
        createJoint(leftThigh_, leftLeg_, leftThighLegAnchor);

        Vec2 torsoRightThighAnchor = new Vec2(rightThighPos.x, rightThighPos.y + THIGH_DIM.y / 2.0f);
        createJoint(torso_, rightThigh_, torsoRightThighAnchor);

        Vec2 rightThighLegAnchor = new Vec2(rightThighPos.x, rightThighPos.y - THIGH_DIM.y / 2.0f);
        createJoint(rightThigh_, rightLeg_, rightThighLegAnchor);

        Vec2 torsoLeftUpperArmAnchor = new Vec2(leftUpperArmPos.x + UPPER_ARM_DIM.x / 2.0f, leftUpperArmPos.y);
        createJoint(torso_, leftUpperArm_, torsoLeftUpperArmAnchor);

        Vec2 leftUpperLowerArmAnchor = new Vec2(leftUpperArmPos.x - UPPER_ARM_DIM.x / 2.0f, leftUpperArmPos.y);
        createJoint(leftUpperArm_, leftLowerArm_, leftUpperLowerArmAnchor);

        Vec2 leftArmHandAnchor = new Vec2(leftLowerArmPos.x - LOWER_ARM_DIM.x / 2.0f, leftLowerArmPos.y);
        createJoint(leftLowerArm_, leftHand_, leftArmHandAnchor);

        Vec2 torsoRightUpperArmAnchor = new Vec2(rightUpperArmPos.x - UPPER_ARM_DIM.x / 2.0f, rightUpperArmPos.y);
        createJoint(torso_, rightUpperArm_, torsoRightUpperArmAnchor);

        Vec2 rightUpperLowerArmAnchor = new Vec2(rightUpperArmPos.x + UPPER_ARM_DIM.x / 2.0f, rightUpperArmPos.y);
        createJoint(rightUpperArm_, rightLowerArm_, rightUpperLowerArmAnchor);

        Vec2 rightArmHandAnchor = new Vec2(rightLowerArmPos.x + LOWER_ARM_DIM.x / 2.0f, rightLowerArmPos.y);
        createJoint(rightLowerArm_, rightHand_, rightArmHandAnchor);
    }

    private Joint createJoint(Body bodyA, Body bodyB, Vec2 anchor) {
        RevoluteJointDef jdef = new RevoluteJointDef();
        jdef.motorSpeed = 0.0f;
        jdef.maxMotorTorque = 0.05f;
        jdef.enableMotor = true;
        jdef.initialize(bodyA, bodyB, anchor);
        return world_.createJoint(jdef);
    }

    private Body createLimb(Vec2 pos, Vec2 dim) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(dim.x / 2.0f, dim.y / 2.0f);

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position = pos;
        Body body = world_.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.filter.categoryBits = 0x2;
        fd.filter.maskBits = 0x1;

        body.createFixture(fd);
        return body;
    }

    /**
     * Save the climber state in the Bundle. It can later be restored by passing
     * the Bundle to the constructor of a new GameState.
     * 
     * @param outState
     */
    public void save(Bundle outState) {
    }

    /**
     * Populate this instance with the state passed as a parameter.
     * 
     * @param savedState
     */
    private void restore(Bundle savedState) {
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

    public Body getLeftUpperArm() {
        return leftUpperArm_;
    }

    public Body getLeftLowerArm() {
        return leftLowerArm_;
    }

    public Body getLeftHand() {
        return leftHand_;
    }

    public Vec2 getLeftHandPos() {
        return leftHand_.m_xf.position;
    }

    public void setLeftHandPos(Vec2 pos) {
        leftHand_.m_xf.position.set(pos);
    }

    public Body getRightUpperArm() {
        return rightUpperArm_;
    }

    public Body getRightLowerArm() {
        return rightLowerArm_;
    }

    public Body getRightHand() {
        return rightHand_;
    }

    public Vec2 getRightHandPos() {
        return rightHand_.m_xf.position;
    }

    public void setRightHandPos(Vec2 pos) {
        rightHand_.m_xf.position.set(pos);
    }

    /**
     * Get the gripper (hand|foot) closest to the specified point
     */
    public Body getClosestGripper(Vec2 pos) {
        float lhd = pos.sub(leftHand_.getPosition()).lengthSquared();
        float rhd = pos.sub(rightHand_.getPosition()).lengthSquared();

        Body closestHand = lhd < rhd ? leftHand_ : rightHand_;
        return closestHand;
    }
}
