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

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Render the climbing game state.
 */
class ClimbRenderer extends Renderer {
    private Resources resources_;

    public ClimbRenderer(Resources resources) {
        resources_ = resources;
    }

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
        canvas.drawCircle(p.x, p.y, 20, circlePaint);
        circlePaint.setARGB(255, 0, 0, 0);
        canvas.drawCircle(p.x, p.y, 16, circlePaint);

        // Draw frame rate
        Paint textPaint = new Paint();
        textPaint.setARGB(255, 220, 220, 220);
        canvas.drawText("FPS: " + gameState.getFps(), 10.0f, 10.0f, textPaint);
        canvas.drawText("X: " + gameState.getPos().x, 10.0f, 20.0f, textPaint);
        canvas.drawText("Y: " + gameState.getPos().y, 10.0f, 30.0f, textPaint);
    }
}