/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaderblow.filter.simplerefractionfilter;

/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.io.IOException;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;

/**
 * BloomFilter is used to make objects in the scene have a glow effect.<br>
 * There are 2 mode : Scene and Objects.<br> Scene mode extracts the bright
 * parts of the scene to make them glow<br> Object mode make objects glow
 * according to their material's glowMap or their GlowColor<br>
 *
 * @see <a
 * href="http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:bloom_and_glow">advanced:bloom_and_glow</a>
 * for more details
 *
 * @author Rémy Bouquet aka Nehon
 */
public class SimpleRefractionFilter extends Filter {

    public enum RefractMode {

        /**
         * Apply refraction filter to entire scene.
         */
        Scene,
        /**
         * Apply refraction only to objects that have refraction MatDef.
         */
        Objects,}
    // Refract parameters
    private RefractMode refractMode = RefractMode.Scene;
    private float downSamplingFactor = 1;
//    private Pass refractPass = new Pass();
    private int screenWidth;
    private int screenHeight;
    protected Texture2D refractionTexture;
    protected Texture2D depthTexture;
    protected Texture2D normalTexture;
    protected Texture2D dudvTexture;
    protected Texture2D preRefractTexture;
    private final AssetManager manager;
    // private Material matRefraction;
    private RenderManager renderManager;
    private ViewPort viewPort;
    private Pass preGrayPass;

    // private Pass preGlowPass;
    /**
     * Creates a Refract filter
     */
    public SimpleRefractionFilter(final AssetManager manager, RefractMode refractMode) {
        super("RefractionFilter");
        this.refractMode = refractMode;
        this.manager = manager;
        this.material = new Material(manager, "ShaderBlow/MatDefs/Filters/SimpleRefraction/SimpleRefractionFilter.j3md");
    }

    @Override
    protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp,
            final int w, final int h) {

        this.renderManager = renderManager;
        this.viewPort = vp;

        this.normalTexture = (Texture2D) manager.loadTexture("TestTextures/Refraction/water_normalmap.png");
        this.normalTexture.setWrap(WrapMode.Repeat);
        this.normalTexture.setMagFilter(MagFilter.Bilinear);
        this.normalTexture.setMinFilter(MinFilter.BilinearNearestMipMap);

        this.dudvTexture = (Texture2D) manager.loadTexture("TestTextures/Refraction/dudv_map.png");
        this.dudvTexture.setMagFilter(MagFilter.Bilinear);
        this.dudvTexture.setMinFilter(MinFilter.BilinearNearestMipMap);
        this.dudvTexture.setWrap(WrapMode.Repeat);


        this.material.setFloat("distortionScale", 0.1f);
        this.material.setFloat("distortionMix", 0.3f);
        this.material.setFloat("texScale", 0.3f);
//        this.material.setFloat("timeFlow", 0.5f);


        this.material.setTexture("water_normalmap", this.normalTexture);
        this.material.setTexture("water_dudvmap", this.dudvTexture);


        if (refractMode != refractMode.Scene) {
            preGrayPass = new Pass() {
                @Override
                public boolean requiresSceneAsTexture() {
                    return true;
                }

                @Override
                public boolean requiresDepthAsTexture() {
                    return true;
                }
            };
            preGrayPass.init(renderManager.getRenderer(), w, h, Format.RGBA8, Format.Depth);

            material.setTexture("GrayTex", preGrayPass.getRenderedTexture());
        }
    }

    @Override
    protected Material getMaterial() {

        return this.material;
    }

//    @Override
//    public void preFrame(final float tpf) {
//        
//    }

    @Override
    protected void postQueue(RenderQueue queue) {
        if (refractMode != refractMode.Scene) {
            renderManager.getRenderer().setBackgroundColor(ColorRGBA.BlackNoAlpha);
            renderManager.getRenderer().setFrameBuffer(preGrayPass.getRenderFrameBuffer());
            renderManager.getRenderer().clearBuffers(true, true, true);
            renderManager.setForcedTechnique("Refract");
            renderManager.renderViewPortQueues(viewPort, false);
            renderManager.setForcedTechnique(null);
            renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
        }
    }
    
    @Override
    protected void cleanUpFilter(Renderer r) {
        preGrayPass.cleanup(r);
    }

    @Override
    public void write(final JmeExporter ex) throws IOException {
        super.write(ex);
        final OutputCapsule oc = ex.getCapsule(this);

    }

    @Override
    public void read(final JmeImporter im) throws IOException {
        super.read(im);
        final InputCapsule ic = im.getCapsule(this);

    }
}
