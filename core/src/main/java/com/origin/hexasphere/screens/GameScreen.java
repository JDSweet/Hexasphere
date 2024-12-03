package com.origin.hexasphere.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.origin.hexasphere.geometry.Hexasphere;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    public float phi = (float) (1 + Math.sqrt(5d)) / 2f;
    public float[] vertices = new float[]
        {
            0, 1, phi,
            0, -1, phi,
            0, 1, -phi,
            0, -1, -phi,
            phi, 0, 1,
            -phi, 0, 1,
            phi, 0, -1,
            -phi, 0, -1,
            1, phi, 0,
            -1, phi, 0,
            1, -phi, 0,
            -1, -phi, 0
        };

    private short[] indices = new short[]
        {
            0, 11, 5,
            0, 5, 1,
            0, 1, 7,
            0, 7, 10,
            0, 10, 11,
            1, 5, 9,
            5, 11, 4,
            11, 10, 2,
            10, 7, 6,
            7, 1, 8,
            3, 9, 4,
            3, 4, 2,
            3, 2, 6,
            3, 6, 8,
            3, 8, 9,
            4, 9, 5,
            2, 4, 11,
            6, 2, 10,
            8, 6, 7,
            9, 8, 1
        };

    private Mesh mesh;
    private Model model;
    private ShaderProgram shader;
    private ModelBatch batch;
    private PerspectiveCamera cam;
    private ModelInstance modelInstance;
    private Hexasphere hexasphere;

    @Override
    public void show()
    {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        batch = new ModelBatch();
        hexasphere = new Hexasphere();

        //modelInstance = createSquare();
        //modelInstance = createIcosahedron();
        modelInstance = hexasphere.createModel();
        //modelInstance = createIco2();
    }


    Vector3 hexPos = new Vector3(0f, 0f, 0f);
    Vector3 wCamRotationSpeed = new Vector3(0.0f, 0.0f, 0.02f);
    Vector3 sCamRotationSpeed = new Vector3(0f, 0f, -0.02f);
    Vector3 aCamRotationSpeed = new Vector3(-0.02f, 0f, 0f);
    Vector3 dCamRotationSpeed = new Vector3(0.02f, 0f, 0f);

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //cam.rotateAround(hexPos, camRotationSpeed, 1f);
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            cam.rotateAround(hexPos, wCamRotationSpeed, 1f);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            cam.rotateAround(hexPos, aCamRotationSpeed, 1f);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            cam.rotateAround(hexPos, dCamRotationSpeed, 1f);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            cam.rotateAround(hexPos, sCamRotationSpeed, 1f);

        //cam.position.x += 0.01f;
        //cam.position.y += 0.01f;
        //cam.position.z += 0.01f;
        //cam.rotate(1f, 1f, 0f, 1f);

        //cam.lookAt(0, 0, 0);
        cam.update();

        batch.begin(cam);
        batch.render(modelInstance);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

    public ModelInstance createSquare()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(1f, 1f, 1f,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance instance = new ModelInstance(model);
        return instance;
    }

    public ModelInstance createIco2()
    {
        float[] r_verts = new float[]
        {
            -1, phi, 0, 1, phi, 0, -1, -phi, 0, 1, -phi, 0,
            0, -1, phi, 0, 1, phi, 0, -1, -phi, 0, 1, -phi,
            phi, 0, -1, phi, 0, 1, -phi, 0, -1, -phi, 0, 1
        };
        short[] r_indices = new short[]
        {
            0, 11, 5, 0, 5, 1, 0, 1, 7, 0, 7, 10, 0, 10, 11,
            11, 10, 2, 5, 11, 4, 1, 5, 9, 7, 1, 8, 10, 7, 6,
            3, 9, 4, 3, 4, 2, 3, 2, 6, 3, 6, 8, 3, 8, 9,
            9, 8, 1, 4, 9, 5, 2, 4, 11, 6, 2, 10, 8, 6, 7
        };

        //r_indices = reverseArr(r_indices);

        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();
        b.begin(VertexAttributes.Usage.Position);
        for(int i = 0; i <= r_verts.length / 3; i++)
        {
            b.vertex(r_verts[i], r_verts[i+1], r_verts[i+2]);
        }
        for(int i = 0; i < r_indices.length; i++)
        {
            b.index(r_indices[i]);
        }
        Mesh mesh = b.end();
        modelBuilder.begin();
        modelBuilder.part("ico", mesh, VertexAttributes.Usage.Position, new Material());
        return new ModelInstance(modelBuilder.end());
    }

    short[] reverseArr(short[] input)
    {
        short[] output = new short[input.length];
        //short[] curTri = new short[3];
        for(int oi = 0; oi < output.length-3; oi += 3)
        {
            output[oi] = input[input.length-1-oi];
        }
        return output;
    }

    short[] reverseTriangles(short[] input)
    {
        short[] output = new short[input.length];
        //short[] curTri = new short[3];
        for(int oi = 0; oi < output.length-3; oi += 3)
        {
            short idx1 = (short)oi;
            short idx2 = (short)(oi+1);
            short idx3 = (short)(oi+2);
            output[oi] = idx3;
            output[oi+1] = idx2;
            output[oi+2] = idx1;
        }
        return output;
    }

    public ModelInstance createIcosahedron1()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();
        b.begin(VertexAttributes.Usage.Position);
        for(int i = 0; i <= vertices.length / 3; i++)
        {
            b.vertex(vertices[i], vertices[i+1], vertices[i+2]);
        }
        for(int i = 0; i < indices.length; i++)
        {
            b.index(indices[i]);
        }
        Mesh mesh = b.end();
        modelBuilder.begin();
        modelBuilder.part("ico", mesh, VertexAttributes.Usage.Position, new Material());
        return new ModelInstance(modelBuilder.end());
    }
}
