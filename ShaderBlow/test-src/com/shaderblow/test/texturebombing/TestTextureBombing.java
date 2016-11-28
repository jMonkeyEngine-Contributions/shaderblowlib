package com.shaderblow.test.texturebombing;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/*
 * @author wezrule
*/

public class TestTextureBombing extends SimpleApplication {

    public static void main(String[] args) {
        new TestTextureBombing().start();
    }

    public TestTextureBombing () {
        super (new FlyCamAppState ());
    }
    
    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          

        Box b = new Box(1, 1, 1);
        Spatial spatial = new Geometry("box", b);
        Material textureBombMat = new Material(assetManager, "ShaderBlow/MatDefs/TextureBombing/TextureBombing.j3md");
        textureBombMat.setTexture("TextureAtlas", assetManager.loadTexture("TestTextures/TextureBombing/Glyphs.png"));
        textureBombMat.setTexture("NoiseTex", assetManager.loadTexture("TestTextures/TextureBombing/Noise.png"));

        textureBombMat.setFloat("ScaleFactor", 10f);       // Scales the tex coords by 10, increasing the cell count
        textureBombMat.setFloat("NumImages", 10f);         // A 10 x 10 texture atlas (100 images)
        textureBombMat.setFloat("Percentage", 1f);      // 75% of cells will be filled
        textureBombMat.setBoolean("RandomScale", true);   // Should the images be randomly scaled?
        textureBombMat.setBoolean("UseAtlasColors", false); // We have a black and white image, so let the shader generate color
        textureBombMat.setBoolean("RandomRotate", true);  // Should they be randomly rotated?
        textureBombMat.setFloat("SamplesPerCell", 1.0f);   // Should be >= 1.0f (How many images per cell)
        textureBombMat.setBoolean("Animated", false);       // Should the images be animated?

        // Change filters otherwise there are artifacts
        textureBombMat.getTextureParam("TextureAtlas").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        textureBombMat.getTextureParam("NoiseTex").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);

        spatial.setMaterial(textureBombMat);
        rootNode.attachChild(spatial);

        // 2nd Model
        Material matcapMat = textureBombMat.clone();
        matcapMat.setColor("Color", new ColorRGBA(0.3f, 0.7f, 1f, 1.0f));

        final Spatial char_boy1 = this.assetManager.loadModel("TestModels/LightBlow/jme_lightblow.mesh.xml");

        char_boy1.setMaterial(matcapMat);
        char_boy1.setLocalTranslation(2.5f, -1, 0);
        TangentBinormalGenerator.generate(char_boy1);

        AnimControl control1 = char_boy1.getControl(AnimControl.class);
        AnimChannel channel1 = control1.createChannel();
        channel1.setAnim("Action");
        SkeletonControl skeletonControl1 = char_boy1.getControl(SkeletonControl.class);
        skeletonControl1.setHardwareSkinningPreferred(true);

        rootNode.attachChild(char_boy1);

        // 3rd model (same one but with animations)
        Spatial clone = char_boy1.clone(true);
        clone.setLocalTranslation(4, -1, 0);
//        Geometry child = (Geometry) ((Node) clone).getChild("jme_lightblow-geom-1");
        Geometry child = (Geometry) ((Node) clone).getChild(0); //there is only one geom
        child.getMaterial().setBoolean("Animated", true);       // Should the images be animated?
        rootNode.attachChild(clone);

        // 4th model (Using a 9x9 blood atlas)
        Spatial clone1 = char_boy1.clone(true);
        clone1.setLocalTranslation(0, -1, 3);
//        Geometry child1 = (Geometry) ((Node) clone1).getChild("jme_lightblow-geom-1");
        Geometry child1 = (Geometry) ((Node) clone1).getChild(0); //there is only one geom

        Material childMat = child1.getMaterial();
        childMat.setColor("Color", ColorRGBA.White);
        childMat.setTexture("ColorMap", assetManager.loadTexture("TestTextures/matcaps/met2.png"));
        childMat.setFloat("NumImages", 3f);         // A 3 x 3 texture atlas (9 images)
        childMat.setFloat("ScaleFactor", 15f);       // Scales the tex coords by 10, increasing the cell count
        childMat.setTexture("TextureAtlas", assetManager.loadTexture("TestTextures/TextureBombing/Blood.png"));
        childMat.getTextureParam("TextureAtlas").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        childMat.getTextureParam("NoiseTex").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        childMat.setFloat("SamplesPerCell", 6.0f);   // Should be >= 1.0f (How many images per cell)
        childMat.setBoolean("Animated", false);       // Should the images be animated?
        childMat.setBoolean("UseAtlasColors", true);
        rootNode.attachChild(clone1);
    }
}