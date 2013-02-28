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
package com.shaderblow.test.filter.oldfilm;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.shaderblow.filter.oldfilm.OldFilmFilter;
import com.shaderblow.test.TestObjectBuilder;

public class TestOldFilm extends SimpleApplication {

	private FilterPostProcessor fpp;
	private boolean enabled = true;
	private OldFilmFilter oldFilmFilter;

	public static void main(final String[] args) {
		final TestOldFilm app = new TestOldFilm();
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
		this.oldFilmFilter = new OldFilmFilter(new ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f), 0.7f, 0.4f,
				0.3f, 0.9f);
		this.fpp.addFilter(this.oldFilmFilter);
		this.viewPort.addProcessor(this.fpp);
		this.initInputs();

		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize());
		ch.setText("space: Filter ON/OFF\nY/H: Color Density UP/DOWN\nU/J: Noise Density UP/DOWN\nI/K: Scratching Density UP/DOWN\nO/L: Vignetting Diameter UP/DOWN"); // crosshairs
		ch.setColor(new ColorRGBA(1f, 0.8f, 0.1f, 1f));
		ch.setLocalTranslation(settings.getWidth() * 0.01f, settings.getHeight() * 0.99f, 0);
		guiNode.attachChild(ch);
	}

	private void initInputs() {
		this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
		this.inputManager.addMapping("ColorDensityUp", new KeyTrigger(KeyInput.KEY_Y));
		this.inputManager.addMapping("ColorDensityDown", new KeyTrigger(KeyInput.KEY_H));
		this.inputManager.addMapping("NoiseDensityUp", new KeyTrigger(KeyInput.KEY_U));
		this.inputManager.addMapping("NoiseDensityDown", new KeyTrigger(KeyInput.KEY_J));
		this.inputManager.addMapping("ScratchDensityUp", new KeyTrigger(KeyInput.KEY_I));
		this.inputManager.addMapping("ScratchDensityDown", new KeyTrigger(KeyInput.KEY_K));
		this.inputManager.addMapping("VignettingValueUp", new KeyTrigger(KeyInput.KEY_O));
		this.inputManager.addMapping("VignettingValueDown", new KeyTrigger(KeyInput.KEY_L));

		final ActionListener acl = new ActionListener() {

			@Override
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (name.equals("toggle") && keyPressed) {
					if (TestOldFilm.this.enabled) {
						TestOldFilm.this.enabled = false;
						TestOldFilm.this.viewPort.removeProcessor(TestOldFilm.this.fpp);
					} else {
						TestOldFilm.this.enabled = true;
						TestOldFilm.this.viewPort.addProcessor(TestOldFilm.this.fpp);
					}
				}

			}
		};

		final AnalogListener anl = new AnalogListener() {

			@Override
			public void onAnalog(final String name, final float isPressed, final float tpf) {
				if (name.equals("ColorDensityUp")) {
					TestOldFilm.this.oldFilmFilter
							.setColorDensity(TestOldFilm.this.oldFilmFilter.getColorDensity() + 0.01f);
					System.out.println("OldFilm color density : " + TestOldFilm.this.oldFilmFilter.getColorDensity());
				}
				if (name.equals("ColorDensityDown")) {
					TestOldFilm.this.oldFilmFilter
							.setColorDensity(TestOldFilm.this.oldFilmFilter.getColorDensity() - 0.01f);
					System.out.println("OldFilm color density : " + TestOldFilm.this.oldFilmFilter.getColorDensity());
				}
				if (name.equals("NoiseDensityUp")) {
					TestOldFilm.this.oldFilmFilter
							.setNoiseDensity(TestOldFilm.this.oldFilmFilter.getNoiseDensity() + 0.01f);
					System.out.println("OldFilm noise density : " + TestOldFilm.this.oldFilmFilter.getNoiseDensity());
				}
				if (name.equals("NoiseDensityDown")) {
					TestOldFilm.this.oldFilmFilter
							.setNoiseDensity(TestOldFilm.this.oldFilmFilter.getNoiseDensity() - 0.01f);
					System.out.println("OldFilm noise density : " + TestOldFilm.this.oldFilmFilter.getNoiseDensity());
				}
				if (name.equals("ScratchDensityUp")) {
					TestOldFilm.this.oldFilmFilter
							.setScratchDensity(TestOldFilm.this.oldFilmFilter.getScratchDensity() + 0.01f);
					System.out.println("OldFilm scratch density : "
							+ TestOldFilm.this.oldFilmFilter.getScratchDensity());
				}
				if (name.equals("ScratchDensityDown")) {
					TestOldFilm.this.oldFilmFilter
							.setScratchDensity(TestOldFilm.this.oldFilmFilter.getScratchDensity() - 0.01f);
					System.out.println("OldFilm scratch density : "
							+ TestOldFilm.this.oldFilmFilter.getScratchDensity());
				}
				if (name.equals("VignettingValueUp")) {
					TestOldFilm.this.oldFilmFilter.setVignettingValue(TestOldFilm.this.oldFilmFilter
							.getVignettingValue() + 0.01f);
					System.out.println("OldFilm vignetting diameter : "
							+ TestOldFilm.this.oldFilmFilter.getVignettingValue());
				}
				if (name.equals("VignettingValueDown")) {
					TestOldFilm.this.oldFilmFilter.setVignettingValue(TestOldFilm.this.oldFilmFilter
							.getVignettingValue() - 0.01f);
					System.out.println("OldFilm vignetting diameter : "
							+ TestOldFilm.this.oldFilmFilter.getVignettingValue());
				}
			}
		};

		this.inputManager.addListener(acl, "toggle");
		this.inputManager.addListener(anl, "ColorDensityUp", "ColorDensityDown", "NoiseDensityUp", "NoiseDensityDown",
				"ScratchDensityUp", "ScratchDensityDown", "VignettingValueUp", "VignettingValueDown");

	}
}