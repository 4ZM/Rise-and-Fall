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

package org.sparvnastet.rnf.test.helpers;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Surface;
import android.view.SurfaceHolder;

public class TestSurfaceHolder implements SurfaceHolder {
    public ArrayList<Callback> cbs_ = new ArrayList<Callback>();

    @Override
    public void addCallback(Callback callback) {
        cbs_.add(callback);
    }

    public void announceCreated() {
        for (Callback cb : cbs_) {
            cb.surfaceCreated(this);
            announceChanged();
        }
    }

    public void annouceDestroyed() {
        for (Callback cb : cbs_)
            cb.surfaceDestroyed(this);
    }

    public void announceChanged() {
        for (Callback cb : cbs_)
            cb.surfaceChanged(this, PixelFormat.OPAQUE, 1, 1);
    }

    @Override
    public Canvas lockCanvas() {
        return new Canvas();
    }

    // The rest of the stubbed functions

    @Override
    public Surface getSurface() {
        return null;
    }

    @Override
    public Rect getSurfaceFrame() {
        return null;
    }

    @Override
    public boolean isCreating() {
        return false;
    }

    @Override
    public Canvas lockCanvas(Rect dirty) {
        return new Canvas();
    }

    @Override
    public void removeCallback(Callback callback) {
    }

    @Override
    public void setFixedSize(int width, int height) {
    }

    @Override
    public void setFormat(int format) {
    }

    @Override
    public void setKeepScreenOn(boolean screenOn) {
    }

    @Override
    public void setSizeFromLayout() {
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void unlockCanvasAndPost(Canvas canvas) {
    }
}