package com.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;

public class TestLightBlowToonPostEdgesJMEFilter extends SimpleApplication {

    private FilterPostProcessor fpp;

    public static void main(final String[] args) {
        final TestLightBlowToonPostEdgesJMEFilter app = new TestLightBlowToonPostEdgesJMEFilter();
        app.start();

    }

    public void setupFilters() {
        if (this.renderer.getCaps().contains(Caps.GLSL120)) {
            this.fpp = new FilterPostProcessor(this.assetManager);
            final CartoonEdgeFilter toon = new CartoonEdgeFilter();
            this.fpp.addFilter(toon);
            this.viewPort.addProcessor(this.fpp);

        }
    }

    @Override
    public void simpleInitApp() {
        
        assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          

        final Spatial toon = this.assetManager.loadModel("TestModels/ToonBlow/toon.obj");
        final Material mat = this.assetManager.loadMaterial("TestMaterials/LightBlow/Toon_System/Toon_Base.j3m");
        toon.setMaterial(mat);
        TangentBinormalGenerator.generate(toon);
        this.rootNode.attachChild(toon);

        final Spatial toon2 = this.assetManager.loadModel("TestModels/ToonBlow/toon.obj");
        final Material mat2 = this.assetManager
                .loadMaterial("TestMaterials/LightBlow/Toon_System/Toon_Base_Specular.j3m");
        toon2.setMaterial(mat2);
        TangentBinormalGenerator.generate(toon2);
        toon2.setLocalTranslation(-2f, 0, 0);
        this.rootNode.attachChild(toon2);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        this.rootNode.addLight(al);

        this.flyCam.setMoveSpeed(5);

        setupFilters();

    }

    @Override
    public void simpleUpdate(final float tpf) {
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);
    }

}
