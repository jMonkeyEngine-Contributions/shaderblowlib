package com.shaderblow.test.simplespriteparticle;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;

public class TestSimpleSpriteParticle extends SimpleApplication {

    private Mesh mesh;
    private final SimpleSprite[] sprites = new SimpleSprite[30000];

    private static class SimpleSprite {
        public Vector3f position;
        public float size = 1f;
        public ColorRGBA color;
        public float startX = 1f;
        public float startY = 1f;
        public float endX = 0f;
        public float endY = 0f;
    }

    public static void main(final String[] args) {
        final TestSimpleSpriteParticle app = new TestSimpleSpriteParticle();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLocator("test-data", FileLocator.class);          

        getFlyByCamera().setMoveSpeed(50);

        this.mesh = createSpriteMesh(this.sprites);

        final float min = 0;
        final float max = 30;
        for (int i = 0; i < this.sprites.length; i++) {
            this.sprites[i] = new SimpleSprite();
            this.sprites[i].position = new Vector3f(min + (float) (Math.random() * max), min
                    + (float) (Math.random() * max), min + (float) (Math.random() * max));
            this.sprites[i].color = ColorRGBA.White;
        }
        updateParticleData(this.mesh, this.sprites);

        final Geometry geom = new Geometry("", this.mesh);
        final Material mat = new Material(this.assetManager,
                "ShaderBlow/MatDefs/SimpleSpriteParticle/SimpleSpriteParticle.j3md");
        mat.setTexture("Texture", this.assetManager.loadTexture("TestTextures/matcaps/Gold.jpg"));
        geom.setMaterial(mat);

        this.rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(final float tpf) {
        super.simpleUpdate(tpf);
        updateParticleData(this.mesh, this.sprites);
    }

    private void updateParticleData(final Mesh mesh, final SimpleSprite[] particles) {
        final VertexBuffer pvb = mesh.getBuffer(VertexBuffer.Type.Position);
        final FloatBuffer positions = (FloatBuffer) pvb.getData();

        final VertexBuffer cvb = mesh.getBuffer(VertexBuffer.Type.Color);
        final ByteBuffer colors = (ByteBuffer) cvb.getData();

        final VertexBuffer svb = mesh.getBuffer(VertexBuffer.Type.Size);
        final FloatBuffer sizes = (FloatBuffer) svb.getData();

        final VertexBuffer tvb = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        final FloatBuffer texcoords = (FloatBuffer) tvb.getData();

        // update data in vertex buffers
        positions.rewind();
        colors.rewind();
        sizes.rewind();
        texcoords.rewind();
        for (final SimpleSprite p : particles) {
            positions.put(p.position.x).put(p.position.y).put(p.position.z);
            colors.putInt(p.color.asIntABGR());
            sizes.put(p.size);
            texcoords.put(p.startX).put(p.startY).put(p.endX).put(p.endY);
        }
        positions.flip();
        colors.flip();
        sizes.flip();
        texcoords.flip();

        // force renderer to re-send data to <span class="domtooltips">GPU<span class="domtooltips_tooltip"
        // style="display: none">A "Graphics Processing Unit" is a specialized circuit designed to rapidly manipulate
        // and alter memory in such a way so as to accelerate the building of images in a frame buffer intended for
        // output to a display. GPUs are used in embedded systems, mobile phones, personal computers, workstations, and
        // game consoles.</span></span>
        pvb.updateData(positions);
        cvb.updateData(colors);
        svb.updateData(sizes);
        tvb.updateData(texcoords);
        mesh.updateBound();
    }

    private static void initBuffer(final Mesh mesh, final VertexBuffer.Type type, final Buffer pb, final Usage usage,
            final int components, final Format format, final boolean normalise) {
        final VertexBuffer pvb = new VertexBuffer(type);
        pvb.setupData(usage, components, format, pb);
        if (normalise) {
            pvb.setNormalized(true);
        }

        final VertexBuffer buf = mesh.getBuffer(type);
        if (buf != null) {
            buf.updateData(pb);
        } else {
            mesh.setBuffer(pvb);
        }
    }

    private Mesh createSpriteMesh(final SimpleSprite[] sprites) {
        final Mesh spriteMesh = new Mesh();
        final int numParticles = sprites.length;
        spriteMesh.setMode(Mesh.Mode.Points);
        initBuffer(spriteMesh, VertexBuffer.Type.Position, BufferUtils.createFloatBuffer(numParticles * 3),
                Usage.Stream, 3, Format.Float, false);
        initBuffer(spriteMesh, VertexBuffer.Type.Color, BufferUtils.createByteBuffer(numParticles * 4), Usage.Stream,
                4, Format.UnsignedByte, true);
        initBuffer(spriteMesh, VertexBuffer.Type.Size, BufferUtils.createFloatBuffer(numParticles), Usage.Stream, 1,
                Format.Float, false);
        initBuffer(spriteMesh, VertexBuffer.Type.TexCoord, BufferUtils.createFloatBuffer(numParticles * 4),
                Usage.Stream, 4, Format.Float, false);
        return spriteMesh;
    }
}