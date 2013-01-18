package com.shaderblow.test.electricity;

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
public class Electricity extends SimpleApplication {

    public static void main(String[] args) {
        Electricity app = new Electricity();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        this.assetManager.registerLocator("assets", FileLocator.class);        

        electro_01_2();
        electro_01();
        electro_02();
        electro_02_2();
        electro_03_line1();
        electro_03_line2();
        electro_03_line3();
        electro_05_2();
        electro_04();
        
        
        DirectionalLight light = new DirectionalLight();
        light.setColor(new ColorRGBA(0.6f,0.6f,0.6f,0.6f));
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.6f,0.6f,0.6f,0.6f));
        light.setDirection(new Vector3f(-1,-1,-1));
        rootNode.addLight(light);
        rootNode.addLight(ambient);
        

        
        flyCam.setMoveSpeed(10);
        viewPort.setBackgroundColor(ColorRGBA.Gray);   
    }

    void electro_01_2 () {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity1_2.j3m");
                
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(-1.5f, 0, 0);
        rootNode.attachChild(man);
    }
    
    void electro_01 () {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity1.j3m");
        
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
    }
    
    
    void electro_02() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity2.j3m");
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
     
        man.move(1.5f, 0, 0);
        rootNode.attachChild(man);        
    }

    
    void electro_02_2() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity2_2.j3m");

        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(3f, 0, 0);
        rootNode.attachChild(man);       
    }
    
    
    void electro_03_line1() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity3_line1.j3m");
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(4.5f, 0, 0);

        rootNode.attachChild(man);        
    }
    
     void electro_03_line2() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity3_line2.j3m");
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(6f, 0, 0);

        rootNode.attachChild(man);        
    }
     
     
     void electro_03_line3() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity3_line3.j3m");
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(7.5f, 0, 0);

        rootNode.attachChild(man);        
    }     

     void electro_05_2() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity5_2.j3m");
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(9f, 0, 0);

        rootNode.attachChild(man);        
    }        
    

     void electro_04() {
        Spatial man = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        Material matMan = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        man.setMaterial(matMan);
        
        Material mat = assetManager.loadMaterial("TestMaterials/Electricity/electricity4.j3m");
        
        for (Spatial child : ((Node)man).getChildren()){
            if (child instanceof Geometry){
                Geometry electricity = new Geometry("electrified_" + child.getName());
                electricity.setQueueBucket(Bucket.Transparent);
                electricity.setMesh(((Geometry)child).getMesh());
                electricity.setMaterial(mat);
                ((Node)man).attachChild(electricity);
            }
        }
        
        man.move(10.5f, 0, 0);

        rootNode.attachChild(man);        
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
