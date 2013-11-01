/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaderblow.simplerefraction;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

/**
 * 
 * Simple Refraction renders a simple geometry that use refraction to look like an air effect. 
 * 
 * @author Normen Hansen & Rémy Bouquet
 */
public class SimpleRefractionProcessor implements SceneProcessor {

    protected RenderManager rm;
    protected ViewPort vp;
    protected Spatial reflectionScene;
    // protected ViewPort reflectionView;
    protected ViewPort refractionView;
    // protected FrameBuffer reflectionBuffer;
    protected FrameBuffer refractionBuffer;
    // protected Camera reflectionCam;
    protected Camera refractionCam;
    protected Texture2D reflectionTexture;
    protected Texture2D refractionTexture;
    protected Texture2D depthTexture;
    protected Texture2D normalTexture;
    protected Texture2D dudvTexture;
    protected int renderWidth = 512;
    protected int renderHeight = 512;
    protected Plane plane = new Plane(Vector3f.UNIT_Y, Vector3f.ZERO.dot(Vector3f.UNIT_Y));
    protected float speed = 0.05f;
    protected Ray ray = new Ray();
    protected Vector3f targetLocation = new Vector3f();
    protected AssetManager manager;
    protected Material material;
    protected float waterDepth = 1;
    protected float waterTransparency = 0.5f;
    protected boolean debug = false;
    private Picture dispRefraction;
    // private Picture dispReflection;
    private Picture dispDepth;
    float realTpf = 0;
   
    // private Plane reflectionClipPlane;
    // private Plane refractionClipPlane;
    // private float refractionClippingOffset = 100f;
    // private float reflectionClippingOffset = -5f;
    // private Vector3f vect1 = new Vector3f();
    // private Vector3f vect2 = new Vector3f();
    // private Vector3f vect3 = new Vector3f();

    /**
     * Creates a SimpleWaterProcessor
     * 
     * @param manager
     *            the asset manager
     */
    public SimpleRefractionProcessor(final AssetManager manager) {
        this.manager = manager;
        this.material = new Material(manager, "ShaderBlow/MatDefs/SimpleRefraction/SimpleRefraction.j3md");
        this.material.setFloat("waterTransparency", this.waterTransparency / 10);


        this.material.setFloat("distortionScale", 0.2f);
        this.material.setFloat("distortionMix", 0.5f);
        this.material.setFloat("texScale", 1.0f);
        // updateClipPlanes();

    }

    @Override
    public void initialize(final RenderManager rm, final ViewPort vp) {
        this.rm = rm;
        this.vp = vp;

        loadTextures(this.manager);
        createTextures();
        applyTextures(this.material);
//        this.material.setFloat("timeFlow", 0.5f);        
        
        createPreViews();

        if (this.debug) {
            this.dispRefraction = new Picture("dispRefraction");
            this.dispRefraction.setTexture(this.manager, this.refractionTexture, false);
            this.dispDepth = new Picture("depthTexture");
            this.dispDepth.setTexture(this.manager, this.depthTexture, false);
        }
    }

    @Override
    public void reshape(final ViewPort vp, final int w, final int h) {
    }

    @Override
    public boolean isInitialized() {
        return this.rm != null;
    }

    @Override
    public void preFrame(final float tpf) {
        this.realTpf = tpf;
    }

    @Override
    public void postQueue(final RenderQueue rq) {
        
        final Camera sceneCam = this.rm.getCurrentCamera();

        this.refractionCam.setLocation(sceneCam.getLocation());
        this.refractionCam.setRotation(sceneCam.getRotation());
        this.refractionCam.setFrustum(sceneCam.getFrustumNear(), sceneCam.getFrustumFar(), sceneCam.getFrustumLeft(),
        sceneCam.getFrustumRight(), sceneCam.getFrustumTop(), sceneCam.getFrustumBottom());
        this.refractionCam.setParallelProjection(false);

        this.rm.renderViewPort(this.refractionView, realTpf);
        this.rm.getRenderer().setFrameBuffer(this.vp.getOutputFrameBuffer());
        this.rm.setCamera(sceneCam, false);

        this.rm.getRenderer().clearBuffers(true, true, true);   
     
        
//        this.rm.setForcedTechnique("Simple_Refraction");
//        this.rm.renderViewPortQueues(this.vp, false);        
//        this.rm.setForcedTechnique(null);    
    
        
    }

    @Override
    public void postFrame(final FrameBuffer out) {
        if (this.debug) {
            displayMap(this.rm.getRenderer(), this.dispRefraction, 64);
            displayMap(this.rm.getRenderer(), this.dispDepth, 256);
        }
    }

    @Override
    public void cleanup() {
    }

    // debug only : displays maps
    protected void displayMap(final Renderer r, final Picture pic, final int left) {
        final Camera cam = this.vp.getCamera();
        this.rm.setCamera(cam, true);
        final int h = cam.getHeight();

        pic.setPosition(left, h / 20f);

        pic.setWidth(128);
        pic.setHeight(128);
        pic.updateGeometricState();
        this.rm.renderGeometry(pic);
        this.rm.setCamera(cam, false);
    }

