package com.shaderblow.filter.basicssao;

import java.io.IOException;
import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.shader.VarType;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;

public class BasicSSAO extends Filter {

    private Pass normalPass;
    private Vector3f frustumCorner;
    private Vector2f frustumNearFar;
    private final Vector3f[] samples = { new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 1.0f),
            new Vector3f(0.0f, 1.0f, 1.0f), new Vector3f(0.0f, -1.0f, 1.0f), new Vector3f(1.0f, 0.0f, 0.0f),
            new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f),
            new Vector3f(1.0f, 0.0f, -1.0f), new Vector3f(-1.0f, 0.0f, -1.0f), new Vector3f(0.0f, 1.0f, -1.0f),
            new Vector3f(0.0f, -1.0f, -1.0f) };
    // Wide area occlusion settings
    private float sampleRadius = 3.0f;
    private float intensity = 10.2f;
    private float scale = 3.15f;
    private float bias = 0.025f;

    // Fine detail occlusion settings
    private boolean enableFD = false;
    private float sampleRadiusFD = 0.55f;
    private float intensityFD = 2.5f;
    private float scaleFD = 1.15f;
    private float biasFD = 0.025f;

    // Distance falloff
    private boolean useDistanceFalloff = false;
    private float falloffStartDistance = 800f, falloffRate = 2.0f;

    private boolean useSmoothing = false;
    private boolean smoothMore = false;
    private boolean useOnlyAo = false;
    private boolean useAo = true;
    private Material ssaoMat;
    private Pass ssaoPass;

    private final float downSampleFactor = 1f;
    RenderManager renderManager;
    ViewPort viewPort;

    /**
     * Create a Screen Space Ambient Occlusion Filter
     */
    public BasicSSAO() {
        super("BasicSSAO");
    }

    /**
     * Create a Screen Space Ambient Occlusion Filter
     * 
     * @param sampleRadius
     *            The radius of the area where random samples will be picked. default 5.1f
     * @param intensity
     *            intensity of the resulting AO. default 1.2f
     * @param scale
     *            distance between occluders and occludee. default 0.2f
     * @param bias
     *            the width of the occlusion cone considered by the occludee. default 0.1f
     */
    public BasicSSAO(final float sampleRadius, final float intensity, final float scale, final float bias) {
        this();
        this.sampleRadius = sampleRadius;
        this.intensity = intensity;
        this.scale = scale;
        this.bias = bias;
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
     protected void postQueue(RenderQueue queue) {
//    protected void postQueue(final RenderManager renderManager, final ViewPort viewPort) {
        final Renderer r = renderManager.getRenderer();
        r.setFrameBuffer(this.normalPass.getRenderFrameBuffer());
        renderManager.getRenderer().clearBuffers(true, true, true);
        renderManager.setForcedTechnique("PreNormalPass");
        renderManager.renderViewPortQueues(viewPort, false);
        renderManager.setForcedTechnique(null);
        renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
    }

    @Override
    protected Material getMaterial() {
        return this.material;
    }

    @Override
    protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp,
            final int w, final int h) {
        this.renderManager = renderManager;
        this.viewPort = vp;

        final int screenWidth = w;
        final int screenHeight = h;
        this.postRenderPasses = new ArrayList<Pass>();

        this.normalPass = new Pass();
        this.normalPass.init(renderManager.getRenderer(), (int) (screenWidth / this.downSampleFactor),
                (int) (screenHeight / this.downSampleFactor), Format.RGBA8, Format.Depth);

        this.frustumNearFar = new Vector2f();

        final float farY = vp.getCamera().getFrustumTop() / vp.getCamera().getFrustumNear()
                * vp.getCamera().getFrustumFar();
        final float farX = farY * ((float) screenWidth / (float) screenHeight);
        this.frustumCorner = new Vector3f(farX, farY, vp.getCamera().getFrustumFar());
        this.frustumNearFar.x = vp.getCamera().getFrustumNear();
        this.frustumNearFar.y = vp.getCamera().getFrustumFar();

        // ssao Pass
        this.ssaoMat = new Material(manager, "ShaderBlow/MatDefs/Filters/BasicSSAO/BasicSSAO.j3md");
        this.ssaoMat.setTexture("Normals", this.normalPass.getRenderedTexture());

        this.ssaoPass = new Pass() {
            @Override
            public boolean requiresDepthAsTexture() {
                return true;
            }
        };
        this.ssaoPass.init(renderManager.getRenderer(), (int) (screenWidth / this.downSampleFactor),
                (int) (screenHeight / this.downSampleFactor), Format.RGBA8, Format.Depth, 1, this.ssaoMat);
        this.ssaoPass.getRenderedTexture().setMinFilter(Texture.MinFilter.Trilinear);
        this.ssaoPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
        this.postRenderPasses.add(this.ssaoPass);

        final float xScale = 1.0f / w;
        final float yScale = 1.0f / h;
        final float blurScale = 6.75f;

        this.material = new Material(manager, "ShaderBlow/MatDefs/Filters/BasicSSAO/BasicSSAOBlur.j3md");
        this.material.setTexture("SSAOMap", this.ssaoPass.getRenderedTexture());
        this.material.setVector2("FrustumNearFar", this.frustumNearFar);
        this.material.setBoolean("UseAo", this.useAo);
        this.material.setBoolean("UseOnlyAo", this.useOnlyAo);
        this.material.setBoolean("UseSmoothing", this.useSmoothing);
        this.material.setBoolean("SmoothMore", this.smoothMore);
        this.material.setFloat("XScale", blurScale * xScale);
        this.material.setFloat("YScale", blurScale * yScale);

        this.ssaoMat.setFloat("SampleRadius", this.sampleRadius);
        this.ssaoMat.setFloat("Intensity", this.intensity);
        this.ssaoMat.setFloat("Scale", this.scale);
        this.ssaoMat.setFloat("Bias", this.bias);

        this.ssaoMat.setBoolean("EnableFD", this.enableFD);
        this.ssaoMat.setFloat("SampleRadiusFD", this.sampleRadiusFD);
        this.ssaoMat.setFloat("IntensityFD", this.intensityFD);
        this.ssaoMat.setFloat("ScaleFD", this.scaleFD);
        this.ssaoMat.setFloat("BiasFD", this.biasFD);

        this.ssaoMat.setBoolean("UseDistanceFalloff", this.useDistanceFalloff);
        this.ssaoMat.setFloat("FalloffStartDistance", this.falloffStartDistance);
        this.ssaoMat.setFloat("FalloffRate", this.falloffRate);

        this.ssaoMat.setVector3("FrustumCorner", this.frustumCorner);
        this.ssaoMat.setVector2("FrustumNearFar", this.frustumNearFar);
        this.ssaoMat.setParam("Samples", VarType.Vector3Array, this.samples);

    }
    
    
    @Override
    protected void cleanUpFilter(Renderer r) {
//        reflectionPass.cleanup(r);
        normalPass.cleanup(r);
        ssaoPass.cleanup(r);
    }

    /**
     * Enables fine detail pass for help in blending out artifacting in the wider area pass without losing detail
     * 
     * @param useDetailPass
     */
    public void setUseDetailPass(final boolean useDetailPass) {
        this.enableFD = useDetailPass;
        if (this.ssaoMat != null) {
            this.ssaoMat.setBoolean("EnableFD", useDetailPass);
        }
    }

    /**
     * Returns the use fine detail setting
     * 
     * @return enableFD
     */
    public boolean getUseDetailPass() {
        return this.enableFD;
    }

    /**
     * Return the wide area bias<br>
     * see {@link #setBias(float bias)}
     * 
     * @return bias
     */
    public float getBias() {
        return this.bias;
    }

    /**
     * Sets the the width of the wide area occlusion cone considered by the occludee default is 0.025f
     * 
     * @param bias
     */
    public void setBias(final float bias) {
        this.bias = bias;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("Bias", bias);
        }
    }

    /**
     * Return the fine detail bias<br>
     * see {@link #setDetailBias(float biasFD)}
     * 
     * @return biasFD
     */
    public float getDetailBias() {
        return this.biasFD;
    }

    /**
     * Sets the the width of the fine detail occlusion cone considered by the occludee default is 0.025f
     * 
     * @param biasFD
     */
    public void setDetailBias(final float biasFD) {
        this.biasFD = biasFD;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("BiasFD", biasFD);
        }
    }

    /**
     * returns the ambient occlusion intensity
     * 
     * @return intensity
     */
    public float getIntensity() {
        return this.intensity;
    }

    /**
     * Sets the Ambient occlusion intensity default is 10.2f
     * 
     * @param intensity
     */
    public void setIntensity(final float intensity) {
        this.intensity = intensity;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("Intensity", intensity);
        }

    }

    /**
     * returns the fine detail ambient occlusion intensity
     * 
     * @return intensityFD
     */
    public float getDetailIntensity() {
        return this.intensityFD;
    }

    /**
     * Sets the fine detail ambient occlusion intensity default is 2.5f
     * 
     * @param intensityFD
     */
    public void setDetailIntensity(final float intensityFD) {
        this.intensityFD = intensityFD;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("IntensityFD", intensityFD);
        }
    }

    /**
     * returns the sample radius<br>
     * see {link setSampleRadius(float sampleRadius)}
     * 
     * @return the sample radius
     */
    public float getSampleRadius() {
        return this.sampleRadius;
    }

    /**
     * Sets the radius of the area where random samples will be picked dafault 3.0f
     * 
     * @param sampleRadius
     */
    public void setSampleRadius(final float sampleRadius) {
        this.sampleRadius = sampleRadius;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("SampleRadius", sampleRadius);
        }

    }

    /**
     * returns the sample radius<br>
     * see {link setDetailSampleRadius(float sampleRadiusFD)}
     * 
     * @return the sample radius for detail pass
     */
    public float getDetailSampleRadius() {
        return this.sampleRadiusFD;
    }

    /**
     * Sets the radius of the area where random samples will be picked for fine detail pass dafault 0.55f
     * 
     * @param sampleRadiusFD
     */
    public void setDetailSampleRadius(final float sampleRadiusFD) {
        this.sampleRadiusFD = sampleRadiusFD;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("SampleRadiusFD", sampleRadiusFD);
        }

    }

    /**
     * returns the scale<br>
     * see {@link #setScale(float scale)}
     * 
     * @return scale
     */
    public float getScale() {
        return this.scale;
    }

    /**
     * 
     * Returns the distance between occluders and occludee. default 3.15f
     * 
     * @param scale
     */
    public void setScale(final float scale) {
        this.scale = scale;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("Scale", scale);
        }
    }

    /**
     * returns the detail pass scale<br>
     * see {@link #setDetailScale(float scaleFD)}
     * 
     * @return scaleFD
     */
    public float getDetailScale() {
        return this.scaleFD;
    }

    /**
     * 
     * Returns the distance between detail pass occluders and occludee. default 1.55f
     * 
     * @param scaleFD
     */
    public void setDetailScale(final float scaleFD) {
        this.scaleFD = scaleFD;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("ScaleFD", scaleFD);
        }
    }

    public void scaleSettings(final float aoScale) {
        setBias(getBias() * aoScale);
        setDetailBias(getDetailBias() * aoScale);
        setIntensity(getIntensity() * aoScale);
        setDetailIntensity(getDetailIntensity() * aoScale);
        setScale(getScale() * aoScale);
        setDetailScale(getDetailScale() * aoScale);
        setScale(getScale() * aoScale);
        setDetailScale(getDetailScale() * aoScale);
        setSampleRadius(getSampleRadius() * aoScale);
        setDetailSampleRadius(getDetailSampleRadius() * aoScale);
    }

    /**
     * debugging only , will be removed
     * 
     * @return Whether or not
     */
    public boolean isUseAo() {
        return this.useAo;
    }

    /**
     * debugging only
     */
    public void setUseAo(final boolean useAo) {
        this.useAo = useAo;
        if (this.material != null) {
            this.material.setBoolean("UseAo", useAo);
        }

    }

    /**
     * debugging only , will be removed
     * 
     * @return useOnlyAo
     */
    public boolean isUseOnlyAo() {
        return this.useOnlyAo;
    }

    /**
     * debugging only
     */
    public void setUseOnlyAo(final boolean useOnlyAo) {
        this.useOnlyAo = useOnlyAo;
        if (this.material != null) {
            this.material.setBoolean("UseOnlyAo", useOnlyAo);
        }
    }

    public void setUseSmoothing(final boolean useSmoothing) {
        this.useSmoothing = useSmoothing;
        if (this.material != null) {
            this.material.setBoolean("UseSmoothing", useSmoothing);
        }
    }

    public boolean isUseSmoothing() {
        return this.useSmoothing;
    }

    public void setSmoothMore(final boolean smoothMore) {
        this.smoothMore = smoothMore;
        if (this.material != null) {
            this.material.setBoolean("SmoothMore", smoothMore);
        }
    }

    public boolean isSmoothMore() {
        return this.smoothMore;
    }

    /**
     * Enable distance falloff
     * 
     * @param useDistanceFalloff
     */
    public void setUseDistanceFalloff(final boolean useDistanceFalloff) {
        this.useDistanceFalloff = useDistanceFalloff;
        if (this.ssaoMat != null) {
            this.ssaoMat.setBoolean("UseDistanceFalloff", useDistanceFalloff);
        }
    }

    /**
     * Returns distance falloff setting
     * 
     * @return useDistanceFalloff
     */
    public boolean getUseDistanceFalloff() {
        return this.useDistanceFalloff;
    }

    /**
     * Sets the start distance for distance falloff. Default is 800f
     * 
     * @param falloffStartDistance
     */
    public void setFalloffStartDistance(final float falloffStartDistance) {
        this.falloffStartDistance = falloffStartDistance;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("FalloffStart", falloffStartDistance);
        }
    }

    /**
     * Returns the start distance for distance falloff.
     * 
     * @return falloffStartDistance
     */
    public float getFalloffStartDistance() {
        return this.falloffStartDistance;
    }

    /**
     * Sets the rate at which distance falloff increases past the start distance. Default is 2.0f
     * 
     * @param falloffRate
     */
    public void setFalloffRate(final float falloffRate) {
        this.falloffRate = falloffRate;
        if (this.ssaoMat != null) {
            this.ssaoMat.setFloat("FalloffAmount", falloffRate);
        }
    }

    /**
     * Returns the rate at which distance falloff increases past the start distance.
     * 
     * @return falloffRate
     */
    public float getFalloffRate() {
        return this.falloffRate;
    }

    /**
     * Used for debugging. toggles between shadowmap, colormap & colormap+shadowmap
     */
    public void toggleSSAO() {
        if (!this.useOnlyAo && this.useAo) { // BasicSSAO Disabled
            this.useOnlyAo = false;
            this.useAo = false;

        } else if (this.useOnlyAo && this.useAo) { // BasicSSAO Map Only
            this.useOnlyAo = false;
            this.useAo = true;
        } else if (!this.useOnlyAo && !this.useAo) { // BasicSSAO Blended
            this.useOnlyAo = true;
            this.useAo = true;
        }
        if (this.material != null) {
            this.material.setBoolean("UseAo", this.useAo);
            this.material.setBoolean("UseOnlyAo", this.useOnlyAo);
        }
    }

    /**
     * Used for debugging. toggles between no smoothing, 1 pass smoothing & 2 pass smoothing
     */
    public void toggleSmoothing() {
        if (this.smoothMore && this.useSmoothing) { // Smoothing Disabled
            this.useSmoothing = false;
            this.smoothMore = false;

        } else if (this.useSmoothing && !this.smoothMore) { // 2 pass Smoothing
            this.useSmoothing = true;
            this.smoothMore = true;
        } else if (!this.useSmoothing && !this.smoothMore) { // 1 pass Smoothing
            this.useSmoothing = true;
            this.smoothMore = false;
        }
        if (this.material != null) {
            this.material.setBoolean("UseSmoothing", this.useSmoothing);
            this.material.setBoolean("SmoothMore", this.smoothMore);
        }
    }

    @Override
    public void write(final JmeExporter ex) throws IOException {
        super.write(ex);

        final OutputCapsule oc = ex.getCapsule(this);
        oc.write(this.sampleRadius, "sampleRadius", 3.0f);
        oc.write(this.intensity, "intensity", 10.2f);
        oc.write(this.scale, "scale", 3.15f);
        oc.write(this.bias, "bias", 0.025f);
        oc.write(this.sampleRadiusFD, "sampleRadiusFD", 0.55f);
        oc.write(this.intensityFD, "intensityFD", 2.5f);
        oc.write(this.scaleFD, "scaleFD", 1.15f);
        oc.write(this.biasFD, "biasFD", 0.025f);
    }

    @Override
    public void read(final JmeImporter im) throws IOException {
        super.read(im);
        final InputCapsule ic = im.getCapsule(this);
        this.sampleRadius = ic.readFloat("sampleRadius", 3.0f);
        this.intensity = ic.readFloat("intensity", 10.2f);
        this.scale = ic.readFloat("scale", 3.15f);
        this.bias = ic.readFloat("bias", 0.025f);
        this.sampleRadiusFD = ic.readFloat("sampleRadiusFD", 0.55f);
        this.intensityFD = ic.readFloat("intensityFD", 2.5f);
        this.scaleFD = ic.readFloat("scaleFD", 1.15f);
        this.biasFD = ic.readFloat("biasFD", 0.025f);
    }
}