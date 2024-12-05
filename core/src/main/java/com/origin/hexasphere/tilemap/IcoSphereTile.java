package com.origin.hexasphere.tilemap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
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

    public IcoSphereTile(Hexasphere world, Point point, TileType tileType)
    {
        this.world = world;
        this.geometryIndex = point.getIndex();
        this.type = tileType;
        point.setColor(type.getColor());
        this.latLon = new LatLon();
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

    public void setTileType(TileType type)
    {
        this.type = type;
        world.registerTileForUpdate(this);
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
