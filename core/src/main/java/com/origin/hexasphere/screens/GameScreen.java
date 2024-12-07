package com.origin.hexasphere.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Logger;
import com.origin.hexasphere.geometry.g2d.HexasphereGUI;
import com.origin.hexasphere.geometry.g3d.Hexasphere;
import com.origin.hexasphere.tilemap.IcoSphereTile;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen, InputProcessor
{
    private InputMultiplexer plexer;

    private Mesh mesh;
    private Model model;
    private ShaderProgram shader;
    private ModelBatch batch;
    private PerspectiveCamera cam;
    private ModelInstance modelInstance;
    private Hexasphere hexasphere;
    private HexasphereGUI gui;
    //private Vector3 facing;

    //For whatever reason, the setting the tile color doesn't work when we don't subdivide the hexagon.
    //This means, that some logic is dependent on subdivision.
    @Override
    public void show()
    {
        Gdx.app.setLogLevel(Logger.DEBUG);
        plexer = new InputMultiplexer();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        batch = new ModelBatch();
        gui = new HexasphereGUI();

        hexasphere = new Hexasphere(3f, 5);
        hexasphere.debugMesh(false, false);
        hexasphere.tileIcosphere();
        hexasphere.oceanify();
        hexasphere.debugTiles();
        modelInstance = hexasphere.instance();

        plexer.addProcessor(this);
        plexer.addProcessor(gui);
        Gdx.input.setInputProcessor(plexer);
    }

    Vector3 hexPos = new Vector3(0f, 0f, 0f);
    Vector3 wCamRotationSpeed = new Vector3(0.0f, 0.0f, 0.02f);
    Vector3 sCamRotationSpeed = new Vector3(0f, 0f, -0.02f);
    Vector3 aCamRotationSpeed = new Vector3(-0.02f, 0f, 0f);
    Vector3 dCamRotationSpeed = new Vector3(0.02f, 0f, 0f);

    private Vector3 newDir = new Vector3();
    private float camZoomAmnt = 0.2f;
    private float minCamZoom = 1f;
    private float maxCamZoom = 52f;

    @Override
    public void render(float delta)
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        hexasphere.update();

        cam.lookAt(0f, 0f, 0f);
        cam.update();

        handleKeyInput();

        batch.begin(cam);
        hexasphere.render(batch);
        batch.end();

        gui.act();
        gui.draw();
    }

    private void handleKeyInput()
    {
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

    @Override
    public boolean keyDown(int keycode) {
        if(gui.isEditingEnabled())
        {
            if(keycode == Input.Keys.SPACE) {
                hexasphere.oceanify();
                hexasphere.desertifyOriginals();
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Gdx.app.log("CamPos Debug", cam.position.toString());

        // This code kind of works, in that it projects a point to the sphere and gets the closest point, but it
        // fumbles a bit when the camera is not directly facing a tile.
        // It basically gets the user's touch position, and uses the camera to
        // get the world coordinate of a tile, and uses that world coordinate to look up the tile.
        /*Vector3 touchPos = new Vector3(screenX, screenY, 0f);
        Gdx.app.log("TouchPos", touchPos.toString());

        Vector3 projectedTouchPos = cam.unproject(touchPos);
        projectedTouchPos.z = cam.position.z;
        Gdx.app.log("Projected TouchPos", projectedTouchPos.toString());

        Vector3 rayCastOutput = sr.rayCastToSphere(projectedTouchPos, hexasphere.getCenter(), cam.direction, hexasphere.getRadius());
        Gdx.app.log("Raycast Output Debug", rayCastOutput.toString());
        Gdx.app.log("Camera Direction Debug", cam.direction.toString());*/

        /*Vector3 touchPos = new Vector3(screenX, screenY, 0f);
        Vector3 projTouchPos = cam.unproject(touchPos);
        Vector3 touchRay = cam.direction.cpy().sub(projTouchPos); // Get direction from touch to camera

        //sr.rayCastToSphere()
        Vector3 rayCastOutput = sr.rayCastToSphere(cam.position, hexasphere.getCenter(), touchRay, hexasphere.getRadius());

        // We could also possibly try the reverse of this operation:

        IcoSphereTile touched = hexasphere.getClosestTile(hexasphere.projectToSphere(rayCastOutput));
        touched.setTileType(IcoSphereTile.TileType.GRASS);
        ;*/
        //hexasphere.everyOther();

        // Okay, so eventually I stumbled into pick rays.
        // A pick-ray is basically a ray extending from the camera's viewport.
        // So, if we pass a pick-ray to Intersector, it correctly translates from 2D click space into the 3D position of whatever was touched.
        if(gui.isEditingEnabled())
        {
            Ray ray = cam.getPickRay(screenX, screenY);
            Vector3 intersection = new Vector3();
            boolean intersects = Intersector.intersectRaySphere(ray, hexasphere.getCenter(), hexasphere.getRadius(), intersection);
            if(intersects)
            {
                IcoSphereTile touched = hexasphere.getClosestTile(intersection);
                touched.setTileType(gui.getSelection());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
