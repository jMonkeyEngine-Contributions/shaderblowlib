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
package com.shaderblow.test.forceshield;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.shaderblow.forceshield.ForceShieldControl;

public class TestShield extends SimpleApplication implements ActionListener {

    private ForceShieldControl forceShieldControl;

    public static void main(final String[] args) {
        new TestShield().start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);

        initCrossHairs();

        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, 0));
        this.rootNode.addLight(sun);

        final Box box = new Box(1, 1, 1);
        final Geometry cube = new Geometry("ship", box);
        cube.setLocalScale(0.5f, 0.5f, 0.5f);
        final Material mat1 = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        this.rootNode.attachChild(cube);

        // Create spatial to be the shield
        final Sphere sphere = new Sphere(30, 30, 1.2f);
        final Geometry shield = new Geometry("forceshield", sphere);
        shield.setQueueBucket(Bucket.Transparent); // Remenber to set the queue bucket to transparent for the spatial

        // Create ForceShieldControl
        this.forceShieldControl = new ForceShieldControl(this.assetManager, 0.5f);
        shield.addControl(this.forceShieldControl); // Add the control to the spatial
        this.forceShieldControl.setEffectSize(2f); // Set the effect size
        this.forceShieldControl.setColor(new ColorRGBA(1, 0, 0, 3)); // Set effect color
        this.forceShieldControl.setVisibility(0.1f); // Set shield visibility.

        // Set a texture to the shield
        this.forceShieldControl.setTexture(this.assetManager.loadTexture("TestTextures/ForceShield/fs_texture.png"));

        // this.forceShieldControl.setEnabled(false); // Enable, disable animation.

        this.rootNode.attachChild(shield);

        this.flyCam.setMoveSpeed(10);

        this.inputManager.addMapping("FIRE", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        this.inputManager.addListener(this, "FIRE");

        this.viewPort.setBackgroundColor(ColorRGBA.Gray);
    }

    @Override
    public void onAction(final String name, final boolean isPressed, final float arg) {
        if (name.equals("FIRE") && isPressed) {
            final CollisionResults crs = new CollisionResults();
            this.rootNode.collideWith(new Ray(this.cam.getLocation(), this.cam.getDirection()), crs);
            if (crs.getClosestCollision() != null) {
                System.out.println("Hit at " + crs.getClosestCollision().getContactPoint());
                // Register a hit
                this.forceShieldControl.registerHit(crs.getClosestCollision().getContactPoint());
            }
        }
    }

    protected void initCrossHairs() {
        this.guiNode.detachAllChildren();
        this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
        final BitmapText ch = new BitmapText(this.guiFont, false);
        ch.setSize(this.guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation(
                // center
                this.settings.getWidth() / 2 - this.guiFont.getCharSet().getRenderedSize() / 3 * 2,
                this.settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        this.guiNode.attachChild(ch);
    }

}
