package com.origin.hexasphere.geometry;

import com.badlogic.gdx.math.Vector3;

public class Point
{
    private Vector3 position;

    public Point(Vector3 position)
    {
        this.position = position;
    }

    public Vector3 getPosition()
    {
        return position;
    }
}
