package com.origin.hexasphere.geometry.g3d;

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

    public Vector3 rayCastToSphere(Vector3 origPos, Vector3 center, float step, int maxSteps, float radius)
    {
          Vector3 output = new Vector3();
          Intersector.intersectRaySphere(ray, center, radius, output);
          return output;
//        curStep = 0;
//        Vector3 pos = new Vector3(origPos);
//        while(pos.dst(dest) > radius && curStep < maxSteps)
//        {
//            //pos.set(pos.x + step, pos.y + step, pos.z + step);
//            if(pos.x < dest.x)
//                pos.x += step;
//            if(pos.y < dest.y)
//                pos.y += step;
//            if(pos.z < dest.z)
//                pos.z += step;
//            if(pos.x > dest.x)
//                pos.x -= step;
//            if(pos.y > dest.y)
//                pos.y -= step;
//            if(pos.z > dest.z)
//                pos.z -= step;
//            curStep++;
//        }
//        //curStep = 0;
//        return pos;
    }
}
