package com.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public class TestLinearVsSRGBLighting extends SimpleApplication {

    public static void main(final String[] args) {
        final TestLinearVsSRGBLighting app = new TestLinearVsSRGBLighting();
        app.start();
    }

    public void Models() {
        
        assetManager.registerLocator("assets", FileLocator.class);

        // Material
        final Material mat = this.assetManager.loadMaterial("TestMaterials/LightBlow/Linear_SRGB/Linear.j3m");
        final Material mat2 = this.assetManager.loadMaterial("TestMaterials/LightBlow/Linear_SRGB/SRGB.j3m");

        final Mesh sph_test = new Sphere(20, 20, 5);
        final Geometry geo_test = new Geometry("geo_test", sph_test);
        TangentBinormalGenerator.generate(geo_test);
        geo_test.setLocalTranslation(0, 0, -20);
        geo_test.rotate(1.6f, 0, 0);
        
        Geometry geo2 = geo_test.clone();
        geo2.move(10,0,0);
        
        geo_test.setMaterial(mat);
        geo2.setMaterial(mat2);
        
        this.rootNode.attachChild(geo_test);
        this.rootNode.attachChild(geo2);

        


    }

    @Override
    public void simpleInitApp() {

        Models();

        // Add a light Source
        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1));
        this.rootNode.addLight(dl);

        this.flyCam.setMoveSpeed(40);
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);

    }

}
