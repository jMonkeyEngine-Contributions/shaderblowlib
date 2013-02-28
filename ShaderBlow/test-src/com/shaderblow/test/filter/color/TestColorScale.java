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
package com.shaderblow.test.filter.color;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.shaderblow.filter.colorscale.ColorScaleFilter;
import com.shaderblow.test.TestObjectBuilder;

public class TestColorScale extends SimpleApplication {

	private FilterPostProcessor fpp;
	private boolean enabled = true;
	private ColorScaleFilter colorScale;

	public static void main(final String[] args) {
		final TestColorScale app = new TestColorScale();
		app.start();
	}

	@Override
	public void simpleInitApp() {

		this.assetManager.registerLocator("assets", FileLocator.class);

		this.flyCam.setMoveSpeed(10);

		TestObjectBuilder.buildTestModel(this.assetManager, this.rootNode);
		TestObjectBuilder.buildSkybox(this.assetManager, this.rootNode);
		TestObjectBuilder.buildFloor(this.assetManager, this.rootNode);
		TestObjectBuilder.buildLights(this.rootNode);

		this.flyCam.setMoveSpeed(15);

		this.fpp = new FilterPostProcessor(this.assetManager);
		// this.fpp.setNumSamples(4);
		this.colorScale = new ColorScaleFilter(new ColorRGBA(130f / 255f, 26f / 255f, 90f / 255f, 1.0f), 0.7f);
		this.fpp.addFilter(this.colorScale);
		this.viewPort.addProcessor(this.fpp);
		this.initInputs();

		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize());
		ch.setText("Space,Y,H,1,2,3"); // crosshairs
		ch.setColor(new ColorRGBA(1f, 0.8f, 0.1f, 1f));
		ch.setLocalTranslation(settings.getWidth() * 0.3f, settings.getHeight() * 0.1f, 0);
		guiNode.attachChild(ch);

	}

	private void initInputs() {
		this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
		this.inputManager.addMapping("Overlay", new KeyTrigger(KeyInput.KEY_1));
		this.inputManager.addMapping("Multiply", new KeyTrigger(KeyInput.KEY_2));
		this.inputManager.addMapping("Normal", new KeyTrigger(KeyInput.KEY_3));
		this.inputManager.addMapping("DensityUp", new KeyTrigger(KeyInput.KEY_Y));
		this.inputManager.addMapping("DensityDown", new KeyTrigger(KeyInput.KEY_H));

		final ActionListener acl = new ActionListener() {
			@Override
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (name.equals("Overlay") && keyPressed) {
					if (TestColorScale.this.enabled) {
						TestColorScale.this.colorScale.setMultiply(false);
						TestColorScale.this.colorScale.setOverlay(true);
					}
				}

				if (name.equals("Multiply") && keyPressed) {
					if (TestColorScale.this.enabled) {
						TestColorScale.this.colorScale.setMultiply(true);
						TestColorScale.this.colorScale.setOverlay(false);
					}
				}

				if (name.equals("Normal") && keyPressed) {
					if (TestColorScale.this.enabled) {
						TestColorScale.this.colorScale.setMultiply(false);
						TestColorScale.this.colorScale.setOverlay(false);
					}
				}

				if (name.equals("toggle") && keyPressed) {
					if (TestColorScale.this.enabled) {
						TestColorScale.this.enabled = false;
						TestColorScale.this.viewPort.removeProcessor(TestColorScale.this.fpp);
					} else {
						TestColorScale.this.enabled = true;
						TestColorScale.this.viewPort.addProcessor(TestColorScale.this.fpp);
					}
				}

			}
		};

		final AnalogListener anl = new AnalogListener() {
			@Override
			public void onAnalog(final String name, final float isPressed, final float tpf) {
				if (name.equals("DensityUp")) {
					TestColorScale.this.colorScale
							.setColorDensity(TestColorScale.this.colorScale.getColorDensity() + 0.001f);
					System.out.println("ColorScale density : " + TestColorScale.this.colorScale.getColorDensity());
				}
				if (name.equals("DensityDown")) {
					TestColorScale.this.colorScale
							.setColorDensity(TestColorScale.this.colorScale.getColorDensity() - 0.001f);
					System.out.println("ColorScale density : " + TestColorScale.this.colorScale.getColorDensity());
				}
			}
		};

		this.inputManager.addListener(acl, "toggle");
		this.inputManager.addListener(acl, "Overlay");
		this.inputManager.addListener(acl, "Multiply");
		this.inputManager.addListener(acl, "Normal");
		this.inputManager.addListener(anl, "DensityUp", "DensityDown");

	}
}