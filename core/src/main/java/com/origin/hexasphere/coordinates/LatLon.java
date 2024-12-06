package com.origin.hexasphere.coordinates;

import com.badlogic.gdx.math.Vector2;

public class LatLon extends Vector2
{
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Vector2)
        {
            return epsilonEquals((Vector2)other, 0.01f);
        }
        else
            return super.equals(other);
    }
}
