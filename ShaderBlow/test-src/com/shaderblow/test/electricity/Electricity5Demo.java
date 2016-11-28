package com.shaderblow.test.electricity;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * @author cvlad
 */
public class Electricity5Demo extends SimpleApplication {

    public static void main(String[] args) {
        Electricity5Demo app = new Electricity5Demo();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
      this.assetManager.registerLocator("assets", FileLocator.class);          
      this.assetManager.registerLocator("test-data", FileLocator.class);          
        
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Electricity5Material mat = new Electricity5Material(assetManager, "ShaderBlow/MatDefs/Electricity/Electricity5_2.j3md");
        mat.setLayers(10);
        mat.setWidth(0.2f);
        mat.setFloat("speed", 0.1f);
        mat.setFloat("thickness", 0.14f);
        Texture noiseTex = assetManager.loadTexture("TestTextures/Electricity/noise.png");
        noiseTex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("noise", noiseTex);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.getAdditionalRenderState().setDepthTest(true);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        mat.setColor("color", ColorRGBA.Blue);
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        

        rootNode.attachChild(man);
        DirectionalLight light = new DirectionalLight();
        light.setColor(new ColorRGBA(0.6f,0.6f,0.6f,0.6f));
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.6f,0.6f,0.6f,0.6f));
        light.setDirection(new Vector3f(-1,-1,-1));
        rootNode.addLight(light);
        rootNode.addLight(ambient);
        
        ((Node)man).setLocalTranslation(new Vector3f(0,-1.5f,5));
        
        getFlyByCamera().setMoveSpeed(getFlyByCamera().getMoveSpeed()*10);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
