package com.origin.hexasphere.geometry.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class SphereRayCaster
{
    private Ray ray;

    public SphereRayCaster()
    {
        ray = new Ray();
    }

    private int curStep = 0;

    public Vector3 rayCastToSphere(Vector3 origPos, Vector3 sphereCenter, Vector3 dir, float radius)
    {
        Vector3 output = new Vector3();
        ray.set(origPos, dir);
        //ray.direction.set(dir);
        //ray.origin.set(origPos);
        boolean intersection = Intersector.intersectRaySphere(ray, sphereCenter, radius, output);
        Gdx.app.log("SphereCast Debug", output.toString() + ", Did it Intersect? " + intersection);
        return output;
    }
}
