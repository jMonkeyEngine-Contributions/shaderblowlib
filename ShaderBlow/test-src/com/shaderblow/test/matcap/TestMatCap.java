/*
 * Copyright (c) 2009-2012 ShaderBlow
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ShaderBlow' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.shaderblow.test.matcap;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;
import com.shaderblow.lightblow.CartoonEdgeProcessor;

public class TestMatCap extends SimpleApplication {

	public static void main(final String[] args) {
		final TestMatCap app = new TestMatCap();
		app.start();
	}

	@Override
	public void simpleInitApp() {

		this.assetManager.registerLocator("assets", FileLocator.class);

		final Spatial char_boy1 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
		final Material mat1 = this.assetManager.loadMaterial("TestMaterials/MatCap/MatCap1.j3m");
		char_boy1.setMaterial(mat1);
		char_boy1.setLocalTranslation(0, 0, 0);
		TangentBinormalGenerator.generate(char_boy1);
		this.rootNode.attachChild(char_boy1);

		final Spatial char_boy2 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
		final Material mat2 = this.assetManager.loadMaterial("TestMaterials/MatCap/MatCapBump1.j3m");
		char_boy2.setMaterial(mat2);
		char_boy2.setLocalTranslation(1, 0, 0);
		TangentBinormalGenerator.generate(char_boy2);
		this.rootNode.attachChild(char_boy2);

		final Spatial char_boy3 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
		final Material mat3 = this.assetManager.loadMaterial("TestMaterials/MatCap/MatCap2.j3m");
		char_boy3.setMaterial(mat3);
		char_boy3.setLocalTranslation(-1, 0, 0);
		TangentBinormalGenerator.generate(char_boy3);
		this.rootNode.attachChild(char_boy3);

		final Spatial char_boy4 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");
		final Material mat4 = this.assetManager.loadMaterial("TestMaterials/MatCap/MatCapBump2.j3m");
		char_boy4.setMaterial(mat4);
		char_boy4.setLocalTranslation(-2, 0, 0);
		TangentBinormalGenerator.generate(char_boy4);
		this.rootNode.attachChild(char_boy4);

		this.flyCam.setMoveSpeed(10);
		this.viewPort.setBackgroundColor(ColorRGBA.Gray);

		if (this.renderer.getCaps().contains(Caps.GLSL100)) {
			final CartoonEdgeProcessor cartoonEdgeProcess = new CartoonEdgeProcessor();
			this.viewPort.addProcessor(cartoonEdgeProcess);
		}

		// Requiered for toon edge effect
		final DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
		dl.setColor(new ColorRGBA(1, 1, 1, 1));
		this.rootNode.addLight(dl);

	}

}
