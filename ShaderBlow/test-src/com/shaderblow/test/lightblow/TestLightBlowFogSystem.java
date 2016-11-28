package com.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.Type;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

public class TestLightBlowFogSystem extends SimpleApplication {

    private final Node fog = new Node();

    public static void main(final String[] args) {
        final TestLightBlowFogSystem app = new TestLightBlowFogSystem();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          

        final TextureKey skyhi = new TextureKey("TestTextures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        skyhi.setTextureTypeHint(Type.CubeMap);//skyhi.setAsCube(true);

        // TextureKey skylow = new TextureKey("TestTextures/Water32.dds", true);
        // skylow.setGenerateMips(true);
        // skylow.setAsCube(true);
        final Texture texlow = this.assetManager.loadTexture(skyhi);
        this.rootNode.attachChild(SkyFactory.createSky(this.assetManager, texlow, false));

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat = this.assetManager.loadMaterial("TestMaterials/LightBlow/Fog_System/LightBlow_Fog.j3m");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        // rootNode.attachChild(char_boy);

        final Node fog1[] = new Node[20];
        for (int i = 0; i < fog1.length; i++) {

            final Node ndd = new Node("fog1_" + i);
            ndd.attachChild(char_boy.clone(false));
            ndd.setLocalTranslation(0, 0, -(i + 3) * 5);
            System.out.println(ndd.getName());
            this.fog.attachChild(ndd);
        }

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Fog_System/LightBlow_Fog_Skybox.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(2.0f, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        // rootNode.attachChild(char_boy2);

        final Node fog2[] = new Node[20];
        for (int i = 0; i < fog2.length; i++) {

            final Node ndd = new Node("fog2_" + i);
            ndd.attachChild(char_boy2.clone(false));
            ndd.setLocalTranslation(3, 0, -(i + 3) * 5);
            System.out.println(ndd.getName());
            this.fog.attachChild(ndd);
        }

        this.rootNode.attachChild(this.fog);

        // Spatial char_boy3 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        // Material mat3 = assetManager.loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_ref_a_nor.j3m");
        // char_boy3.setMaterial(mat3);
        // char_boy3.setLocalTranslation(-4f, 0, 0);
        // TangentBinormalGenerator.generate(char_boy3);
        // rootNode.attachChild(char_boy3);
        //
        // Spatial char_boy4 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        // Material mat4 = assetManager.loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_minnaert.j3m");
        // char_boy4.setMaterial(mat4);
        // char_boy4.setLocalTranslation(-6f, 0, 0);
        // TangentBinormalGenerator.generate(char_boy4);
        // rootNode.attachChild(char_boy4);
        //
        // Spatial char_boy5 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        // Material mat5 = assetManager.loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_rim.j3m");
        // char_boy5.setMaterial(mat5);
        // char_boy5.setLocalTranslation(-8f, 0, 0);
        // TangentBinormalGenerator.generate(char_boy5);
        // rootNode.attachChild(char_boy5);
        //
        // Spatial char_boy6 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        // Material mat6 = assetManager.loadMaterial("TestMaterials/LightBlow/Shading_System/LightBlow_rim_2.j3m");
        // char_boy6.setMaterial(mat6);
        // char_boy6.setLocalTranslation(-10f, 0, 0);
        // TangentBinormalGenerator.generate(char_boy6);
        // rootNode.attachChild(char_boy6);
        //

        this.flyCam.setMoveSpeed(5);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

    }

}
