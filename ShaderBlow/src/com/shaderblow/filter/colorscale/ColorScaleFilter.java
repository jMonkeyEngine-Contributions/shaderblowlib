package com.shaderblow.filter.colorscale;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

public class ColorScaleFilter extends Filter {

    private static final ColorRGBA DEAFULT_COLOR = ColorRGBA.Red.clone();
    private static final float DEFAULT_DENSITY = 0.7f;

    private ColorRGBA filterColor = null;
    private float colorDensity = 0f;

    public ColorScaleFilter() {
        this(ColorScaleFilter.DEAFULT_COLOR, ColorScaleFilter.DEFAULT_DENSITY);
    }

    public ColorScaleFilter(final ColorRGBA filterColor, final float colorDensity) {
        super("ColorScaleFilter");
        this.filterColor = filterColor;
        this.colorDensity = colorDensity;
    }

    @Override
    protected Material getMaterial() {
        return this.material;
    }
    
    public Material getTheMaterial() {
        return this.material;
    }    

    public void setOverlay(boolean overlay) {
        this.material.setBoolean("Overlay", overlay);
    }     

    public void setMultiply(boolean multiply) {
        this.material.setBoolean("Multiply", multiply);
    }         
    
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
