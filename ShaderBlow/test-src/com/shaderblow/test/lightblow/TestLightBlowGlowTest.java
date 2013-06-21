package com.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

public class TestLightBlowGlowTest extends SimpleApplication {

    private FilterPostProcessor fpp;

    public static void main(final String[] args) {
        final TestLightBlowGlowTest app = new TestLightBlowGlowTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        assetManager.registerLocator("assets", FileLocator.class);
        
        final TextureKey skyhi = new TextureKey("TestTextures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        skyhi.setAsCube(true);

        final Texture texlow = this.assetManager.loadTexture(skyhi);
        this.rootNode.attachChild(SkyFactory.createSky(this.assetManager, texlow, false));

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat = this.assetManager.loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_ibl.j3m");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        this.rootNode.attachChild(char_boy);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_reflection.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(-1.5f, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        this.rootNode.attachChild(char_boy2);

        final Spatial char_boy2_2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2_2 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_reflection_additive.j3m");
        char_boy2_2.setMaterial(mat2_2);
        char_boy2_2.setLocalTranslation(-2.5f, 0, 0);
        TangentBinormalGenerator.generate(char_boy2_2);
        this.rootNode.attachChild(char_boy2_2);

        final Spatial char_boy3 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat3 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_ref_a_nor.j3m");
        char_boy3.setMaterial(mat3);
        char_boy3.setLocalTranslation(-4f, 0, 0);
        TangentBinormalGenerator.generate(char_boy3);
        this.rootNode.attachChild(char_boy3);

        final Spatial char_boy4 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat4 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_minnaert.j3m");
        char_boy4.setMaterial(mat4);
        char_boy4.setLocalTranslation(-6f, 0, 0);
        TangentBinormalGenerator.generate(char_boy4);
        this.rootNode.attachChild(char_boy4);

        final Spatial char_boy5 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat5 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_rim.j3m");
        char_boy5.setMaterial(mat5);
        char_boy5.setLocalTranslation(-8f, 0, 0);
        TangentBinormalGenerator.generate(char_boy5);
        this.rootNode.attachChild(char_boy5);

        final Spatial char_boy6 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat6 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_rim_2.j3m");
        char_boy6.setMaterial(mat6);
        char_boy6.setLocalTranslation(-10f, 0, 0);
        TangentBinormalGenerator.generate(char_boy6);
        this.rootNode.attachChild(char_boy6);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        this.flyCam.setMoveSpeed(5);

        this.fpp = new FilterPostProcessor(this.assetManager);
        // fpp.setNumSamples(4);
        final BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setDownSamplingFactor(2);
        bloom.setBlurScale(1.37f);
        bloom.setExposurePower(2.1f);
        bloom.setExposureCutOff(0.3f);
        bloom.setBloomIntensity(7.8f);

        this.fpp.addFilter(bloom);
        this.viewPort.addProcessor(this.fpp);

    }

    @Override
    public void simpleUpdate(final float tpf) {
        // rootNode.rotate(0, tpf * 0.25f, 0);
    }

}
