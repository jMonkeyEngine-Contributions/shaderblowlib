package com.shaderblow.test.gpuanimationfactory;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

public class TestGPUAnimationFactory extends SimpleApplication {

    public static void main(final String[] args) {
        final TestGPUAnimationFactory app = new TestGPUAnimationFactory();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          

        final Spatial char_boy = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");

        final Material mat_1D = new Material(this.assetManager,
                "ShaderBlow/MatDefs/GPUAnimationFactory/GPUAnimationFactory.j3md");

//// Standard uniforms for lighting
//mat_1D.setBoolean("SteepParallax", true);
        mat_1D.setFloat("Scale", 3.0f);

        final TextureKey tkDif = new TextureKey("TestTextures/Terrain_Textures/pattern_69/pattern_69_diffus.png", false);
        tkDif.setAnisotropy(2);
        final Texture diffuseTex = this.assetManager.loadTexture(tkDif);
        diffuseTex.setWrap(Texture.WrapMode.Repeat);
        mat_1D.setTexture("DiffuseMap", diffuseTex);

        final TextureKey tkNor = new TextureKey("TestTextures/Terrain_Textures/pattern_69/pattern_69_normal.png", false);
        tkNor.setAnisotropy(2);
        final Texture normalTex = this.assetManager.loadTexture(tkNor);
        normalTex.setWrap(Texture.WrapMode.Repeat);
        mat_1D.setTexture("NormalMap", normalTex);


// Applying movement to the texture
        mat_1D.setBoolean("Texture_Move", true);
        mat_1D.setFloat("MoveF_Speed", 1f);
        mat_1D.setFloat("MoveF_Rotation", 10);

// Turn on & Animate texture deformation
        mat_1D.setBoolean("Texture_Deform", true);
        mat_1D.setBoolean("Texture_Animate", true);

// Applying texture ripple
        mat_1D.setBoolean("DeformF_Wave", true);
        mat_1D.setFloat("DeformF_Wave_SizeX", 5f);
        mat_1D.setFloat("DeformF_Wave_SizeY", 5f);
        mat_1D.setFloat("DeformF_Wave_DepthX", 5f);
        mat_1D.setFloat("DeformF_Wave_DepthY", 5f);
        mat_1D.setFloat("DeformF_Wave_SpeedX", 0.5f);
        mat_1D.setFloat("DeformF_Wave_SpeedY", 0.5f);

//// Applying texture warp
//mat_1D.setBoolean("DeformF_Warp", true);
//mat_1D.setFloat("DeformF_Warp_SizeX",12f);
//mat_1D.setFloat("DeformF_Warp_SizeY",12f);
//mat_1D.setFloat("DeformF_Warp_DepthX",12f);
//mat_1D.setFloat("DeformF_Warp_DepthY",12f);
//mat_1D.setFloat("DeformF_Warp_SpeedX",1f);
//mat_1D.setFloat("DeformF_Warp_SpeedY",1f);

//// Applying texture warp
//mat_1D.setBoolean("DeformF_Mixer", true);
//mat_1D.setFloat("DeformF_Mixer_SizeX",12f);
//mat_1D.setFloat("DeformF_Mixer_SizeY",12f);
//mat_1D.setFloat("DeformF_Mixer_DepthX",12f);
//mat_1D.setFloat("DeformF_Mixer_DepthY",12f);
//mat_1D.setFloat("DeformF_Mixer_SpeedX",1f);
//mat_1D.setFloat("DeformF_Mixer_SpeedY",1f);
// 
// Applying texture breath
        mat_1D.setBoolean("DeformF_Breath", true);

// Applying vertex deforms (X axis)
        mat_1D.setBoolean("DeformX_Wave", true);
        mat_1D.setBoolean("DeformX_Ripple", true);
        mat_1D.setBoolean("DeformX_Warble", true);
        mat_1D.setBoolean("DeformX_Pulse", true);
        mat_1D.setBoolean("DeformX_Swell", true);

// Global X axis settings
        mat_1D.setInt("DirX", 180);
        mat_1D.setFloat("SpeedX", 5f);
        mat_1D.setFloat("SizeX", 10f);
        mat_1D.setFloat("DepthX", .02f);

// Mirror deforms along X axis
        mat_1D.setBoolean("MirrorX", true);

// To apply these along other axis replace X with either Y or Z in the examples for X axis

// To rotate textures:
// Auto-rotation:
        mat_1D.setBoolean("RotF_Rotate", true); // turn rotation on
        mat_1D.setBoolean("RotF_AutoRotate", true); // turn auto-rotation on
        mat_1D.setBoolean("RotF_Clockwise", false);  // true is default setting
        mat_1D.setFloat("RotF_Speed", 0.35f); // sets the rotation speed

// Picking a static rotation:
        mat_1D.setBoolean("RotF_Rotate", true); // turn rotation on
        mat_1D.setBoolean("RotF_AutoRotate", false); // turn auto-rotation off
        mat_1D.setFloat("RotF_Angle", 35f); // pick an angle

        // // Change the X to Y or Z for other axis
        // mat_1D.setFloat("Offset1X", .02f);
        // mat_1D.setFloat("Offset2X", .02f);

        char_boy.setMaterial(mat_1D);
        char_boy.setLocalTranslation(0, 2, 1);
        this.rootNode.attachChild(char_boy);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        this.flyCam.setMoveSpeed(30);
        this.viewPort.setBackgroundColor(ColorRGBA.Gray);

    }
}
