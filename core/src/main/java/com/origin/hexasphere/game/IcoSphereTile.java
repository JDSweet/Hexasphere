package com.origin.hexasphere.game;

import com.badlogic.gdx.graphics.Color;
import com.origin.hexasphere.geometry.g3d.Hexasphere;
import com.origin.hexasphere.geometry.g3d.Point;
import com.origin.hexasphere.util.ColorHolder;

public class IcoSphereTile
{
    private Hexasphere world;
    private int geometryIndex;
    private TileType type;

    public IcoSphereTile(Hexasphere world, Point point, TileType tileType)
    {
        this.world = world;
        this.geometryIndex = point.getIndex();
        this.type = tileType;
        point.setColor(type.getColor());
    }

    public Point getPoint()
    {
        return world.getPointAt(geometryIndex);
    }

    public Color getColor()
    {
        return type.getColor();
    }


    public enum TileType implements ColorHolder
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
