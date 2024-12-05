package com.origin.hexasphere.geometry.g3d;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.origin.hexasphere.tilemap.IcoSphereTile;
import it.unimi.dsi.fastutil.floats.*;

//Read THIS: https://web.archive.org/web/20180808214504/http://donhavey.com:80/blog/tutorials/tutorial-3-the-icosahedron-sphere/


public class Hexasphere
{
    private Vector3 center;
    private Array<Point> points;
    private Array<Triangle> faces;
    private Model model;
    private float radius = 3f;
    private int subdivisions;

    //ArrayMap<Float, ArrayMap<Float, IcoSphereTile>> latLon;
    //Float2ObjectArrayMap<Float2ObjectArrayMap<IcoSphereTile>> latLon;
    ArrayMap<Vector2, IcoSphereTile> latLon;

    public Hexasphere(int subdivisions)
    {
        points = new Array<Point>(true, 12);
        faces = new Array<Triangle>(true, 20);
        //this.latLon = new Float2ObjectArrayMap<Float2ObjectArrayMap<IcoSphereTile>>(); //new ArrayMap<Float, ArrayMap<Float, IcoSphereTile>>();
        this.latLon = new ArrayMap<Vector2, IcoSphereTile>();
        this.center = new Vector3(0f, 0f, 0f);
        this.subdivisions = subdivisions;

        createIcosahedron();

        subdivide(this.subdivisions);

        tileIcosphere();

        buildMesh();
    }

    //Based on the following articles:
    //      1. http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
    //      2. https://www.classes.cs.uchicago.edu/archive/2003/fall/23700/docs/handout-04.pdf
    private void createIcosahedron()
    {
        float tao = 1.61803399f; //(float)(1.0f + Math.sqrt(5.0f)) / 2.0f;

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

        Point[] pnts = new Point[]
        {
            new Point(radius, tao * radius, 0),
            new Point(-radius, tao * radius, 0),
            new Point(radius,-tao * radius,0),
            new Point(-radius,-tao * radius,0),
            new Point(0, radius,tao * radius),
            new Point(0,-radius,tao * radius),
            new Point(0, radius,-tao * radius),
            new Point(0,-radius,-tao * radius),
            new Point(tao * radius,0, radius),
            new Point(-tao * radius,0, radius),
            new Point(tao * radius,0,-radius),
            new Point(-tao * radius,0,-radius)
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

        for(int i = 0; i < pnts.length; i++)
        {
            Point p = pnts[i];
            Color c = colors[i];
            p.setColor(c);
            points.add(p);
        }

        for(Triangle f : fces)
        {
            faces.add(f);
        }
    }

    // Not implemented yet...
    public void tileIcosphere()
    {
        for(int i = 0; i < points.size; i++)
        {
            Point p = points.get(i);
            IcoSphereTile tile = new IcoSphereTile(this, p, IcoSphereTile.TileType.GRASS);
            float polarAngle = (float)Math.acos(p.getPosition().z / getRadius());
            float azimuthAngle = (float)Math.atan2(p.getPosition().y, p.getPosition().x);
            float lat = 90f - polarAngle;
            float lon = azimuthAngle;
            tile.setLatLon(lat, lon);
            latLon.put(tile.getLatLon(), tile);
            //IcoSphereTile(this, points.get(i), IcoSphereTile.TileType.GRASS);
        }
    }

    public void subdivide(int times)
    {
        // We are caching the old array of triangles because
        // removeFace (called in the Triangle.subdivide() method) mutates our main array of triangles ("faces").
        // If we just use "faces," then faces are removed and added during the loop,
        // leading to infinite regression
        // (size is always increasing faster than i is incrementing - by a factor
        // of 4 new triangles per triangle iterated through)
        // This is super inefficient for larger meshes/more subdivisions.
        // We should be caching only the new triangles we create, and adding them to the main faces
        for(int i = 0; i < times; i++)
        {
            var newFaces = new Array<Triangle>(faces);
            for(int j = 0; j < newFaces.size; j++)
            {
                newFaces.get(j).subdivide();
            }
            //newFaces = new Array<Triangle>(faces);
        }
    }

    public void addRawTriangle(Point p1, Point p2, Point p3)
    {
        short p1Idx, p2Idx, p3Idx;
        p1Idx = (short)addPointIfDoesNotExist(p1);
        p2Idx = (short)addPointIfDoesNotExist(p2);
        p3Idx = (short)addPointIfDoesNotExist(p3);

        Triangle tri = new Triangle(this, p1Idx, p2Idx, p3Idx);
        faces.add(tri);
        getPointAt(tri.getIdx1()).setIndex(tri.getIdx1());
        getPointAt(tri.getIdx2()).setIndex(tri.getIdx2());
        getPointAt(tri.getIdx3()).setIndex(tri.getIdx3());
    }

    //This doesn't remove vertices, it just removes the specific triangle/collection of indices that form the triangle.
    public void removeTriangle(Triangle tri)
    {
        this.faces.removeValue(tri, true);
    }

    public void buildMesh()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();

        b.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal |VertexAttributes.Usage.ColorUnpacked);
        for(int i = 0; i < points.size; i++)
        {
            Point point = points.get(i);

            b.vertex(
                point.getPosition(),
                point.getPosition(),
                point.getColor(),
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
    }

    //Thank the Gods for this brave soul: https://gamedev.stackexchange.com/a/212473/60136
    public ModelInstance instance()
    {
        return new ModelInstance(model);
    }

    public Point getPointAt(int index)
    {
        return points.get(index);
    }

    // Adds the point and returns its index if it does not exist.
    // Returns the index of the point if it does exist.
    public int addPointIfDoesNotExist(Point point)
    {
        int retval = -1;
        for(int i = 0; i < points.size; i++)
        {
            Point other = points.get(i);
            if(other == point || other.equals(point))
            {
                retval = i;
                //Gdx.app.debug("Hexasphere", "Point " + point.getPosition() + " exists! Returning " + retval);
                break;
            }
        }
        if(retval == -1)
        {
            points.add(point);
            retval = points.size - 1;
            //Gdx.app.debug("Hexasphere", "Point " + point.getPosition() + " does not exist! Returning " + retval);
        }

        return retval;
    }

    public float getRadius()
    {
        return this.radius;
    }

    public Vector3 getCenter()
    {
        return this.center;
    }

    public void debugMesh(boolean verts, boolean faces)
    {
        String vertsDebug = "\n";
        String facesDebug = "\n";

        if(verts)
            for(Point p : points)
                vertsDebug += "point [" + p.getPosition().toString() + "]\n";
        vertsDebug += "Vert Count: " + points.size;
        if(faces)
            for(Triangle f : this.faces)
                facesDebug += "face [" + f.toString() + "]\n";
        facesDebug += "Face Count: " + this.faces.size;
        Gdx.app.log("Vertices", vertsDebug);
        Gdx.app.log("Faces", facesDebug);
    }

    public void debugTiles()
    {
        String tileDebug = "\n";
        for(IcoSphereTile tile : latLon.values())
        {
            tileDebug += "\nTile: " + tile.getPoint().getPosition();
        }
        Gdx.app.log("Tiles", tileDebug);
    }
}
