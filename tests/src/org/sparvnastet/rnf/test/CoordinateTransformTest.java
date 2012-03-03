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

package org.sparvnastet.rnf.test;

import junit.framework.Assert;

import org.jbox2d.common.Vec2;
import org.sparvnastet.rnf.CoordinateTransform;

import android.test.AndroidTestCase;

public class CoordinateTransformTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {

    }

    public void testInvalidInput() {
        CoordinateTransform t = new CoordinateTransform(1.0f, 1.0f, 0.0f, 0.0f);
        try {
            t.transform(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void testIdentiryTransfrom() {
        Vec2 p = new Vec2(0.0f, 0.0f);
        Vec2 pt;
        CoordinateTransform t = new CoordinateTransform(1.0f, 1.0f, 0.0f, 0.0f);

        pt = t.transform(p);
        assertEquals(p, pt);

        p.x = -2.0f;
        pt = t.transform(p);
        assertEquals(p, pt);

        p.x = 23.0f;
        pt = t.transform(p);
        assertEquals(p, pt);

        p.x = -13;
        p.y = -42;
        pt = t.transform(p);
        assertEquals(p, pt);

        p.x = 521;
        p.y = 123;
        pt = t.transform(p);
        assertEquals(p, pt);
    }

    public void testScaled() {
        Vec2 pt;
        CoordinateTransform t = new CoordinateTransform(2.0f, -4.0f, 0.0f, 0.0f);

        Vec2 p = new Vec2(0.0f, 0.0f);
        pt = t.transform(p);
        assertEquals(0.0f, pt.x);
        assertEquals(0.0f, pt.y);

        p = new Vec2(100.0f, -200.0f);
        pt = t.transform(p);
        assertEquals(200.0f, pt.x);
        assertEquals(800.0f, pt.y);
    }

    public void testTranslated() {
        Vec2 p = new Vec2(0.0f, 0.0f);
        Vec2 pt;
        CoordinateTransform t = new CoordinateTransform(0.0f, 0.0f, 2.0f, -4.0f);
        pt = t.transform(p);
        assertEquals(2.0f, pt.x);
        assertEquals(-4.0f, pt.y);

    }

    public void testScaledAndTranslated() {
        Vec2 pt;
        CoordinateTransform t = new CoordinateTransform(2.0f, -4.0f, -20.0f, 40.0f);

        Vec2 p = new Vec2(0.0f, 0.0f);
        pt = t.transform(p);
        assertEquals(p.x * 2.0f - 20.0f, pt.x);
        assertEquals(p.y * -4.0f + 40.0f, pt.y);

        p = new Vec2(-100.0f, 200.0f);
        pt = t.transform(p);
        assertEquals(-220.0f, pt.x);
        assertEquals(-760.0f, pt.y);
    }
}
