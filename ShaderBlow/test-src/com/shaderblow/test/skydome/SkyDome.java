/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaderblow.test.skydome;

import com.shaderblow.skydome.SkyDomeControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.shaderblow.test.filter.grayscale.TestGrayScale;

/**
 *
 * @author mifth
 */
public class SkyDome extends SimpleApplication {

    public static void main(final String[] args) {
        final SkyDome app = new SkyDome();
        app.start();
    }    
    
    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          
        
        addGeometry();
        

        SkyDomeControl skyDome = new SkyDomeControl(assetManager, cam,
                "TestModels/SkyDome/SkyDome.j3o",
                "TestTextures/SkyDome/SkyNight_L.png",
                "TestTextures/SkyDome/Moon_L.png",
                "TestTextures/SkyDome/Clouds_L.png",
                "TestTextures/SkyDome/Fog_Alpha.png");
        Node sky = new Node();
        sky.setQueueBucket(Bucket.Sky);
        sky.addControl(skyDome);
        sky.setCullHint(Spatial.CullHint.Never);

// Either add a reference to the control for the existing JME fog filter or use the one I posted…
// But… REMEMBER!  If you use JME’s… the sky dome will have fog rendered over it.
// Sorta pointless at that point
//        FogFilter fog = new FogFilter(ColorRGBA.Blue, 0.5f, 10f);
//        skyDome.setFogFilter(fog, viewPort);

// Set some fog colors… or not (defaults are cool)
        skyDome.setFogColor(ColorRGBA.Blue);
        skyDome.setFogNightColor(new ColorRGBA(0.5f, 0.5f, 1f, 1f));
        skyDome.setDaySkyColor(new ColorRGBA(0.5f, 0.5f, 0.9f, 1f));

// Enable the control to modify the fog filter
        skyDome.setControlFog(true);

// Add the directional light you use for sun… or not
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
        skyDome.setSun(sun);
        
        AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(0.7f,0.7f,1f,1.0f));
        rootNode.addLight(al);          

// Set some sunlight day/night colors… or not
        skyDome.setSunDayLight(new ColorRGBA(1, 1, 1, 1));
        skyDome.setSunNightLight(new ColorRGBA(0.5f, 0.5f, 0.9f, 1f));

// Enable the control to modify your sunlight
        skyDome.setControlSun(true);

// Enable the control
        skyDome.setEnabled(true);
        
// Add the skydome to the root… or where ever
        rootNode.attachChild(sky);

    }

    
    private void addGeometry() {
        
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
       Geometry geom = new Geometry("Box", b);
        geom.updateModelBound();

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        geom.setMaterial(mat);
        rootNode.attachChild(geom);    
        
        for (int i=0; i<100; i++) {
            
            Geometry gm = geom.clone(false);
            gm.setName("instance"+i);
            gm.setLocalTranslation((float) Math.random() * 100.0f - 50f,(float) Math.random() * 10.0f,(float)Math.random() * 100.0f - 50f);
            gm.rotate(0, (float) Math.random() * (float)Math.PI, 0);
            rootNode.attachChild(gm);
            
        }        
        
        
        flyCam.setMoveSpeed(30);
        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.5f, 1f, 1f));         
        
    }    
    
    @Override
    public void simpleUpdate(float tpf) {
    }
}
