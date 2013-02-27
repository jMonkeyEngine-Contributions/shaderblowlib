/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaderblow.test;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author H
 */
public class TestObjectBuilder {

	public static Geometry buildFloor(final AssetManager assetManager, final Node rootNode) {
		final Texture floorTexture = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
		floorTexture.setWrap(Texture.WrapMode.Repeat);

		final Material floorMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		floorMaterial.setTexture("ColorMap", floorTexture);

		final Box floorBox = new Box(70, 0.25f, 70);
		floorBox.scaleTextureCoordinates(new Vector2f(3, 3));

		final Geometry floorGeometry = new Geometry("Floor", floorBox);
		floorGeometry.setMaterial(floorMaterial);
		floorGeometry.setLocalTranslation(0, -1, 0);
		floorGeometry.addControl(new RigidBodyControl(0));
		
		rootNode.attachChild(floorGeometry);

		return floorGeometry;
	}

	public static void buildLights(final Node rootNode) {
        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        rootNode.addLight(al);
	}
	
	public static void buildTestModel(final AssetManager assetManager, final Node rootNode) {
		final Spatial char_boy1 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat1 = assetManager.loadMaterial("TestMaterials/MatCap/MatCap1.j3m");
        char_boy1.setMaterial(mat1);
        char_boy1.setLocalTranslation(0, 0, 0);
        TangentBinormalGenerator.generate(char_boy1);
        rootNode.attachChild(char_boy1);

        final Spatial char_boy2 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = assetManager.loadMaterial("TestMaterials/MatCap/MatCapBump1.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(2, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        rootNode.attachChild(char_boy2);

        final Spatial char_boy3 = assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
        final Material mat3 = assetManager.loadMaterial("TestMaterials/MatCap/MatCap2.j3m");
        char_boy3.setMaterial(mat3);
        char_boy3.setLocalTranslation(-2, 0, 0);
        TangentBinormalGenerator.generate(char_boy3);
        rootNode.attachChild(char_boy3);
	}
	
	public static void buildSkybox(final AssetManager assetManager, final Node rootNode) {
        final Node mainScene = new Node();
        mainScene.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        rootNode.attachChild(mainScene);
	}
}
