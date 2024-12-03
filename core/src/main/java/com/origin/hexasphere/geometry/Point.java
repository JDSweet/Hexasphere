package com.origin.hexasphere.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;


public class Point
{
    private Vector3 position;
    private Color color;

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

    public Vector3 getPosition()
    {
        return position;
    }

    public Color getColor()
    {
        return this.color;
    }
}
