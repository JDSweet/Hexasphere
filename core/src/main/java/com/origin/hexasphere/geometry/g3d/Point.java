package com.origin.hexasphere.geometry.g3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;

import java.util.Objects;


public class Point
{
    private Vector3 position;
    private Color color;
    private int index;
    private boolean isOriginal;
    private IntArray adjacencies;

    public Point(float x, float y, float z, boolean isOriginal)
    {
        this(x, y, z, Color.RED, isOriginal);
    }

    public Point(float x, float y, float z, Color color, boolean isOriginal)
    {
        this(new Vector3(x, y, z), color, isOriginal);
    }

    public Point(Vector3 position, Color color, boolean isOriginal)
    {
        this.position = position;
        this.color = color;
        this.isOriginal = isOriginal;
        this.adjacencies = new IntArray();
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

    public boolean isOriginal()
    {
        return this.isOriginal;
    }

    @Override
    public boolean equals(Object other)
    {
        Point otherPnt = (Point)other;
        return other == this || this.position.epsilonEquals(otherPnt.getPosition(), 0.01f);
    }

    public void addAdjacency(int idx)
    {
        if(!adjacencies.contains(idx))
            adjacencies.add(idx);
    }

    public IntArray getAdjacencies()
    {
        return adjacencies;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position);
    }
}
