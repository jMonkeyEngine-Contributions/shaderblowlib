package com.shaderblow.filter.oldfilm;

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

public class OldFilmFilter extends Filter {

    private static final ColorRGBA DEAFULT_COLOR = ColorRGBA.Red.clone();
    private static final float DEFAULT_COLOR_DENSITY = 0.7f;
    private static final float DEFAULT_NOISE_DENSITY = 0.4f;
    private static final float DEFAULT_SCRATCH_DENSITY = 0.3f;

    private ColorRGBA filterColor = null;
    private float colorDensity = 0f;
    private float noiseDensity = 0f;
    private float scratchDensity = 0f;
    private Random randomGenerator = null;

    public OldFilmFilter() {
        this(OldFilmFilter.DEAFULT_COLOR, OldFilmFilter.DEFAULT_COLOR_DENSITY, OldFilmFilter.DEFAULT_NOISE_DENSITY,
                OldFilmFilter.DEFAULT_SCRATCH_DENSITY);
    }

    public OldFilmFilter(final ColorRGBA filterColor, final float colorDensity, final float noiseDensity,
            final float scratchDensity) {
        super("OldFilmFilter");
        this.filterColor = filterColor;
        this.colorDensity = colorDensity;
        this.noiseDensity = noiseDensity;
        this.scratchDensity = scratchDensity;
        this.randomGenerator = new Random();
    }

    @Override
    protected Material getMaterial() {
        return this.material;
    }

    @Override
    protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp,
            final int w, final int h) {
        this.material = new Material(manager, "ShaderBlow/MatDefs/Filters/OldFilm/OldFilm.j3md");
        this.material.setColor("FilterColor", this.filterColor);
        this.material.setFloat("ColorDensity", this.colorDensity);
        this.material.setFloat("NoiseDensity", this.noiseDensity);
        this.material.setFloat("ScratchDensity", this.scratchDensity);
        this.material.setFloat("RandomValue", this.randomGenerator.nextFloat());
    }

    @Override
    protected void preFrame(final float tpf) {
        this.material.setFloat("RandomValue", this.randomGenerator.nextFloat());
    }

    public void setColorDensity(final float colorDensity) {
        if (this.material != null) {
            this.material.setFloat("ColorDensity", this.colorDensity);
            this.colorDensity = colorDensity;
        }
    }

    public float getColorDensity() {
        return this.colorDensity;
    }

    public void setFilterColor(final ColorRGBA filterColor) {
        if (this.material != null) {
            this.material.setColor("FilterColor", this.filterColor);
            this.filterColor = filterColor;
        }
    }

    public ColorRGBA getFilterColor() {
        return this.filterColor;
    }

    public void setNoiseDensity(final float noiseDensity) {
        if (this.material != null) {
            this.material.setFloat("NoiseDensity", this.noiseDensity);
            this.noiseDensity = noiseDensity;
        }
    }

    public float getNoiseDensity() {
        return this.noiseDensity;
    }

    public void setScratchDensity(final float scratchDensity) {
        if (this.material != null) {
            this.material.setFloat("ScratchDensity", this.scratchDensity);
            this.scratchDensity = scratchDensity;
        }
    }

    public float getScratchDensity() {
        return this.scratchDensity;
    }
}
