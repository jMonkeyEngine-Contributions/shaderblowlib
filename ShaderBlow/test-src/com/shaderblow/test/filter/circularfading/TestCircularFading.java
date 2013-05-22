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
package com.shaderblow.test.filter.circularfading;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;
import com.shaderblow.filter.circularfading.CircularFadingFilter;
import com.shaderblow.test.TestObjectBuilder;

public class TestCircularFading extends SimpleApplication {

    private FilterPostProcessor fpp;
    private CircularFadingFilter circleFadingFilter;

    public static void main(final String[] args) {
        final TestCircularFading app = new TestCircularFading();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);

        this.flyCam.setMoveSpeed(10);

        this.initInputs();

        TestObjectBuilder.buildSkybox(this.assetManager, this.rootNode);
        TestObjectBuilder.buildFloor(this.assetManager, this.rootNode);
        TestObjectBuilder.buildLights(this.rootNode);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = this.assetManager.loadMaterial("TestMaterials/MatCap/MatCapBump1.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(2, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        this.rootNode.attachChild(char_boy2);

        this.fpp = new FilterPostProcessor(this.assetManager);
        this.fpp.setNumSamples(4);

        final Vector3f screenCoordinates = getCamera().getScreenCoordinates(char_boy2.getWorldTranslation());

        this.circleFadingFilter = new CircularFadingFilter(screenCoordinates);
        this.fpp.addFilter(this.circleFadingFilter);
        this.viewPort.addProcessor(this.fpp);
    }

    private void initInputs() {
        this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));

        final ActionListener acl = new ActionListener() {
            @Override
            public void onAction(final String name, final boolean keyPressed, final float tpf) {
                if (name.equals("toggle") && keyPressed) {
                    TestCircularFading.this.circleFadingFilter.setEnabled(!TestCircularFading.this.circleFadingFilter
                            .isEnabled());
                }

            }
        };

        this.inputManager.addListener(acl, "toggle");

    }
}