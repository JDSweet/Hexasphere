package com.origin.hexasphere.geometry;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
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

    public Hexasphere()
    {
        points = new Array<Point>();
        faces = new Array<Face>();
    }

    public void createIcosahedron()
    {
        float tao = MathUtils.PI / 2;
        float defaultSize = 5f; //Was 100f

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
        new Face(this, (short) 0, (short) 1, (short) 4, true);
        new Face(this, (short) 1, (short) 9, (short) 4, true);
        new Face(icosahedronCorners[4], icosahedronCorners[9], icosahedronCorners[5], false);
        new Face(icosahedronCorners[5], icosahedronCorners[9], icosahedronCorners[3], false);
        new Face(icosahedronCorners[2], icosahedronCorners[3], icosahedronCorners[7], false);
        new Face(icosahedronCorners[3], icosahedronCorners[2], icosahedronCorners[5], false);
        new Face(icosahedronCorners[7], icosahedronCorners[10], icosahedronCorners[2], false);
        new Face(icosahedronCorners[0], icosahedronCorners[8], icosahedronCorners[10], false);
        new Face(icosahedronCorners[0], icosahedronCorners[4], icosahedronCorners[8], false);
        new Face(icosahedronCorners[8], icosahedronCorners[2], icosahedronCorners[10], false);
        new Face(icosahedronCorners[8], icosahedronCorners[4], icosahedronCorners[5], false);
        new Face(icosahedronCorners[8], icosahedronCorners[5], icosahedronCorners[2], false);
        new Face(icosahedronCorners[1], icosahedronCorners[0], icosahedronCorners[6], false);
        new Face(icosahedronCorners[3], icosahedronCorners[9], icosahedronCorners[11], false);
        new Face(icosahedronCorners[6], icosahedronCorners[10], icosahedronCorners[7], false);
        new Face(icosahedronCorners[3], icosahedronCorners[11], icosahedronCorners[7], false);
        new Face(icosahedronCorners[11], icosahedronCorners[6], icosahedronCorners[7], false);
        new Face(icosahedronCorners[6], icosahedronCorners[0], icosahedronCorners[10], false);
        new Face(icosahedronCorners[11], icosahedronCorners[1], icosahedronCorners[6], false);
        new Face(icosahedronCorners[9], icosahedronCorners[1], icosahedronCorners[11], false);
    }

    public ModelInstance createModel()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();
        b.begin(VertexAttributes.Usage.Position);
        for(int i = 0; i <= points.size; i++)
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
        return new ModelInstance(modelBuilder.end());
    }
}
