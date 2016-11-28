/*
 * Copyright (c) 2009-2010 jMonkeyEngine
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
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
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
package com.shaderblow.test.filter.simplerefractionfilter;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.shaderblow.filter.simplerefractionfilter.SimpleRefractionFilter;
import com.shaderblow.filter.simplerefractionfilter.SimpleRefractionFilter;

/**
 *
 * @author normenhansen
 */
public class TestSimpleRefractionFilter_Objects extends SimpleApplication {

    Material mat;
    Geometry jmeSphere;
    Node sceneNode;
    boolean useWater = true;
    private Vector3f lightPos = new Vector3f(33, 12, -29);

    public static void main(String[] args) {
        TestSimpleRefractionFilter_Objects app = new TestSimpleRefractionFilter_Objects();
        AppSettings aps = new AppSettings(true);
        aps.setVSync(false);
        app.setSettings(aps);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          
        
        initScene();
        
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        SimpleRefractionFilter refract = new SimpleRefractionFilter(assetManager, SimpleRefractionFilter.RefractMode.Objects);
        fpp.addFilter(refract);
        viewPort.addProcessor(fpp);
        // fpp.setNumSamples(4);

        flyCam.setMoveSpeed(70f);
//        flyCam.setDragToRotate(true);

        
        Node nd = new Node("nd");
        
//        Box quad = new Box(10f, 10f, 10f);
        Sphere quad = new Sphere(10, 10, 20);
        Geometry geom = new Geometry("WaterGeometry", quad);
        Material matref = new Material(assetManager, "ShaderBlow/MatDefs/Filters/SimpleRefraction/Refract.j3md");
        matref.setBoolean("DoRefract", true);
        matref.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
//        matref.setBoolean("DoRefract", true);
        geom.setMaterial(matref);
        
        for (int i = 0; i < 50; i++) {
            Geometry geo = geom.clone(false);
            geo.move(30f * i, 0f, 0f);
            rootNode.attachChild(geo);
        }

        rootNode.attachChild(nd);        
        
    }

    private void initScene() {
        //init cam location
        cam.setLocation(new Vector3f(0, 30, 30));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        //init scene
        sceneNode = new Node("Scene");
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        geom.setMaterial(mat);
        sceneNode.attachChild(geom);

        // load sky
        sceneNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        rootNode.attachChild(sceneNode);

        for (int i = 0; i < 50; i++) {
            //add lightPos Geometry
            Sphere lite = new Sphere(50, 50, 3.0f);
            jmeSphere = new Geometry("lightsphere", lite);
            jmeSphere.setMaterial(mat);
            jmeSphere.setLocalTranslation((float) Math.random() * 700.0f,(float) Math.random() * -10.0f,(float)Math.random() * 10.0f);
            sceneNode.attachChild(jmeSphere);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
    }
}
