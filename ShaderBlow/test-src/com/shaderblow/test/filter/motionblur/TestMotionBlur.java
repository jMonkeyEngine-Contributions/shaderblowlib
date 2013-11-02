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
package com.shaderblow.test.filter.motionblur;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.post.FilterPostProcessor;
import com.shaderblow.filter.motionblur.MotionBlurFilter;
import com.shaderblow.test.TestObjectBuilder;

public class TestMotionBlur extends SimpleApplication {

	private FilterPostProcessor fpp;
	private boolean enabled = true;
	private MotionBlurFilter motionBlurFilter;

	
	public static void main(final String[] args) {
		new TestMotionBlur().start();
	}

	
	@Override
	public void simpleInitApp() {

		this.assetManager.registerLocator("assets", FileLocator.class);

		TestObjectBuilder.buildTestModel(this.assetManager, this.rootNode);
		TestObjectBuilder.buildSkybox(this.assetManager, this.rootNode);
		TestObjectBuilder.buildFloor(this.assetManager, this.rootNode);
		TestObjectBuilder.buildLights(this.rootNode);

		this.flyCam.setMoveSpeed(15);

		this.fpp = new FilterPostProcessor(this.assetManager);
		
		motionBlurFilter = new MotionBlurFilter();
		
		this.fpp.addFilter(motionBlurFilter);
		this.viewPort.addProcessor(this.fpp);
		
		this.initInputs();
	}

	
	private void initInputs() {
		this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));

		final ActionListener acl = new ActionListener() {

			@Override
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (name.equals("toggle") && keyPressed) {
					if (TestMotionBlur.this.enabled) {
						TestMotionBlur.this.enabled = false;
						TestMotionBlur.this.viewPort.removeProcessor(TestMotionBlur.this.fpp);
					} else {
						TestMotionBlur.this.enabled = true;
						TestMotionBlur.this.viewPort.addProcessor(TestMotionBlur.this.fpp);
					}
				}

			}
		};

		this.inputManager.addListener(acl, "toggle");

	}
}