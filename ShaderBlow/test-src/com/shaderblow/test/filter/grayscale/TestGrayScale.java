/*
 * Copyright (c) 2009-2012 ShaderBlow
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
 * * Neither the name of 'ShaderBlow' nor the names of its contributors
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
package com.shaderblow.test.filter.grayscale;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;
import com.shaderblow.filter.grayscale.GrayScaleFilter;

public class TestGrayScale extends SimpleApplication {

    private FilterPostProcessor fpp;
    private boolean enabled = true;
    private GrayScaleFilter grayScale;

    public static void main(final String[] args) {
        final TestGrayScale app = new TestGrayScale();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);

        this.flyCam.setMoveSpeed(10);

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat = this.assetManager.loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_ibl.j3m");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        this.rootNode.attachChild(char_boy);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        this.rootNode.addLight(al);

        this.flyCam.setMoveSpeed(15);

        this.fpp = new FilterPostProcessor(this.assetManager);
        this.fpp.setNumSamples(4);
        this.grayScale = new GrayScaleFilter();
        this.fpp.addFilter(this.grayScale);
        this.viewPort.addProcessor(this.fpp);
        this.initInputs();
    }

    private void initInputs() {
        this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));

        final ActionListener acl = new ActionListener() {

            @Override
            public void onAction(final String name, final boolean keyPressed, final float tpf) {
                if (name.equals("toggle") && keyPressed) {
                    if (TestGrayScale.this.enabled) {
                        TestGrayScale.this.enabled = false;
                        TestGrayScale.this.viewPort.removeProcessor(TestGrayScale.this.fpp);
                    } else {
                        TestGrayScale.this.enabled = true;
                        TestGrayScale.this.viewPort.addProcessor(TestGrayScale.this.fpp);
                    }
                }

            }
        };

        this.inputManager.addListener(acl, "toggle");

    }
}