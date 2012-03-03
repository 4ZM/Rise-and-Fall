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