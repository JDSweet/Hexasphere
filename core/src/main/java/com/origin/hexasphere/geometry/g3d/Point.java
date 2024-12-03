package com.origin.hexasphere.geometry.g3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;


public class Point
{
    private Vector3 position;
    private Color color;
    private int index;

    public Point(float x, float y, float z)
    {
        this(x, y, z, Color.RED);
    }

    public Point(float x, float y, float z, Color color)
    {
        this(new Vector3(x, y, z), color);
    }

    public Point(Vector3 position, Color color)
    {
        this.position = position;
        this.color = color;
    }

    public void setIndex(int idx)
    {
        this.index = idx;
    }

    public int getIndex()
    {
        return this.index;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public Color getColor()
    {
        return this.color;
    }

    public void setColor(Color c)
    {
        this.color = c;
    }

    @Override
    public boolean equals(Object other)
    {
        Point otherPnt = (Point)other;
        return getPosition().epsilonEquals(otherPnt.getPosition(), 0.00000f);
    }
}
