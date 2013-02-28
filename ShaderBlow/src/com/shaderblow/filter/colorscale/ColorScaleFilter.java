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
package com.shaderblow.filter.colorscale;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * The ColorScale filter applys a color to the render image. You can use this filter to tint the render according to one
 * particular color without change any material (underwater scene, night scene, fire scene) or to achieve
 * fade-in/fade-out effect.
 * 
 * <pre>
 * Features:
 * 	- Allow to set the color to apply. Default is red.
 * 	- Allow to set intensity of the color. Default is 0.7f. Frag shader clamps color intensity between 0 and 1.
 * </pre>
 * 
 * @author H
 */
public class ColorScaleFilter extends Filter {

	/** Default values */
	private static final ColorRGBA DEAFULT_COLOR = ColorRGBA.Red.clone();
	private static final float DEFAULT_DENSITY = 0.7f;

	private ColorRGBA filterColor = null;
	private float colorDensity = 0f;

	/**
	 * Default Constructor.
	 */
	public ColorScaleFilter() {
		this(ColorScaleFilter.DEAFULT_COLOR, ColorScaleFilter.DEFAULT_DENSITY);
	}

	/**
	 * Constructor.
	 * 
	 * @param filterColor
	 *            Allow to set the color to apply. Default is red.
	 * @param colorDensity
	 *            Allow to set intensity of the color. Frag shader clamps color intensity between 0 and 1.
	 */
	public ColorScaleFilter(final ColorRGBA filterColor, final float colorDensity) {
		super("ColorScaleFilter");
		this.filterColor = filterColor;
		this.colorDensity = colorDensity;
	}

	/**
	 * @see com.jme3.post.Filter#getMaterial()
	 */
	@Override
	protected Material getMaterial() {
		return this.material;
	}

	public void setOverlay(boolean overlay) {
		this.material.setBoolean("Overlay", overlay);
	}

	public void setMultiply(boolean multiply) {
		this.material.setBoolean("Multiply", multiply);
	}

	/**
	 * @see com.jme3.post.Filter#initFilter(com.jme3.asset.AssetManager, com.jme3.renderer.RenderManager,
	 *      com.jme3.renderer.ViewPort, int, int)
	 */
	@Override
	protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp,
			final int w, final int h) {
		this.material = new Material(manager, "ShaderBlow/MatDefs/Filters/ColorScale/ColorScale.j3md");
		this.material.setColor("FilterColor", this.filterColor);
		this.material.setFloat("ColorDensity", this.colorDensity);
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

}
