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

import java.text.DecimalFormat;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Render the climbing game state.
 */
class ClimbRenderer extends Renderer {
    private Resources resources_;

    public ClimbRenderer(Resources resources) {
        resources_ = resources;
    }

    DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void draw(Canvas canvas, GameState gameState) {
        canvas.drawColor(Color.BLACK);

        Paint paint = new Paint();
        paint.setARGB(255, 250, 90, 90);
        canvas.drawCircle(toScreenCoords(gameState, gameState.leftGrip_).x,
                toScreenCoords(gameState, gameState.leftGrip_).y, 5, paint);
        canvas.drawCircle(toScreenCoords(gameState, gameState.rightGrip_).x,
                toScreenCoords(gameState, gameState.rightGrip_).y, 5, paint);

        Climber climber = gameState.getClimber();

        paint.setARGB(255, 255, 0, 0);
        drawBody(canvas, gameState, paint, climber.getTorso());

        paint.setARGB(255, 0, 255, 0);
        drawBody(canvas, gameState, paint, climber.getLeftThigh());
        drawBody(canvas, gameState, paint, climber.getLeftLeg());

        paint.setARGB(255, 0, 0, 255);
        drawBody(canvas, gameState, paint, climber.getRightThigh());
        drawBody(canvas, gameState, paint, climber.getRightLeg());

        paint.setARGB(255, 255, 128, 0);
        drawBody(canvas, gameState, paint, climber.getLeftUpperArm());
        drawBody(canvas, gameState, paint, climber.getLeftLowerArm());

        paint.setARGB(255, 255, 0, 255);
        drawBody(canvas, gameState, paint, climber.getRightUpperArm());
        drawBody(canvas, gameState, paint, climber.getRightLowerArm());

        Body lh = climber.getLeftHand();
        Body rh = climber.getRightHand();

        if (gameState.getMovingGripper() == lh)
            paint.setARGB(255, 200, 100, 200);
        else
            paint.setARGB(255, 200, 40, 100);
        drawBody(canvas, gameState, paint, climber.getLeftHand());

        if (gameState.getMovingGripper() == rh)
            paint.setARGB(255, 200, 100, 200);
        else
            paint.setARGB(255, 200, 40, 100);
        drawBody(canvas, gameState, paint, climber.getRightHand());

        // Draw text
        Paint textPaint = new Paint();
        textPaint.setARGB(255, 220, 220, 220);
        canvas.drawText("FPS: " + df.format(gameState.getFps()), 10.0f, 10.0f, textPaint);
    }

    private void drawBody(Canvas canvas, GameState gameState, Paint paint, Body body) {

        Fixture f = body.getFixtureList();
        PolygonShape s = (PolygonShape) f.m_shape;

        Path path = new Path();
        Vec2 vertex = s.getVertex(0);
        path.moveTo(vertex.x, vertex.y);
        vertex = s.getVertex(1);
        path.lineTo(vertex.x, vertex.y);
        vertex = s.getVertex(2);
        path.lineTo(vertex.x, vertex.y);
        vertex = s.getVertex(3);
        path.lineTo(vertex.x, vertex.y);
        vertex = s.getVertex(0);
        path.lineTo(vertex.x, vertex.y);

        Vec2 bodyPos = toScreenCoords(gameState, body.getPosition());

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(bodyPos.x, bodyPos.y);
        canvas.scale(screenScale(gameState).x, screenScale(gameState).y);
        canvas.rotate(body.getAngle() / (2.0f * 3.14159f) * 360.0f);
        canvas.drawPath(path, paint);
        canvas.restore();

    }
}