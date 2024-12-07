package com.origin.hexasphere.geometry.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.origin.hexasphere.tilemap.IcoSphereTile;

//Read THIS: https://web.archive.org/web/20180808214504/http://donhavey.com:80/blog/tutorials/tutorial-3-the-icosahedron-sphere/

/*
* Known issues:
*   1. The maximum number of cells is rather small compared to the implementation this is derived from.
*       This issue stems from LibGDX's use of signed shorts for indices (as Java lacks unsigned shorts).
*       As a result, the only way to increase the resolution would be to divide the mesh into chunks
*       (perhaps one chunk per original pentagonal face?) and to subdivide these chunks. This would dramatically
*       complicate the process of creating geometry (as we'd now have to track all occurrences of the same Point
*       in each mesh, and modify all meshes that a point occurs in when we want to make a change, because there would
*       be some overlap).
* */
public class Hexasphere
{
    private Vector3 center;
    private Array<Point> points;
    private Array<Triangle> faces;
    private Mesh mesh;
    private Model model;
    private ModelInstance instance;
    private float radius;
    private int subdivisions;
    private Queue<IcoSphereTile> tilesToUpdate;
    private float[] vertices;

    Array<IcoSphereTile> tiles;

    public Hexasphere(float radius, int subdivisions)
    {
        points = new Array<Point>(true, 12);
        faces = new Array<Triangle>(true, 20);
        this.tilesToUpdate = new Queue<>();
        this.tiles = new Array<IcoSphereTile>(true, 15000);
        this.center = new Vector3(0f, 0f, 0f);
        this.subdivisions = subdivisions;
        this.radius = radius;

        createIcosahedron();

        subdivide(this.subdivisions);

        tileIcosphere();

        createAdjacencies();

        buildMesh();

        this.instance = instance();

        cacheVertexData();

        //debugAdjacencies();

        //debugIndices();
    }

    public void createAdjacencies()
    {
        for(int i = 0; i < tiles.size; i++)
        {
            IcoSphereTile t = tiles.get(i);
            Point p = t.getPoint();
            for(int j = 0; j < faces.size; j++)
            {
                Triangle f = faces.get(j);
                if(p.getIndex() == f.getIdx1())
                {
                    p.addAdjacency(f.getIdx2());
                    p.addAdjacency(f.getIdx3());
                }
                if(p.getIndex() == f.getIdx2())
                {
                    p.addAdjacency(f.getIdx1());
                    p.addAdjacency(f.getIdx3());
                }
                if(p.getIndex() == f.getIdx3())
                {
                    p.addAdjacency(f.getIdx1());
                    p.addAdjacency(f.getIdx2());
                }
            }
            t.createAdjacencies();
            //Gdx.app.log("Adjacency Count for Tile " + t.getID(), t.getAdjacencies().size);
        }
    }

    /*public void debugIndices()
    {
        StringBuilder bldr = new StringBuilder();
        for(int i = 0; i < tiles.size; i++)
        {
            bldr.append("Index ");
            bldr.append(i);
            bldr.append(": ");
            bldr.append(getIndexOccurrences(i));
            bldr.append("\n");
        }
        Gdx.app.log("Index Debug", bldr.toString());
    }

    public int getIndexOccurrences(int search)
    {
        int occur = 0;
        for(int i = 0; i < faces.size; i++)
        {
            Triangle face = faces.get(i);
            if(face.getIdx1() == search)
                occur++;
            if(face.getIdx2() == search)
                occur++;
            if(face.getIdx3() == search)
                occur++;
        }
        return occur;
    }

    public void debugAdjacencies()
    {
        for(int i = 0; i < points.size; i++)
        {
            IntArray adj = getPointAt(i).getAdjacencies();
            for(int j = 0; j < adj.size; j++)
            {
                Gdx.app.log("Adj Debug", "" + j);
                getTileAt(j).setTileType(IcoSphereTile.TileType.HILLS);
            }
        }
    }

    private void createPointAdjacencies()
    {
        for(Triangle t : faces)
        {
            t.getPoint1().addAdjacency(t.getIdx3());
            t.getPoint1().addAdjacency(t.getIdx2());

            t.getPoint2().addAdjacency(t.getIdx3());
            t.getPoint2().addAdjacency(t.getIdx1());

            t.getPoint3().addAdjacency(t.getIdx2());
            t.getPoint3().addAdjacency(t.getIdx1());
        }
    }

    private void createTileAdjacencies()
    {
        for(IcoSphereTile t : tiles)
        {
            t.createAdjacencies();
        }
    }*/

