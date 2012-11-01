/*
 * Copyright (c) 2011 Kazuhiko Kobayashi All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <p/>
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * <p/>
 * * Neither the name of 'MMDLoaderJME' nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.shaderblow.lightblow;

import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;

/**
 * @author kobayasi
 */
public class CartoonEdgeProcessor implements SceneProcessor {

    private RenderManager rm;
    private ViewPort vp;

    public CartoonEdgeProcessor() {
    }

    @Override
    public void initialize(final RenderManager rm, final ViewPort vp) {
        this.rm = rm;
        this.vp = vp;
    }

    @Override
    public void reshape(final ViewPort vp, final int w, final int h) {
    }

    @Override
    public boolean isInitialized() {
        if (this.rm != null) {
            return true;
        }
        return false;
    }

    @Override
    public void preFrame(final float tpf) {
    }

    @Override
    public void postQueue(final RenderQueue rq) {
        this.rm.setForcedTechnique("CartoonEdge");
        this.rm.renderViewPortQueues(this.vp, false);
        this.rm.setForcedTechnique(null);
    }

    @Override
    public void postFrame(final FrameBuffer out) {
    }

    @Override
    public void cleanup() {
    }
}
