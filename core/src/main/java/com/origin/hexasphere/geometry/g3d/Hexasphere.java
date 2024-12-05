package com.origin.hexasphere.geometry.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Queue;
import com.origin.hexasphere.tilemap.IcoSphereTile;

//Read THIS: https://web.archive.org/web/20180808214504/http://donhavey.com:80/blog/tutorials/tutorial-3-the-icosahedron-sphere/


public class Hexasphere
{
    private Vector3 center;
    private Array<Point> points;
    private Array<Triangle> faces;
    private Mesh mesh;
    private Model model;
    private float radius = 3f;
    private int subdivisions;
    private Queue<IcoSphereTile> tilesToUpdate;
    private float[] vertices;

    //Organized by latitude and longitude
    //ArrayMap<Float, ArrayMap<Float, IcoSphereTile>> tiles;
    //Float2ObjectArrayMap<Float2ObjectArrayMap<IcoSphereTile>> tiles;
    ArrayMap<Vector2, IcoSphereTile> tiles;

    public Hexasphere(int subdivisions)
    {
        points = new Array<Point>(true, 12);
        faces = new Array<Triangle>(true, 20);
        this.tilesToUpdate = new Queue<>();
        //this.tiles = new Float2ObjectArrayMap<Float2ObjectArrayMap<IcoSphereTile>>(); //new ArrayMap<Float, ArrayMap<Float, IcoSphereTile>>();
        this.tiles = new ArrayMap<Vector2, IcoSphereTile>();
        this.center = new Vector3(0f, 0f, 0f);
        this.subdivisions = subdivisions;

        createIcosahedron();

        subdivide(this.subdivisions);

        tileIcosphere();

        buildMesh();

        cacheVertexData();
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

    //https://stackoverflow.com/questions/10473852/convert-latitude-and-longitude-to-point-in-3d-space
    public void tileIcosphere()
    {
        for(int i = 0; i < points.size; i++)
        {
            Point p = points.get(i);
            IcoSphereTile tile = new IcoSphereTile(this, p, IcoSphereTile.TileType.GRASS);
            float polarAngle = (float)Math.acos(p.getPosition().z / getRadius());
            float azimuthAngle = (float)Math.atan2(p.getPosition().y, p.getPosition().x);
            float lat = 90f - polarAngle * 180f / MathUtils.PI;
            float lon = azimuthAngle * 180f / MathUtils.PI;
            tile.setLatLon(lat, lon);
            tiles.put(tile.getLatLon(), tile);
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

    //x, y, z, n, o, r, r, g, b, a
    public void buildMesh()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder b = new MeshBuilder();

        b.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0)));
        //b.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal |VertexAttributes.Usage.ColorUnpacked | VertexAttributes.Usage.TextureCoordinates);
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
        this.mesh = b.end();
        modelBuilder.begin();
        modelBuilder.part("world", mesh,  GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
        this.model = modelBuilder.end();
    }

    private void cacheVertexData()
    {
        //Divide to convert bytes to floats. The vertex size is in bytes.
        this.vertices = new float[mesh.getNumVertices() * mesh.getVertexSize() / 4];
        this.vertices = mesh.getVertices(vertices);
        Gdx.app.log("Vertex Storage Debug", "Vertex Size " + vertices.length);
    }

    //Thank the Gods for this brave soul: https://gamedev.stackexchange.com/a/212473/60136
    public ModelInstance instance()
    {
        return new ModelInstance(model);
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

    public void oceanify()
    {
        for(IcoSphereTile t : tiles.values())
        {
            t.setTileType(IcoSphereTile.TileType.OCEAN);
        }
    }

    //This method updates the color of the vertex associated with any tile that has changed since the last update.
    public void updateGeometry()
    {
        while(this.tilesToUpdate.notEmpty())
        {
            int positionOffset = mesh.getVertexAttributes().getOffset(VertexAttributes.Usage.Position);
            int colorOffset = mesh.getVertexAttributes().getOffset(VertexAttributes.Usage.ColorUnpacked);
            int uvOffset = mesh.getVertexAttributes().getOffset(VertexAttributes.Usage.TextureCoordinates);
            int norOffset = mesh.getVertexAttributes().getOffset(VertexAttributes.Usage.Normal);
            //int vertexSize = mesh.getVertexSize() / 4;

            IcoSphereTile tile = tilesToUpdate.removeFirst();
            int idx = tile.getPoint().getIndex();
            int rIdx = (idx * mesh.getVertexSize()/4);
            rIdx += positionOffset + colorOffset;
            vertices[rIdx] = tile.getColor().r;
            vertices[rIdx + 1] = tile.getColor().g;
            vertices[rIdx + 2] = tile.getColor().b;
            vertices[rIdx + 3] = tile.getColor().a;
        }
        mesh.setVertices(vertices);
    }

    //Registers the specified tile to be updated next time the geometry for the world is updated.
    public void registerTileForUpdate(IcoSphereTile tile)
    {
        this.tilesToUpdate.addLast(tile);
    }

    public Point getPointAt(int index)
    {
        return points.get(index);
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
        for(IcoSphereTile tile : tiles.values())
        {
            tileDebug += "\nTile: " + tile.getLatLon();
        }
        Gdx.app.log("Tiles", tileDebug);
    }
}
