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

        Vec2 p = toScreenCoords(gameState, gameState.getPos());

        Paint circlePaint = new Paint();
        if (gameState.isMoving()) {
            circlePaint.setARGB(255, 200, 40, 40);
            canvas.drawCircle(p.x, p.y, 40, circlePaint);
            circlePaint.setARGB(255, 0, 0, 0);
            canvas.drawCircle(p.x, p.y, 36, circlePaint);
        }

        circlePaint.setARGB(255, 200, 40, 40);
        canvas.drawCircle(p.x, p.y, screenScale(gameState).x * 0.3f + 4, circlePaint);
        circlePaint.setARGB(255, 0, 0, 0);
        canvas.drawCircle(p.x, p.y, screenScale(gameState).x * 0.3f, circlePaint);

        Paint bodyPaint = new Paint();
        bodyPaint.setARGB(255, 200, 40, 40);

        drawBody(canvas, gameState, bodyPaint, gameState.getTorso());
        drawBody(canvas, gameState, bodyPaint, gameState.getLeftThigh());
        drawBody(canvas, gameState, bodyPaint, gameState.getLeftLeg());
        drawBody(canvas, gameState, bodyPaint, gameState.getRightThigh());
        drawBody(canvas, gameState, bodyPaint, gameState.getRightLeg());
        drawBody(canvas, gameState, bodyPaint, gameState.getLeftUpperArm());
        drawBody(canvas, gameState, bodyPaint, gameState.getLeftLowerArm());
        drawBody(canvas, gameState, bodyPaint, gameState.getRightUpperArm());
        drawBody(canvas, gameState, bodyPaint, gameState.getRightLowerArm());

        // Draw text
        Paint textPaint = new Paint();
        textPaint.setARGB(255, 220, 220, 220);
        canvas.drawText("FPS: " + df.format(gameState.getFps()), 10.0f, 10.0f, textPaint);
        canvas.drawText("X: " + df.format(gameState.getPos().x) + " Y: " + df.format(gameState.getPos().y), 10.0f,
                20.0f, textPaint);
    }

    private void drawBody(Canvas canvas, GameState gameState, Paint paint, Body body) {
        PolygonShape s = (PolygonShape) body.getFixtureList().m_shape;

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

        Vec2 torsoPos = toScreenCoords(gameState, body.getPosition());

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(torsoPos.x, torsoPos.y);
        canvas.scale(screenScale(gameState).x, screenScale(gameState).y);
        canvas.rotate(body.getAngle() / (2.0f * 3.14159f) * 360.0f);
        canvas.drawPath(path, paint);
        canvas.restore();
    }
}