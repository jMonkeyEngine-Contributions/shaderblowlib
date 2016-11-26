package com.shaderblow.test.simplesprite;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;

/**
 * TODO working? just a gray screen?
 */
public class TestSimpleSprite extends SimpleApplication {

    public static void main(final String[] args) {
        final TestSimpleSprite app = new TestSimpleSprite();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          

        final Spatial char_boy1 = this.assetManager.loadModel("TestModels/SimpleSprite/SimpleSprite.blend");
        final Material mat1 = this.assetManager.loadMaterial("TestMaterials/SimpleSprite/SimpleSprite_1.j3m");
        char_boy1.setMaterial(mat1);
        char_boy1.setLocalTranslation(0, 0, 0);
        TangentBinormalGenerator.generate(char_boy1);
        this.rootNode.attachChild(char_boy1);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/SimpleSprite/SimpleSprite.blend");
        final Material mat2 = this.assetManager.loadMaterial("TestMaterials/SimpleSprite/SimpleSprite_2.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(1, 0, 0);
        char_boy2.setLocalScale(0.5f, 1, 1);
        TangentBinormalGenerator.generate(char_boy2);
        this.rootNode.attachChild(char_boy2);

        // Spatial char_boy3 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.obj");
        // Material mat3 = assetManager.loadMaterial("TestMaterials/MatCap/MatCap2.j3m");
        // char_boy3.setMaterial(mat3);
        // char_boy3.setLocalTranslation(-1,0,0);
        // TangentBinormalGenerator.generate(char_boy3);
        // rootNode.attachChild(char_boy3);
        //
        // Spatial char_boy4 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.obj");
        // Material mat4 = assetManager.loadMaterial("TestMaterials/MatCap/MatCapBump2.j3m");
        // char_boy4.setMaterial(mat4);
        // char_boy4.setLocalTranslation(-2,0,0);
        // TangentBinormalGenerator.generate(char_boy4);
        // rootNode.attachChild(char_boy4);

        this.flyCam.setMoveSpeed(10);
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);

    }

}
