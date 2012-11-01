package com.shaderblow.test.filter.basicssao;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;
import com.shaderblow.filter.basicssao.BasicSSAO;

public class TestBasicSSAO extends SimpleApplication {

    private FilterPostProcessor fpp;

    public static void main(final String[] args) {
        final TestBasicSSAO app = new TestBasicSSAO();
        app.start();

    }

    public void setupFilters() {
        
        assetManager.registerLocator("assets", FileLocator.class);
        
        this.fpp = new FilterPostProcessor(this.assetManager);

        // BasicSSAO ssao = new BasicSSAO();
        // ssao.scaleSettings(0.25f); // or whatever works for your model scale

        // In vars: reflection-radius, intensity, scale, bias
        final BasicSSAO ssao = new BasicSSAO(0.15f, 5.5f, 0.5f, 0.025f);
        // Add in detail pass - this doubles the number of samples taken and halves performance. But, allows for
        // smoothing artifacting while keeping detail
        ssao.setUseDetailPass(true);
        // Add distance falloff and set distance/rate of falloff
        ssao.setUseDistanceFalloff(true);
        ssao.setFalloffStartDistance(50f);
        ssao.setFalloffRate(4.0f);

        this.fpp.addFilter(ssao);
        this.viewPort.addProcessor(this.fpp);

    }

    @Override
    public void simpleInitApp() {

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        char_boy.setMaterial(mat);
        TangentBinormalGenerator.generate(char_boy);
        this.rootNode.attachChild(char_boy);

        final Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        final Geometry geom = new Geometry("Box", b);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, 2, 1);
        this.rootNode.attachChild(geom);

        final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(-2f, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        this.rootNode.attachChild(char_boy2);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        this.rootNode.addLight(al);

        this.flyCam.setMoveSpeed(15);

        setupFilters();

    }

    @Override
    public void simpleUpdate(final float tpf) {
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);
    }
}
