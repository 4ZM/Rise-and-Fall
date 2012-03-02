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

import org.sparvnastet.rnf.GameState;
import org.sparvnastet.rnf.test.helpers.TestRenderer;
import org.sparvnastet.rnf.test.helpers.TestSurfaceHolder;

import android.test.AndroidTestCase;

public class RendererTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    public void testRenderDraws() {
        TestRenderer renderer = new TestRenderer();
        GameState state = new GameState(null);

        TestSurfaceHolder sh = new TestSurfaceHolder();
        sh.addCallback(renderer);

        // Don't draw before surface is created
        renderer.drawFlag_ = false;
        renderer.render(state);
        assertFalse(renderer.drawFlag_);

        // Create surface and draw draw once surface is created
        sh.announceCreated();
        renderer.drawFlag_ = false;
        renderer.render(state);
        assertTrue(renderer.drawFlag_);

        // Still drawing after surface is changed
        sh.announceChanged();
        renderer.drawFlag_ = false;
        renderer.render(state);
        assertTrue(renderer.drawFlag_);

        // Don't draw after surface is destroyed
        sh.annouceDestroyed();
        renderer.drawFlag_ = false;
        renderer.render(state);
        assertFalse(renderer.drawFlag_);
    }
}
