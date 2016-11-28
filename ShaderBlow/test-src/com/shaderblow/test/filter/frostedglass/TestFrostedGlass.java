/*
 * Copyright (c) 2009-2013 ShaderBlow
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
package com.shaderblow.test.filter.frostedglass;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.Type;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;
import com.shaderblow.filter.frostedglass.FrostedGlassFilter;
import com.shaderblow.lightblow.CartoonEdgeProcessor;

/**
 * @author wezrule
 */
public class TestFrostedGlass extends SimpleApplication implements ActionListener {

    private boolean enabled = true;
    private FilterPostProcessor fpp;
    private FrostedGlassFilter frostedGlassFilter;

    public static void main(final String[] args) {
        final TestFrostedGlass app = new TestFrostedGlass();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("assets", FileLocator.class);
    		assetManager.registerLocator("test-data", FileLocator.class);          

        flyCam.setMoveSpeed(5);

        buildScene();

        fpp = new FilterPostProcessor(assetManager);

        // Create a green filter
        frostedGlassFilter = new FrostedGlassFilter();
        frostedGlassFilter.setRandomFactor(0.025f);
        frostedGlassFilter.setRandomScale(2.1f);

        // Create Noise and mask textures
        fpp.addFilter(frostedGlassFilter);
        viewPort.addProcessor(fpp);

        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "toggle");
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("toggle") && isPressed) {
            if (enabled) {
                enabled = false;
                viewPort.removeProcessor(fpp);
            } else {
                enabled = true;
                viewPort.addProcessor(fpp);
            }
        }
    }

    // Copied from the glass test case
    private void buildScene() {
        final TextureKey skyhi = new TextureKey("TestTextures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        skyhi.setTextureTypeHint(Type.CubeMap); //        skyhi.setAsCube(true);

        final Texture texlow = this.assetManager.loadTexture(skyhi);
        this.rootNode.attachChild(SkyFactory.createSky(this.assetManager, texlow, false));

        final Spatial char_boy1 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat1 = this.assetManager.loadMaterial("TestMaterials/Glass/Glass1.j3m");
        char_boy1.setMaterial(mat1);
        char_boy1.setLocalTranslation(0, 0, 0);
        TangentBinormalGenerator.generate(char_boy1);

        AnimControl control = char_boy1.getControl(AnimControl.class);
        AnimChannel channel = control.createChannel();
        channel.setAnim("Action");
        SkeletonControl skeletonControl = char_boy1.getControl(SkeletonControl.class);
        skeletonControl.setHardwareSkinningPreferred(true);

        this.rootNode.attachChild(char_boy1);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = this.assetManager.loadMaterial("TestMaterials/Glass/Glass1_bump.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(1, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);

        AnimControl control2 = char_boy2.getControl(AnimControl.class);
        AnimChannel channel2 = control2.createChannel();
        channel2.setAnim("Action");
        SkeletonControl skeletonControl2 = char_boy2.getControl(SkeletonControl.class);
        skeletonControl2.setHardwareSkinningPreferred(true);

        this.rootNode.attachChild(char_boy2);

        final Spatial char_boy3 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat3 = this.assetManager.loadMaterial("TestMaterials/Glass/Glass2_low.j3m");
        char_boy3.setMaterial(mat3);
        char_boy3.setLocalTranslation(-1, 0, 0);
        TangentBinormalGenerator.generate(char_boy3);

        SkeletonControl skeletonControl3 = char_boy3.getControl(SkeletonControl.class);
        skeletonControl3.setHardwareSkinningPreferred(true);

        this.rootNode.attachChild(char_boy3);

        final Spatial char_boy4 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat4 = this.assetManager.loadMaterial("TestMaterials/Glass/Glass3_color.j3m");
        char_boy4.setMaterial(mat4);
        char_boy4.setLocalTranslation(-2, 0, 0);
        TangentBinormalGenerator.generate(char_boy4);

        SkeletonControl skeletonControl4 = char_boy4.getControl(SkeletonControl.class);
        skeletonControl4.setHardwareSkinningPreferred(true);

        this.rootNode.attachChild(char_boy4);

        final Spatial char_boy5 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat5 = this.assetManager.loadMaterial("TestMaterials/Glass/Glass4_specular.j3m");
        char_boy5.setMaterial(mat5);
        char_boy5.setLocalTranslation(-3, 0, 0);
        TangentBinormalGenerator.generate(char_boy5);
        
             SkeletonControl skeletonControl5 = char_boy5.getControl(SkeletonControl.class);
             skeletonControl5.setHardwareSkinningPreferred(true);   
             
        this.rootNode.attachChild(char_boy5);

        this.flyCam.setMoveSpeed(10);
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);

        this.flyCam.setMoveSpeed(10);
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);

        if (this.renderer.getCaps().contains(Caps.GLSL100)) {
            final CartoonEdgeProcessor cartoonEdgeProcess = new CartoonEdgeProcessor();
            this.viewPort.addProcessor(cartoonEdgeProcess);
        }

        // Requiered for toon edge effect
        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);
    }
}