package com.origin.hexasphere.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.IntArray;
import com.origin.hexasphere.coordinates.LatLon;
import com.origin.hexasphere.geometry.g3d.Hexasphere;
import com.origin.hexasphere.geometry.g3d.Point;
import com.origin.hexasphere.util.IColorHolder;

public class IcoSphereTile
{
    private Hexasphere world;
    private int geometryIndex;
    private TileType type;
    private LatLon latLon;
    private int id;
    private Array<IcoSphereTile> neighbors;

    private static int maxTileID = 0;

    public IcoSphereTile(Hexasphere world, Point point, TileType tileType)
    {
        this.world = world;
        this.geometryIndex = point.getIndex();
        this.type = tileType;
        point.setColor(type.getColor());
        this.latLon = new LatLon();
        this.id = maxTileID++;
        this.neighbors = new Array<>();
    }

    public void createAdjacencies()
    {
        IntArray pointAdjacencies = this.getPoint().getAdjacencies();
        Gdx.app.log("Adjacency Debug", "" + pointAdjacencies.size);
        for(int i = 0; i < pointAdjacencies.size; i++)
        {
            this.neighbors.add(world.getTileAt(pointAdjacencies.get(i)));
        }
    }

    public Array<IcoSphereTile> getAdjacencies()
    {
        return this.neighbors;
    }

    public void setLatLon(float lat, float lon)
    {
        this.latLon.set(lat, lon);
    }

    public LatLon getLatLon()
    {
        return this.latLon;
    }

    public Point getPoint()
    {
        return world.getPointAt(geometryIndex);
    }

    public Color getColor()
    {
        return type.getColor();
    }

    public void setTileType(String type)
    {
        if(type.equalsIgnoreCase("grass"))
            setTileType(TileType.GRASS);
        else if(type.equalsIgnoreCase("mountain"))
            setTileType(TileType.MOUNTAIN);
        else if(type.equalsIgnoreCase("hill"))
            setTileType(TileType.HILLS);
        else if(type.equalsIgnoreCase("ocean"))
            setTileType(TileType.OCEAN);
        else if(type.equalsIgnoreCase("desert"))
            setTileType(TileType.DESERT);
        else
            setTileType(TileType.GRASS);
    }



    public void setTileType(TileType type)
    {
        this.type = type;
        world.registerTileForUpdate(this);
    }

    public Hexasphere getWorld()
    {
        return world;
    }

    public int getID()
    {
        return this.id;
    }

    public enum TileType implements IColorHolder
    {
        GRASS
        {
            @Override
            public Color getColor()
            {
                return Color.GREEN;
            }
        },
        MOUNTAIN
        {
            @Override
            public Color getColor()
            {
                return Color.LIGHT_GRAY;
            }
        },
        OCEAN
        {
            @Override
            public Color getColor()
            {
                return Color.BLUE;
            }
        },
        HILLS
        {
            @Override
            public Color getColor()
            {
                return Color.BROWN;
            }
        },
        DESERT
        {
            @Override
            public Color getColor()
            {
                return Color.ORANGE;
            }
        };
    }
}
