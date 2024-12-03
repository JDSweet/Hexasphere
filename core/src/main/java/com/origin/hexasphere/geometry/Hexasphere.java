package com.origin.hexasphere.geometry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

//Read THIS: https://web.archive.org/web/20180808214504/http://donhavey.com:80/blog/tutorials/tutorial-3-the-icosahedron-sphere/

public class Hexasphere
{
    private Array<Point> points;
    private Array<Triangle> faces;
    private Model model;

    public Hexasphere()
    {
        points = new Array<Point>(true, 12);
        faces = new Array<Triangle>(true, 20);
        //createIco1();
        //createIco2();
        //createIco3();
        //createIco4();
        createIco5();
    }

    //Based on this article: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
    /*public void createIco1()
    {
        float tao = (float) (1 + Math.sqrt(5d)) / 2f; //MathUtils.PI / 2;
        //int offset = 0; //Because 4 = the number of color floats.
        float defaultSize = 1f; //Was 100f

        points.add(new Point(-1,  tao,  0, Color.RED));
        points.add(new Point( 1,  tao,  0, Color.GREEN));
        points.add(new Point(-1, -tao,  0, Color.BLUE));
        points.add(new Point( 1, -tao,  0, Color.LIGHT_GRAY));

        points.add(new Point( 0, -1,  tao, Color.RED));
        points.add(new Point( 0,  1,  tao, Color.GREEN));
        points.add(new Point( 0, -1, -tao, Color.BLUE));
        points.add(new Point( 0,  1, -tao, Color.LIGHT_GRAY));

        points.add(new Point( tao,  0, -1, Color.RED));
        points.add(new Point( tao,  0,  1, Color.GREEN));
        points.add(new Point(-tao,  0, -1, Color.BLUE));
        points.add(new Point(-tao,  0,  1, Color.LIGHT_GRAY));

        //Don't forget to add these faces to the faces array, or you'll be wondering why tf this isn't working.
        // 5 faces around point 0
        faces.add(new Triangle(this, 0, 11, 5, false));
        faces.add(new Triangle(this, 0, 5, 1, false));
        faces.add(new Triangle(this, 0, 1, 7, false));
        faces.add(new Triangle(this, 0, 7, 10, false));
        faces.add(new Triangle(this, 0, 10, 11, false));

        // 5 adjacent faces
        faces.add(new Triangle(this, 1, 5, 9, false));
        faces.add(new Triangle(this, 5, 11, 4, false));
        faces.add(new Triangle(this, 11, 10, 2, false));
        faces.add(new Triangle(this, 10, 7, 6, false));
        faces.add(new Triangle(this, 7, 1, 8, false));

        // 5 faces around point 3
        faces.add(new Triangle(this, 3, 9, 4, false));
        faces.add(new Triangle(this, 3, 4, 2, false));
        faces.add(new Triangle(this, 3, 2, 6, false));
        faces.add(new Triangle(this, 3, 6, 8, false));
        faces.add(new Triangle(this, 3, 8, 9, false));

        // 5 adjacent faces
        faces.add(new Triangle(this, 4, 9, 5, false));
        faces.add(new Triangle(this, 2, 4, 11, false));
        faces.add(new Triangle(this, 6, 2, 10, false));
        faces.add(new Triangle(this, 8, 6, 7, false));
        faces.add(new Triangle(this, 9, 8, 1, false));
    }*/

    //https://www.classes.cs.uchicago.edu/archive/2003/fall/23700/docs/handout-04.pdf
    public void createIco4()
    {
        float t = (float)(1.0f + Math.sqrt(5.0f)) / 2.0f;

        points.add(new Point(t, 1f, 0f));
        points.add(new Point(-t, 1f, 0f));
        points.add(new Point(t, -1f, 0f));
        points.add(new Point(-t, -1f, 0f));

        points.add(new Point(1f, 0f, t));
        points.add(new Point(1f, 0f, -t));
        points.add(new Point(-1f, 0f, t));
        points.add(new Point(-1f, 0f, -t));

        points.add(new Point(0f, t, 1f));
        points.add(new Point(0f, -t, 1f));
        points.add(new Point(0f, t, -1f));
        points.add(new Point(0f, -t, -1f));

        faces.add(new Triangle(this, 0, 8, 4));
        faces.add(new Triangle(this, 0, 5, 10));
        faces.add(new Triangle(this, 2, 4, 9));
        faces.add(new Triangle(this, 2, 11, 5));
        faces.add(new Triangle(this, 1, 6, 8));

        faces.add(new Triangle(this, 1, 10, 7));
        faces.add(new Triangle(this, 3, 9, 6));
        faces.add(new Triangle(this, 3, 7, 11));
        faces.add(new Triangle(this, 0, 10, 8));
        faces.add(new Triangle(this, 1, 8, 10));

        faces.add(new Triangle(this, 2, 9, 11));
        faces.add(new Triangle(this, 3, 9, 11));
        faces.add(new Triangle(this, 4, 2, 0));
        faces.add(new Triangle(this, 5, 0, 2));
        faces.add(new Triangle(this, 6, 1, 3));

        faces.add(new Triangle(this, 7, 3, 1));
        faces.add(new Triangle(this, 8, 6, 4));
        faces.add(new Triangle(this, 9, 4, 6));
        faces.add(new Triangle(this, 10, 5, 7));
        faces.add(new Triangle(this, 11, 7, 5));
    }

