package com.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

public class TestLightBlowMultiplyColorSystem extends SimpleApplication {

    public static void main(final String[] args) {
        final TestLightBlowMultiplyColorSystem app = new TestLightBlowMultiplyColorSystem();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        assetManager.registerLocator("assets", FileLocator.class);

        final TextureKey skyhi = new TextureKey("TestTextures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        skyhi.setAsCube(true);

        // TextureKey skylow = new TextureKey("TestTextures/Water32.dds", true);
        // skylow.setGenerateMips(true);
        // skylow.setAsCube(true);
        final Texture texlow = this.assetManager.loadTexture(skyhi);
        this.rootNode.attachChild(SkyFactory.createSky(this.assetManager, texlow, false));

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/MultiplyColor_System/MultiplyColor_Base.j3m");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        this.rootNode.attachChild(char_boy);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/MultiplyColor_System/MultiplyColor_1.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(-2f, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        this.rootNode.attachChild(char_boy2);

        final Spatial char_boy3 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat3 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/MultiplyColor_System/MultiplyColor_2.j3m");
        char_boy3.setMaterial(mat3);
        char_boy3.setLocalTranslation(-4f, 0, 0);
        TangentBinormalGenerator.generate(char_boy3);
        this.rootNode.attachChild(char_boy3);

        final Spatial char_boy4 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat4 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/MultiplyColor_System/MultiplyColor_3.j3m");
        char_boy4.setMaterial(mat4);
        char_boy4.setLocalTranslation(-6f, 0, 0);
        TangentBinormalGenerator.generate(char_boy4);
        this.rootNode.attachChild(char_boy4);

        this.flyCam.setMoveSpeed(5);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        this.rootNode.addLight(al);

    }

}
