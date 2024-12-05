package com.origin.hexasphere.tilemap;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Octree;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class IcoSphereTileCollider implements Octree.Collider<IcoSphereTile>
{
    //private IcoSphereTile tile;

    public IcoSphereTileCollider()
    {
        //this.tile = tile;
    }

    @Override
    public boolean intersects(BoundingBox nodeBounds, IcoSphereTile geometry)
    {
        return nodeBounds.contains(geometry.getPoint().getPosition());
        //return false;
    }

    @Override
    public boolean intersects(Frustum frustum, IcoSphereTile tile)
    {
        return frustum.sphereInFrustum(tile.getWorld().getCenter(), tile.getWorld().getRadius());
        //return false;
    }

    @Override
    public float intersects(Ray ray, IcoSphereTile geometry)
    {
        return ray.origin.dst(geometry.getPoint().getPosition());
        //return 0;
    }
}