    private void createIco5()
    {
        float tao = 1.61803399f; //(float)(1.0f + Math.sqrt(5.0f)) / 2.0f;
        Point[] pnts = new Point[]
        {
            new Point(3, tao * 3, 0),
            new Point(-3, tao * 3, 0),
            new Point(3,-tao * 3,0),
            new Point(-3,-tao * 3,0),
            new Point(0,3,tao * 3),
            new Point(0,-3,tao * 3),
            new Point(0,3,-tao * 3),
            new Point(0,-3,-tao * 3),
            new Point(tao * 3,0,3),
            new Point(-tao * 3,0,3),
            new Point(tao * 3,0,-3),
            new Point(-tao * 3,0,-3)
        };
        Triangle[] fces = new Triangle[]
        {
            new Triangle(this, 0, 1, 4, false),
            new Triangle(this, 1, 9, 4, false),
            new Triangle(this, 4, 9, 5, false),
            new Triangle(this, 5, 9, 3, false),
            new Triangle(this, 2, 3, 7, false),
            new Triangle(this, 3, 2, 5, false),
            new Triangle(this, 7, 10, 2, false),
            new Triangle(this, 0, 8, 10, false),
            new Triangle(this, 0, 4, 8, false),
            new Triangle(this, 8, 2, 10, false),
            new Triangle(this, 8, 4, 5, false),
            new Triangle(this, 8, 5, 2, false),
            new Triangle(this, 1, 0, 6, false),
            new Triangle(this, 11, 1, 6, false),
            new Triangle(this, 3, 9, 11, false),
            new Triangle(this, 6, 10, 7, false),
            new Triangle(this, 3, 11, 7, false),
            new Triangle(this, 11, 6, 7, false),
            new Triangle(this, 6, 0, 10, false),
            new Triangle(this, 9, 1, 11, false)
        };

        for(Point p : pnts)
        {
            points.add(p);
        }

        for(Triangle f : fces)
        {
            faces.add(f);
        }
    }

    public void buildMesh()
    {

    }

    //Thank the Gods for this brave soul: https://gamedev.stackexchange.com/a/212473/60136
    public ModelInstance createModel()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();
        Color[] colors = new Color[] {
            Color.GREEN,
            Color.RED,
            Color.BLUE,
            Color.BROWN,
            Color.CHARTREUSE,
            Color.CORAL,
            Color.CYAN,
            Color.DARK_GRAY,
            Color.FIREBRICK,
            Color.FOREST,
            Color.GOLD,
            Color.LIME,
            Color.LIGHT_GRAY,
            Color.OLIVE,
            Color.SKY,
            Color.SALMON
        };

        b.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal |VertexAttributes.Usage.ColorUnpacked);
        for(int i = 0; i < points.size; i++)
        {
            Point point = points.get(i);

            b.vertex(
                point.getPosition(),
                point.getPosition(),
                colors[i],
                new Vector2(1, 1));
        }
        for(int i = 0; i < faces.size; i++)
        {
            b.index(faces.get(i).getIdx1());
            b.index(faces.get(i).getIdx2());
            b.index(faces.get(i).getIdx3());
        }
        Mesh mesh = b.end();
        modelBuilder.begin();
        modelBuilder.part("world", mesh,  GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
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
            vertsDebug += "point [" + p.getPosition().toString() + "]\n";
        }
        for(Triangle f : faces)
        {
            facesDebug += "face [" + f.toString() + "]\n";
        }
        Gdx.app.log("Vertices", "");
    }
}