    //For every point a
    //      for every triangle b,
    //          if a is in b then
    //              Add the other points in b to a.
    /*private void createPointAdjacencies()
    {
        for(int i = 0; i < points.size; i++)
        {
            Point a = getPointAt(i);
            for(int j = 0; j < faces.size; j++)
            {
                Triangle face = faces.get(j);
                if(face.containsPoint(a))
                {
                    Point f1 = face.getPoint1();
                    Point f2 = face.getPoint2();
                    Point f3 = face.getPoint3();
                    if(a.equals(f1))
                    {
                        a.addAdjacency(f2.getIndex());
                        a.addAdjacency(f3.getIndex());
                    }
                    else if(a.equals(f2))
                    {
                        a.addAdjacency(f1.getIndex());
                        a.addAdjacency(f3.getIndex());
                    }
                    else if(a.equals(f3))
                    {
                        a.addAdjacency(f2.getIndex());
                        a.addAdjacency(f1.getIndex());
                    }
                }
            }
        }

        for(IcoSphereTile tile : tiles)
        {
            tile.createPointAdjacencies();
        }
    }*/

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
            new Point(radius, tao * radius, 0, true),
            new Point(-radius, tao * radius, 0, true),
            new Point(radius,-tao * radius,0, true),
            new Point(-radius,-tao * radius,0, true),
            new Point(0, radius,tao * radius, true),
            new Point(0,-radius,tao * radius, true),
            new Point(0, radius,-tao * radius, true),
            new Point(0,-radius,-tao * radius, true),
            new Point(tao * radius,0, radius, true),
            new Point(-tao * radius,0, radius, true),
            new Point(tao * radius,0,-radius, true),
            new Point(-tao * radius,0,-radius, true)
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
            p.setIndex(i);
        }

        for(Triangle f : fces)
        {
            faces.add(f);
        }

        //handleOriginalIcosahedron(pnts, fces);
    }

    /*private void handleOriginalIcosahedron(Point[] pnts, Triangle[] tris)
    {
        for(Triangle tri : tris)
        {
            pnts[tri.getIdx1()].setIndex(tri.getIdx1());
            pnts[tri.getIdx2()].setIndex(tri.getIdx2());
            pnts[tri.getIdx3()].setIndex(tri.getIdx3());
        }
    }*/


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
            tiles.add(tile);
            if(p.isOriginal())
                tile.setTileType(IcoSphereTile.TileType.DESERT);
            //tiles.put(tile.getLatLon(), tile);
            //IcoSphereTile(this, points.get(i), IcoSphereTile.TileType.GRASS);
        }
    }

    public void desertifyOriginals()
    {
        for(IcoSphereTile t : tiles)
        {
            if(t.getPoint().isOriginal())
            {
                t.setTileType(IcoSphereTile.TileType.DESERT);
            }
        }
    }

    public void grassifyNonOriginals()
    {
        int ctr = 0;
        for(IcoSphereTile t : tiles)
        {
            if(!t.getPoint().isOriginal() && ctr % 2 == 0)
                t.setTileType(IcoSphereTile.TileType.GRASS);
            else if(!t.getPoint().isOriginal() && ctr % 2 == 1)
                t.setTileType(IcoSphereTile.TileType.MOUNTAIN);
            ctr++;
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
        modelBuilder.part("world", mesh,  /*GL20.GL_TRIANGLES*/ GL20.GL_TRIANGLES, new Material(/*ColorAttribute.createDiffuse(Color.WHITE)*/));
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

        //Point 1
        getPointAt(tri.getIdx1()).setIndex(tri.getIdx1());

        //Point 2
        getPointAt(tri.getIdx2()).setIndex(tri.getIdx2());

        //Point 3
        getPointAt(tri.getIdx3()).setIndex(tri.getIdx3());
    }

    //This doesn't remove vertices, it just removes the specific triangle/collection of indices that form the triangle.
    public void removeTriangle(Triangle tri)
    {
        this.faces.removeValue(tri, true);
    }

    // Adds the point and returns its index if it does not exist.
    // Returns the index of the point if it does exist.
    // Note: If the circle's radius is too small or large for a given number of subdivisions, point comparisons begin to return
    // false positives because of floating point imprecision. This leads to the resulting sphere being generally correct
    // (insofar as it is, visually, a sphere), but it leads to the generation of weird artifacts, and the improper
    // rendering of tiles or anything that heavily relies on the correct construction of triangles and vertex positions.
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
        for(IcoSphereTile t : tiles)
        {
            t.setTileType(IcoSphereTile.TileType.OCEAN);
        }
    }

    public void grassify()
    {
        for(IcoSphereTile t : tiles)
        {
            t.setTileType(IcoSphereTile.TileType.GRASS);
        }
    }

    public IcoSphereTile getClosestTile(Vector3 to)
    {
        return getClosestTile(to.x, to.y, to.z);
    }

    public IcoSphereTile getClosestTile(float x, float y, float z)
    {
        IcoSphereTile closestTile = null;
        float curDistance = 0f;
        float lastDistance = 0f;
        for(int i = 0; i < tiles.size; i++)
        {
            IcoSphereTile tile = tiles.get(i);
            if(closestTile == null)
            {
                closestTile = tile;
                curDistance = tile.getPoint().getPosition().dst(x, y, z);
                lastDistance = curDistance;
                continue;
            }
            curDistance = tile.getPoint().getPosition().dst(x, y, z);
            if(curDistance < lastDistance) {
                closestTile = tile;
                lastDistance = curDistance;
            }
        }
        return closestTile;
    }

    public IcoSphereTile getTileAt(int index)
    {
        return tiles.get(index);
    }

    public void render(ModelBatch batch)
    {
        batch.render(instance);
    }

    public void update()
    {
        if(isTileMapDirty())
        {
            updateTileGeometry();
        }
    }

    private boolean isTileMapDirty()
    {
        return this.tilesToUpdate.notEmpty();
    }

    //This method updates the color of the vertex associated with any tile that has changed since the last update.
    public void updateTileGeometry()
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
        for(IcoSphereTile tile : tiles)
        {
            //tileDebug += "\nTile: " + tile.getLatLon();
        }
        /*for(IcoSphereTile tile : tiles.values())
            tileDebug += "\nTile: " + tile.getLatLon();*/
        Gdx.app.log("Tiles", tileDebug + tiles.size);
    }

    public Vector3 projectToSphere(Vector3 objVector)
    {
        //Magnitude = sqrt((x₁ - x₀)² + (y₁ - y₀)² + (z₁ - z₀)²)
        float xDistance = (float)(Math.pow((objVector.x-center.x), 2d));
        float yDistance = (float)(Math.pow((objVector.y-center.y), 2d));
        float zDistance = (float)(Math.pow((objVector.z-center.z), 2d));
        float distance = (float)Math.sqrt((xDistance + yDistance + zDistance));
        float scaleFactor = getRadius() / distance;
        objVector.set(objVector.x * scaleFactor, objVector.y * scaleFactor, objVector.z * scaleFactor);

        /*float xDistance = (float)(Math.pow((double)(objVector.x-centerVector.x), 2d));
        float yDistance = (float)(Math.pow((double)(objVector.y-centerVector.y), 2d));
        float zDistance = (float)(Math.pow((double)(objVector.z-centerVector.z), 2d));
        float magnitude = (float)Math.sqrt((xDistance + yDistance + zDistance)) / 1.95f;
        objVector.x /= magnitude * hexasphere.getRadius();
        objVector.y /= magnitude * hexasphere.getRadius();
        objVector.z /= magnitude * hexasphere.getRadius();/*
        //float magnitude = (float)Math.sqrt((xDistance + yDistance + zDistance));
        //objVector.x /= magnitude; //* hexasphere.getRadius();
        //objVector.y /= magnitude; //* hexasphere.getRadius();
        //objVector.z /= magnitude; //* hexasphere.getRadius();
        //objVector.set(objVector.dst(centerVector));*/

        return objVector;
    }

    public void everyOther()
    {
        for(int i = 0; i < tiles.size; i++)
        {
            if(i % 2 == 1)
                tiles.get(i).setTileType(IcoSphereTile.TileType.GRASS);
        }
    }
}
