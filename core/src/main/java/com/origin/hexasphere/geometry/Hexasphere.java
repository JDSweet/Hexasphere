package com.origin.hexasphere.geometry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Hexasphere
{
    private Array<Point> points;
    private Array<Face> faces;
    private Model model;

    public Hexasphere()
    {
        points = new Array<Point>();
        faces = new Array<Face>();
    }

    public void createIcosahedron()
    {
        float tao = (float) (1 + Math.sqrt(5d)) / 2f; //MathUtils.PI / 2;
        float defaultSize = 1f; //Was 100f

        points.add(new Point(new Vector3(defaultSize, tao * defaultSize, 0f)));
        points.add(new Point(new Vector3(-defaultSize, tao * defaultSize, 0f)));
        points.add(new Point(new Vector3(defaultSize, -tao * defaultSize, 0f)));
        points.add(new Point(new Vector3(-defaultSize, -tao * defaultSize, 0f)));
        points.add(new Point(new Vector3(0, defaultSize, tao * defaultSize)));
        points.add(new Point(new Vector3(0, -defaultSize, tao * defaultSize)));
        points.add(new Point(new Vector3(0, defaultSize, -tao * defaultSize)));
        points.add(new Point(new Vector3(0, -defaultSize, -tao * defaultSize)));
        points.add(new Point(new Vector3(tao * defaultSize, 0f, defaultSize)));
        points.add(new Point(new Vector3(-tao * defaultSize, 0f, defaultSize)));
        points.add(new Point(new Vector3(tao * defaultSize, 0f, -defaultSize)));
        points.add(new Point(new Vector3(-tao * defaultSize, 0f, -defaultSize)));

        //Don't forget to add these faces to the faces array, or you'll be wondering why tf this isn't working.
        faces.add(new Face(this, 0, 1, 4, true));
        faces.add(new Face(this, 1, 9, 4, true));
        faces.add(new Face(this, 4, 9, 5, true));
        faces.add(new Face(this, 5, 9, 3, true));
        faces.add(new Face(this, 2, 3, 7, true));
        faces.add(new Face(this, 3, 2, 5, true));
        faces.add(new Face(this, 7, 10, 2, true));
        faces.add(new Face(this, 0, 8, 10, true));
        faces.add(new Face(this, 0, 4, 8, true));
        faces.add(new Face(this, 8, 2, 10, true));
        faces.add(new Face(this, 8, 4, 5, true));
        faces.add(new Face(this, 8, 5, 2, true));
        faces.add(new Face(this, 1, 0, 6, true));
        faces.add(new Face(this, 3, 9, 11, true));
        faces.add(new Face(this, 6, 10, 7, true));
        faces.add(new Face(this, 3, 11, 7, true));
        faces.add(new Face(this, 11, 6, 7, true));
        faces.add(new Face(this, 6, 0, 10, true));
        faces.add(new Face(this, 11, 1, 6, true));
        faces.add(new Face(this, 9, 1, 11, true));
    }

    public ModelInstance createModel()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();
        b.begin(VertexAttributes.Usage.Position);
        for(int i = 0; i < points.size; i++)
        {
            b.vertex(points.get(i).getPosition().x, points.get(i).getPosition().y, points.get(i).getPosition().z);
        }
        for(int i = 0; i < faces.size; i++)
        {
            b.index(faces.get(i).getIdx1());
            b.index(faces.get(i).getIdx2());
            b.index(faces.get(i).getIdx3());
        }
        Mesh mesh = b.end();
        modelBuilder.begin();
        modelBuilder.part("world", mesh, VertexAttributes.Usage.Position, new Material());
        this.model = modelBuilder.end();
        return new ModelInstance(model);
    }

    public Point getPointAt(int index)
    {
        return points.get(index);
    }

    public void debugMesh()
    {
        String vertsDebug = "";
        String facesDebug = "";
        for(Point p : points)
        {

        }
        Gdx.app.log("Vertices", "");
    }
}
