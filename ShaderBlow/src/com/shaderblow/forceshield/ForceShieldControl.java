package com.shaderblow.forceshield;

import java.io.IOException;
import java.util.ArrayList;


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

public class ForceShieldControl implements Control {

    private Material material;
    private float maxTime;
    private ArrayList<Vector3f> collisions;
    private ArrayList<Float> collisionTimes;
    private Spatial model;
    private boolean numChanged;
    private boolean enabled;
    private boolean work;
    private float timer;
    private float timerSize;
    private int MAX_HITS;

    public ForceShieldControl(Material material) {

        this.material = material;

        numChanged = false;
        enabled = true;
        work = false;
        maxTime = 0.4f;
        timerSize = maxTime * 3f;
        collisionTimes = new ArrayList<Float>();
        collisions = new ArrayList<Vector3f>();
        MAX_HITS = 4;
        timer = 0;
    }

    public void registerHit(final Vector3f position) {
        if (!this.enabled) {
            return;
        }

        timer = 0f;
        material.setBoolean("Work", true);
        work = true;

        Vector3f lposition = new Vector3f();
        model.worldToLocal(position.clone(), lposition);
        collisions.add(new Vector3f(lposition.x, lposition.y, lposition.z));
        collisionTimes.add(maxTime);
        numChanged = true;
        updateCollisionPoints();
    }

    public void setColor(final ColorRGBA color) {
        this.material.setColor("Color", color);
    }


    public void setVisibility(final float percent) {
        this.material.setFloat("MinAlpha", percent);
    }

    public void setTexture(final Texture texture) {
        this.material.setTexture("ColorMap", texture);
    }

    public Material getMaterial() {
        return this.material;
    }

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
    //    this.material.setInt("CollisionNum", Math.min(this.collisions.size(), this.MAX_HITS));
    }

    public float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
        timerSize = maxTime * 3f;
    }
   
    @Override
    public void update(final float tpf) {
        if (this.work && this.enabled) {

            if (this.timer > this.timerSize) {
                collisions.clear();
                collisionTimes.clear();
                numChanged = false;
                material.setBoolean("Work", false);
                timer = 0f;
                work = false;
                return;
            }

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
            
            this.timer += tpf * 3f;
            updateCollisionAlpha();

            this.numChanged = false;
            
        }
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