    protected void loadTextures(final AssetManager manager) {
        this.normalTexture = (Texture2D) manager.loadTexture("TestTextures/Refraction/water_normalmap.png");
        this.normalTexture.setWrap(WrapMode.Repeat);
        this.normalTexture.setMagFilter(MagFilter.Bilinear);
        this.normalTexture.setMinFilter(MinFilter.BilinearNearestMipMap);

        this.dudvTexture = (Texture2D) manager.loadTexture("TestTextures/Refraction/dudv_map.png");
        this.dudvTexture.setMagFilter(MagFilter.Bilinear);
        this.dudvTexture.setMinFilter(MinFilter.BilinearNearestMipMap);
        this.dudvTexture.setWrap(WrapMode.Repeat);
    }

    protected void createTextures() {
        this.refractionTexture = new Texture2D(this.renderWidth, this.renderHeight, Format.RGBA8);
        this.depthTexture = new Texture2D(this.renderWidth, this.renderHeight, Format.Depth);
    }

    protected void applyTextures(final Material mat) {
        mat.setTexture("water_refraction", this.refractionTexture);
        mat.setTexture("water_normalmap", this.normalTexture);
        mat.setTexture("water_dudvmap", this.dudvTexture);
    }

    protected void createPreViews() {

        this.refractionCam = new Camera(this.renderWidth, this.renderHeight);

        // create a pre-view. a view that is rendered before the main view
        this.refractionView = new ViewPort("Refraction View", this.refractionCam);
        this.refractionView.setClearFlags(true, true, true);
        this.refractionView.setBackgroundColor(ColorRGBA.Black);
        // create offscreen framebuffer
        this.refractionBuffer = new FrameBuffer(this.renderWidth, this.renderHeight, 1);
        // setup framebuffer to use texture
        this.refractionBuffer.setDepthBuffer(Format.Depth);
        this.refractionBuffer.setColorTexture(this.refractionTexture);
        this.refractionBuffer.setDepthTexture(this.depthTexture);
        // set viewport to render to offscreen framebuffer
        this.refractionView.setOutputFrameBuffer(this.refractionBuffer);
        // refractionView.addProcessor(new RefractionProcessor());
        // attach the scene to the viewport to be rendered
        this.refractionView.attachScene(this.reflectionScene);
    }

    protected void destroyViews() {
        this.rm.removePreView(this.refractionView);
    }

    /**
     * Get the water material from this processor, apply this to your water quad.
     * 
     * @return
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Sets the reflected scene, should not include the water quad! Set before adding processor.
     * 
     * @param spat
     */
    public void setRefractionScene(final Spatial spat) {
        this.reflectionScene = spat;
    }

    /**
     * returns the width of the reflection and refraction textures
     * 
     * @return
     */
    public int getRenderWidth() {
        return this.renderWidth;
    }

    /**
     * returns the height of the reflection and refraction textures
     * 
     * @return
     */
    public int getRenderHeight() {
        return this.renderHeight;
    }

    /**
     * Set the reflection Texture render size, set before adding the processor!
     * 
     * @param width
     * @param height
     */
    public void setRenderSize(final int width, final int height) {
        this.renderWidth = width;
        this.renderHeight = height;
    }

    /**
     * Higher values make the refraction texture shine through earlier. Default is 4
     * 
     * @param depth
     */
    public void setWaterDepth(final float depth) {
        this.waterDepth = depth;
        this.material.setFloat("waterDepth", depth);
    }

    /**
     * return the water depth
     * 
     * @return
     */
    public float getWaterDepth() {
        return this.waterDepth;
    }

    /**
     * returns water transparency
     * 
     * @return
     */
    public float getWaterTransparency() {
        return this.waterTransparency;
    }

    /**
     * sets the water transparency default os 0.1f
     * 
     * @param waterTransparency
     */
    public void setWaterTransparency(final float waterTransparency) {
        this.waterTransparency = Math.max(0, waterTransparency);
        this.material.setFloat("waterTransparency", waterTransparency / 10);
    }

    /**
     * Sets the speed of the wave animation, default = 0.05f.
     * 
     * @param speed
     */
    public void setWaveSpeed(final float speed) {
        this.speed = speed;
    }

    /**
     * Sets the scale of distortion by the normal map, default = 0.2
     */
    public void setDistortionScale(final float value) {
        this.material.setColor("distortionScale", new ColorRGBA(value, value, value, value));
    }

    /**
     * Sets how the normal and dudv map are mixed to create the wave effect, default = 0.5
     */
    public void setDistortionMix(final float value) {
        this.material.setColor("distortionMix", new ColorRGBA(value, value, value, value));
    }

    /**
     * Sets the scale of the normal/dudv texture, default = 1. Note that the waves should be scaled by the texture
     * coordinates of the quad to avoid animation artifacts, use mesh.scaleTextureCoordinates(Vector2f) for that.
     */
    public void setTexScale(final float value) {
        this.material.setColor("texScale", new ColorRGBA(value, value, value, value));
    }

    /**
     * retruns true if the waterprocessor is in debug mode
     * 
     * @return
     */
    public boolean isDebug() {
        return this.debug;
    }

    /**
     * set to true to display reflection and refraction textures in the GUI for debug purpose
     * 
     * @param debug
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

}
