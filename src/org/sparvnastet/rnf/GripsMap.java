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

import android.graphics.Bitmap;
import android.graphics.Color;

public class GripsMap {
    private int width_;
    private int height_;
    private byte[] map_;

    public GripsMap(Bitmap bitmap) {
        width_ = bitmap.getWidth();
        height_ = bitmap.getHeight();
        int[] px = new int[width_ * height_];
        bitmap.getPixels(px, 0, width_, 0, 0, width_, height_);
        map_ = new byte[px.length];
        for (int i = 0; i < px.length; ++i)
            map_[i] = (byte) (px[i] == Color.BLACK ? 0 : 1);
    }

    public int getWidth() {
        return width_;
    }

    public int getHeight() {
        return height_;
    }

    public boolean isGrip(int x, int y) {
        return map_[y * width_ + x] == 1;
    }
}
