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

public class TestLightBlowLightMapDirty extends SimpleApplication {

    public static void main(final String[] args) {
        final TestLightBlowLightMapDirty app = new TestLightBlowLightMapDirty();
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

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/lightmap/lightmap.mesh.xml");
        final Material mat = this.assetManager.loadMaterial("TestMaterials/LightBlow/lightmap/lightmap.j3m");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        this.rootNode.attachChild(char_boy);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/lightmap/lightmap.mesh.xml");
//        final Material mat2 = this.assetManager.loadMaterial("TestMaterials/LightBlow/lightmap/lightmap_dirty.j3m");
//        char_boy2.setMaterial(mat2);
//        TangentBinormalGenerator.generate(char_boy2);
//        char_boy2.move(7f, 0f, 0f);
//        this.rootNode.attachChild(char_boy2);

        this.flyCam.setMoveSpeed(25);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(0.5f, 0.9f, 0.5f, 1));
        this.rootNode.addLight(al);

    }

}
