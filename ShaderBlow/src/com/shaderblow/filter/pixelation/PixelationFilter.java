package com.shaderblow.filter.pixelation;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;


/**
 * Pixelation post processing filter.
 * 
 * @author Cord Rehn - jordansg57@gmail.com
 */
public class PixelationFilter extends Filter {
	
	
	private float pixelWidth = 20, pixelHeight = 20;
	private float screenWidth, screenHeight;
	
	
	
	public PixelationFilter() {
		super("PixelationFilter");
	}
	

	
	/**
	 * @see com.jme3.post.Filter#initFilter(com.jme3.asset.AssetManager, com.jme3.renderer.RenderManager, com.jme3.renderer.ViewPort, int, int)
	 */
	@Override
	protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp, final int w, final int h) {
		material = new Material(manager, "ShaderBlow/MatDefs/Filters/Pixelation/Pixelation.j3md");
		
		screenWidth = w;
		screenHeight = h;
		
		// set the material parameters to whatever the fields actually are
		// since this initFilter() method doesnt get called
		// til right before the first render, but by then every AppStates'
		// update() methods have run once and if we are changing these
		// parameters in that time, they have already changed but since the material was null
		// at that point the material's value wasn't set!... just trust me on this one...
		setPixelWidth(pixelWidth);
		setPixelHeight(pixelHeight);
	}

	
	
	public float getPixelWidth() { return pixelWidth; }
	public void setPixelWidth(float x) {
		pixelWidth = x;
		
		if (material != null)
			material.setFloat("ResX", pixelWidth / screenWidth);
	}
	
	
	
	public float getPixelHeight() { return pixelHeight; }
	public void setPixelHeight(float y) {
		pixelHeight = y;
		
		if (material != null)
			material.setFloat("ResY", pixelHeight / screenHeight);
	}
	
	
	
	/**
	 * @see com.jme3.post.Filter#getMaterial()
	 */
	@Override
	protected Material getMaterial() { return material; }
}
