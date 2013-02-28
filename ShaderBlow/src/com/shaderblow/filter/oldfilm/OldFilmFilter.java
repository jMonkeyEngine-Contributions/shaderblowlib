package com.shaderblow.filter.oldfilm;

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

public class OldFilmFilter extends Filter {

	private static final String M_PARAM_NAME_RANDOM_VALUE = "RandomValue";
	private static final String M_PARAM_NAME_OUTER_VIGNETTING = "OuterVignetting";
	private static final String M_PARAM_NAME_INNER_VIGNETTING = "InnerVignetting";
	private static final String M_PARAM_NAME_SCRATCH_DENSITY = "ScratchDensity";
	private static final String M_PARAM_NAME_NOISE_DENSITY = "NoiseDensity";
	private static final String M_PARAM_NAME_COLOR_DENSITY = "ColorDensity";
	private static final String M_PARAM_NAME_FILTER_COLOR = "FilterColor";
	
	private static final ColorRGBA DEAFULT_COLOR = new ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f);
	private static final float DEFAULT_COLOR_DENSITY = 0.7f;
	private static final float DEFAULT_NOISE_DENSITY = 0.4f;
	private static final float DEFAULT_SCRATCH_DENSITY = 0.3f;
	private static final float DEFAULT_VIGNETTING_VALUE = 0.9f;
	private static final float DEFAULT_VIGNETTING_FADE_REGION_WIDTH = 0.4f;

	private ColorRGBA filterColor = null;
	private float colorDensity = 0f;
	private float noiseDensity = 0f;
	private float scratchDensity = 0f;
	private float vignettingValue = 0f;
	private Random randomGenerator = null;

	public OldFilmFilter() {
		this(OldFilmFilter.DEAFULT_COLOR, OldFilmFilter.DEFAULT_COLOR_DENSITY, OldFilmFilter.DEFAULT_NOISE_DENSITY,
				OldFilmFilter.DEFAULT_SCRATCH_DENSITY, OldFilmFilter.DEFAULT_VIGNETTING_VALUE);
	}

	public OldFilmFilter(final ColorRGBA filterColor, final float colorDensity, final float noiseDensity,
			final float scratchDensity, final float vignettingValue) {
		super("OldFilmFilter");
		this.filterColor = filterColor;
		this.colorDensity = colorDensity;
		this.noiseDensity = noiseDensity;
		this.scratchDensity = scratchDensity;
		this.vignettingValue = vignettingValue;
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
		this.material.setColor(M_PARAM_NAME_FILTER_COLOR, this.filterColor);
		this.material.setFloat(M_PARAM_NAME_COLOR_DENSITY, this.colorDensity);
		this.material.setFloat(M_PARAM_NAME_NOISE_DENSITY, this.noiseDensity);
		this.material.setFloat(M_PARAM_NAME_SCRATCH_DENSITY, this.scratchDensity);
		this.material.setFloat(M_PARAM_NAME_INNER_VIGNETTING, this.vignettingValue - DEFAULT_VIGNETTING_FADE_REGION_WIDTH);
		this.material.setFloat(M_PARAM_NAME_OUTER_VIGNETTING, this.vignettingValue);
		this.material.setFloat(M_PARAM_NAME_RANDOM_VALUE, this.randomGenerator.nextFloat());
	}

	@Override
	protected void preFrame(final float tpf) {
		this.material.setFloat(M_PARAM_NAME_RANDOM_VALUE, this.randomGenerator.nextFloat());
	}

	public void setColorDensity(final float colorDensity) {
		if (this.material != null) {
			this.material.setFloat(M_PARAM_NAME_COLOR_DENSITY, colorDensity);
			this.colorDensity = colorDensity;
		}
	}

	public float getColorDensity() {
		return this.colorDensity;
	}

	public void setFilterColor(final ColorRGBA filterColor) {
		if (this.material != null) {
			this.material.setColor(M_PARAM_NAME_FILTER_COLOR, filterColor);
			this.filterColor = filterColor;
		}
	}

	public ColorRGBA getFilterColor() {
		return this.filterColor;
	}

	public void setNoiseDensity(final float noiseDensity) {
		if (this.material != null) {
			this.material.setFloat(M_PARAM_NAME_NOISE_DENSITY, noiseDensity);
			this.noiseDensity = noiseDensity;
		}
	}

	public float getNoiseDensity() {
		return this.noiseDensity;
	}

	public void setScratchDensity(final float scratchDensity) {
		if (this.material != null) {
			this.material.setFloat(M_PARAM_NAME_SCRATCH_DENSITY, scratchDensity);
			this.scratchDensity = scratchDensity;
		}
	}

	public float getScratchDensity() {
		return this.scratchDensity;
	}
	
	public void setVignettingValue(float vignettingValue) {
		if (this.material != null) {
			this.material.setFloat(M_PARAM_NAME_INNER_VIGNETTING, vignettingValue - DEFAULT_VIGNETTING_FADE_REGION_WIDTH);
			this.material.setFloat(M_PARAM_NAME_OUTER_VIGNETTING, vignettingValue);
			this.vignettingValue = vignettingValue;
		}
	}
	
	public float getVignettingValue() {
		return vignettingValue;
	}
}
