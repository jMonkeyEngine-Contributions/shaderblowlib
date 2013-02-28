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
package com.shaderblow.filter.oldfilm;

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Old Film filter simulate the effect of a classic looking film effect. It's a port of
 * http://devmaster.net/posts/2989/shader-effects-old-film.
 * 
 * <pre>
 * Features:
 * 	- Allow to set the <strong>filter's color</strong>. Default is sepia 
 * 	(ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f)).
 * 	- Allow to set the <strong>color's density</strong>. Default is 0.7. Shader clamps this value between 0 to 1.
 * 	The color image gets grayscale when color's densite is set to 0.
 * 	- Allow to set the <strong>noise's density</strong>. Default is 0.4. Shader clamps this value between 0 to 1.
 * 	- Allow to set the <strong>scratches' density</strong>. Default is 0.3. Shader clamps this value between 0 to 1.
 * 	- Allow to set the <strong>vignetting's diameter</strong>. Default is 0.9. Shader clamps this value between 0 to 
 * 	1.4. Vignetting effect is made using two circles. The inner circle represents the region untouched by vignetting.
 * 	The region between the inner and outer circle represent the area where vignetting starts to take place, which is 
 * 	a gradual fade to black from the inner to outer ring. Any part of the frame outside of the outer ring would be 
 * 	completely black.
 * </pre>
 * 
 * <strong>NOTE</strong>: I chose to clamp this value inside the frag shader code instead of using java code because I
 * thought this way is faster (better from preformace point of view). You can clamp this values using java code if you
 * want.
 * 
 * @author H
 */
public class OldFilmFilter extends Filter {

	/** Material parameter's name constants */
	private static final String M_PARAM_NAME_RANDOM_VALUE = "RandomValue";
	private static final String M_PARAM_NAME_OUTER_VIGNETTING = "OuterVignetting";
	private static final String M_PARAM_NAME_INNER_VIGNETTING = "InnerVignetting";
	private static final String M_PARAM_NAME_SCRATCH_DENSITY = "ScratchDensity";
	private static final String M_PARAM_NAME_NOISE_DENSITY = "NoiseDensity";
	private static final String M_PARAM_NAME_COLOR_DENSITY = "ColorDensity";
	private static final String M_PARAM_NAME_FILTER_COLOR = "FilterColor";

	/** Default values */
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

	/**
	 * Default Constructor.
	 */
	public OldFilmFilter() {
		this(OldFilmFilter.DEAFULT_COLOR, OldFilmFilter.DEFAULT_COLOR_DENSITY, OldFilmFilter.DEFAULT_NOISE_DENSITY,
				OldFilmFilter.DEFAULT_SCRATCH_DENSITY, OldFilmFilter.DEFAULT_VIGNETTING_VALUE);
	}

	/**
	 * Constructor.
	 * 
	 * @param filterColor
	 *            Allow to set the <strong>filter's color</strong>.
	 * @param colorDensity
	 *            Allow to set the <strong>color's density</strong>. Shader clamps this value between 0 to 1. The color
	 *            image gets grayscale when color's densite is set to 0.
	 * @param noiseDensity
	 *            Allow to set the <strong>noise's density</strong>. Shader clamps this value between 0 to 1.
	 * @param scratchDensity
	 *            Allow to set the <strong>scratches' density</strong>. Shader clamps this value between 0 to 1.
	 * @param vignettingValue
	 *            Allow to set the <strong>vignetting's diameter</strong>. Shader clamps this value between 0 to 1.4.
	 *            Vignetting effect is made using two circles. The inner circle represents the region untouched by
	 *            vignetting. The region between the inner and outer circle represent the area where vignetting starts
	 *            to take place, which is a gradual fade to black from the inner to outer ring. Any part of the frame
	 *            outside of the outer ring would be completely black.
	 */
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

	/**
	 * @see com.jme3.post.Filter#getMaterial()
	 */
	@Override
	protected Material getMaterial() {
		return this.material;
	}

	/**
	 * @see com.jme3.post.Filter#initFilter(com.jme3.asset.AssetManager, com.jme3.renderer.RenderManager,
	 *      com.jme3.renderer.ViewPort, int, int)
	 */
	@Override
	protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp,
			final int w, final int h) {
		this.material = new Material(manager, "ShaderBlow/MatDefs/Filters/OldFilm/OldFilm.j3md");
		this.material.setColor(M_PARAM_NAME_FILTER_COLOR, this.filterColor);
		this.material.setFloat(M_PARAM_NAME_COLOR_DENSITY, this.colorDensity);
		this.material.setFloat(M_PARAM_NAME_NOISE_DENSITY, this.noiseDensity);
		this.material.setFloat(M_PARAM_NAME_SCRATCH_DENSITY, this.scratchDensity);
		this.material.setFloat(M_PARAM_NAME_INNER_VIGNETTING, this.vignettingValue
				- DEFAULT_VIGNETTING_FADE_REGION_WIDTH);
		this.material.setFloat(M_PARAM_NAME_OUTER_VIGNETTING, this.vignettingValue);
		this.material.setFloat(M_PARAM_NAME_RANDOM_VALUE, this.randomGenerator.nextFloat());
	}

	/**
	 * @see com.jme3.post.Filter#preFrame(float)
	 */
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
			this.material.setFloat(M_PARAM_NAME_INNER_VIGNETTING, vignettingValue
					- DEFAULT_VIGNETTING_FADE_REGION_WIDTH);
			this.material.setFloat(M_PARAM_NAME_OUTER_VIGNETTING, vignettingValue);
			this.vignettingValue = vignettingValue;
		}
	}

	public float getVignettingValue() {
		return vignettingValue;
	}
}
