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
package com.shaderblow.test.dissolve;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * @author thetoucher
 */
public class TestDissolve extends SimpleApplication {

    // speed of animation
    private final float speed = .125f;

    private float count = 0;
    private int dir = 1;

    private Vector2f DSParams, DSParamsInv, DSParamsBurn;

    public static void main(final String[] args) {
        final TestDissolve app = new TestDissolve();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);

        Texture t;
        Material mat;

        this.cam.setLocation(new Vector3f(0, 1.5f, 10f));
        // flyCam.setEnabled(false);

        // reusable params
        this.DSParams = new Vector2f(0, 0); // standard
        this.DSParamsInv = new Vector2f(0, 1); // inverted
        this.DSParamsBurn = new Vector2f(0, 0); // used for offset organic burn map

        // linear dissolve
        addTestCube(-3f, 3f, this.assetManager.loadTexture("TestTextures/Dissolve/linear.png"), this.DSParams);

        // organic dissolve
        addTestCube(0, 3f, this.assetManager.loadTexture("TestTextures/Dissolve/burnMap.png"), this.DSParamsInv);

        // pixel dissolve
        t = this.assetManager.loadTexture("TestTextures/Dissolve/pixelMap.png");
        t.setMagFilter(Texture.MagFilter.Nearest); // this is needed to retain the crisp pixelated look
        addTestCube(3f, 3f, t, this.DSParams);

        // organic growth
        mat = addTestCube(-3f, 0, this.assetManager.loadTexture("TestTextures/Dissolve/growMap.png"), this.DSParamsInv)
                .getMaterial();
        mat.setColor("Ambient", ColorRGBA.Green);
        mat.setTexture("DiffuseMap", this.assetManager.loadTexture("TestTextures/Dissolve/growMap.png"));

        addTestCube(-3f, 0, this.assetManager.loadTexture("TestTextures/Dissolve/growMap.png"), this.DSParams);

        // texture mask
        mat = addTestCube(0, 0, this.assetManager.loadTexture("TestTextures/Dissolve/streetBurn.png"), this.DSParams)
                .getMaterial();
        mat.setTexture("DiffuseMap", this.assetManager.loadTexture("TestTextures/Dissolve/streetClean.png"));
        mat.setColor("Ambient", ColorRGBA.White);

        mat = addTestCube(0f, 0f, this.assetManager.loadTexture("TestTextures/Dissolve/streetBurn.png"),
                this.DSParamsInv).getMaterial();
        mat.setTexture("DiffuseMap", this.assetManager.loadTexture("TestTextures/Dissolve/street.png"));
        mat.setColor("Ambient", ColorRGBA.White);

        // organic burn
        addTestCube(3f, 0, this.assetManager.loadTexture("TestTextures/Dissolve/burnMap.png"), this.DSParamsBurn)
                .getMaterial().setColor("Ambient", ColorRGBA.Red);
        addTestCube(3f, 0, this.assetManager.loadTexture("TestTextures/Dissolve/burnMap.png"), this.DSParams);

        final AmbientLight a = new AmbientLight();
        a.setColor(ColorRGBA.White);
        this.rootNode.addLight(a);

        this.viewPort.setBackgroundColor(ColorRGBA.Gray);
        this.flyCam.setMoveSpeed(5);

    }

    private Geometry addTestCube(final float xPos, final float yPos, final Texture map, final Vector2f DSParams) {
        // Create a material instance using ShaderBlow's Lighting.j3md
        final Material mat = new Material(this.assetManager, "ShaderBlow/MatDefs/Dissolve/Lighting.j3md");
        mat.setColor("Ambient", ColorRGBA.Blue);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.Black);
        mat.setBoolean("UseMaterialColors", true);

        mat.setTexture("DissolveMap", map); // Set mask texture map
        mat.setVector2("DissolveParams", DSParams); // Set params

        final Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        final Geometry geom = new Geometry("Box", b);
        geom.setMaterial(mat);

        geom.setLocalTranslation(new Vector3f(xPos, yPos, 0));
        this.rootNode.attachChild(geom);

        return geom;
    }

    @Override
    public void simpleUpdate(final float tpf) {

        this.count += tpf * this.speed * this.dir;

        // animation ossolation
        if (this.count > 1f) {
            this.dir = -1;
        } else if (this.count < 0) {
            this.dir = 1;
        }

        // update the dissolve amounts
        this.DSParams.setX(this.count);
        this.DSParamsInv.setX(this.count);
        this.DSParamsBurn.setX(this.count - .05f);
    }

}
