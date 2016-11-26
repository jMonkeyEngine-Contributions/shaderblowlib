package com.shaderblow.test.bubble;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

/**
 * @ERRORAT 46	    vNdotV = dot(inNormal, viewVec);
 * @ERROR SEVERE: Uncaught exception thrown in Thread[jME3 Main,5,main] com.jme3.renderer.RendererException: compile error in: ShaderSource[name=ShaderBlow/Shaders/Bubble/Bubble.vert, defines, type=Vertex, language=GLSL100] 0(46) : error C7011: implicit cast from "float" to "vec3" 
 */
public class Bubble extends SimpleApplication {
    
    public static void main(String[] args) {
        Bubble app = new Bubble();
        app.start();
    }
    

    @Override
    public void simpleInitApp() {
        Sphere b = new com.jme3.scene.shape.Sphere(200, 200, 1);
        
        Geometry geom = new Geometry("Sphere", b);

        this.assetManager.registerLocator("assets", FileLocator.class);          
        this.assetManager.registerLocator("test-data", FileLocator.class);
        
        Material mat = new Material(assetManager,"ShaderBlow/MatDefs/Bubble/Bubble.j3md"); //"Common/MatDefs/Misc/Unshaded.j3md");//"MatDefs/bubbleMat.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("TestTextures/Bubble/rainbow.png"));
        mat.setFloat("Shininess", 20f);
        mat.setColor("SpecularColor", ColorRGBA.Blue);
        mat.setBoolean("UseSpecularNormal", true);
       
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        rootNode.attachChild(geom);
        rootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        
        flyCam.setEnabled(false);
        ChaseCamera chaseCam = new ChaseCamera(cam, geom, inputManager);
        
       
    }
    
    
    @Override
    public void simpleUpdate(float tpf) {
        
    }
       
}

