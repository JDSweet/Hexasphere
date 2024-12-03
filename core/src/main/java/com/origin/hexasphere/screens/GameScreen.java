package com.origin.hexasphere.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.origin.hexasphere.geometry.g3d.Hexasphere;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen
{
    private Mesh mesh;
    private Model model;
    private ShaderProgram shader;
    private ModelBatch batch;
    private PerspectiveCamera cam;
    private ModelInstance modelInstance;
    private Hexasphere hexasphere;
    //private Vector3 facing;

    @Override
    public void show()
    {
        Gdx.app.setLogLevel(Logger.DEBUG);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        batch = new ModelBatch();

        hexasphere = new Hexasphere(5);
        hexasphere.debugMesh(true, true);
        modelInstance = hexasphere.instance();
    }

    Vector3 hexPos = new Vector3(0f, 0f, 0f);
    Vector3 wCamRotationSpeed = new Vector3(0.0f, 0.0f, 0.02f);
    Vector3 sCamRotationSpeed = new Vector3(0f, 0f, -0.02f);
    Vector3 aCamRotationSpeed = new Vector3(-0.02f, 0f, 0f);
    Vector3 dCamRotationSpeed = new Vector3(0.02f, 0f, 0f);

    private Vector3 newDir = new Vector3();
    private float camZoomAmnt = 1f;
    private float minCamZoom = 4f;
    private float maxCamZoom = 12f;

    @Override
    public void render(float delta)
    {
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
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
            if(hexasphere.getCenter().dst(cam.position) > minCamZoom)
                cam.position.add(newDir.set(cam.direction).scl(camZoomAmnt));
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
            if(hexasphere.getCenter().dst(cam.position) < maxCamZoom)
                cam.position.add(newDir.set(cam.direction).scl(-camZoomAmnt));
        cam.lookAt(0f, 0f, 0f);
            //cam.position.set(cam.position.x+cam.direction.x, cam.position.y+cam.direction.y, cam.position.z-+cam.direction.z);


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
}
