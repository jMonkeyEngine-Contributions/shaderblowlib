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
package com.shaderblow.forceshield;

import java.io.IOException;
import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;

/**
 * Force shield effect. It sets material to controlled object. If you experience problems, try higher polygon object.
 * 
 * @author Stanislav Fifik
 */
public class ForceShieldControl implements Control {

    private final Material material;
    private float maxTime = 0.5f;
    private final ArrayList<Vector3f> collisions = new ArrayList<Vector3f>();
    private final ArrayList<Float> collisionTimes = new ArrayList<Float>();
    private Spatial model;
    private boolean numChanged = false;
    private boolean enabled = true;
    private boolean work = false;
    private float timer = 0;
    private final float timerSize;

    /** Max number of hits displayed I've experienced crashes with 7 or 8 hits */
    private final int MAX_HITS = 4;

    public ForceShieldControl(final AssetManager assetManager) {
        this.material = new Material(assetManager, "ShaderBlow/MatDefs/ForceShield/ForceShield.j3md");
        this.material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        this.material.setFloat("MaxDistance", 1);
        this.timerSize = 4f;
    }

    /**
     * 
     * @param assetManager
     * @param duration
     *            - effect duration (Default is 0.5s)
     */
    public ForceShieldControl(final AssetManager assetManager, final float duration) {
        this(assetManager);
        this.maxTime = duration;
    }

    @Override
    public void update(final float tpf) {
        if (this.work && this.enabled) {

            if (this.timer > this.timerSize) {
                this.timer = 0f;
                this.work = false;
                return;
            }

            this.timer += tpf * 3f;

            for (int i = 0; i < this.collisionTimes.size(); i++) {
                float time = this.collisionTimes.get(i);
                time -= tpf;
                if (time <= 0) {
                    this.collisionTimes.remove(i);
                    this.collisions.remove(i--);
                    this.numChanged = true;
                    continue;
                }
                this.collisionTimes.set(i, time);
            }
            if (this.numChanged) {
                updateCollisionPoints();
            }
            updateCollisionAlpha();

            this.numChanged = false;

        }
    }

    /**
     * Adds hit to display.
     * 
     * @param position
     *            - world space position
     */
    public void registerHit(final Vector3f position) {
        if (!this.enabled) {
            return;
        }

        this.timer = 0f;
        this.work = true;
        final Vector3f lposition = new Vector3f();
        this.model.worldToLocal(position, lposition);
        this.collisions.add(new Vector3f(lposition.x, lposition.y, lposition.z));
        this.collisionTimes.add(this.maxTime);
        this.numChanged = true;
        updateCollisionPoints();
    }

    /**
     * Color of the shield
     * 
     * @param color
     */
    public void setColor(final ColorRGBA color) {
        this.material.setColor("Color", color);
    }

    /**
     * Visibility of inactive shield
     * 
     * @param percent
     */
    public void setVisibility(final float percent) {
        this.material.setFloat("MinAlpha", percent);
    }

    /**
     * Shield texture
     * 
     * @param texture
     */
    public void setTexture(final Texture texture) {
        this.material.setTexture("ColorMap", texture);
    }

    /**
     * material displaying effect
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Maximum distance from contact point where effect is visible Has to be set after setting control to object!
     * 
     * @param size
     */
    public void setEffectSize(final float size) {
        this.material.setFloat("MaxDistance", size / this.model.getLocalScale().x);
    }

    protected void updateCollisionAlpha() {
        final float[] alphas = new float[Math.min(this.collisionTimes.size(), this.MAX_HITS)];
        for (int i = 0; i < alphas.length && i < this.MAX_HITS; i++) {
            alphas[i] = this.collisionTimes.get(this.collisions.size() - 1 - i) / this.maxTime;
        }
        this.material.setParam("CollisionAlphas", VarType.FloatArray, alphas);
    }

    protected void updateCollisionPoints() {
        final Vector3f[] collisionsArray = new Vector3f[Math.min(this.collisions.size(), this.MAX_HITS)];
        for (int i = 0; i < this.collisions.size() && i < this.MAX_HITS; i++) {
            collisionsArray[i] = this.collisions.get(this.collisions.size() - 1 - i);
        }
        this.material.setParam("Collisions", VarType.Vector3Array, collisionsArray);
        this.material.setInt("CollisionNum", Math.min(this.collisions.size(), this.MAX_HITS));
    }

    @Override
    public void read(final JmeImporter arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(final JmeExporter arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public Control cloneForSpatial(final Spatial spatial) {
        return null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void render(final RenderManager rm, final ViewPort vp) {
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setSpatial(final Spatial model) {
        this.model = model;
        model.setMaterial(this.material);
        model.setQueueBucket(Bucket.Transparent);

    }

}
