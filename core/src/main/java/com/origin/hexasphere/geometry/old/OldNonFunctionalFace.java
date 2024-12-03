/*package com.origin.hexasphere.geometry;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Triangle
{
    private String _id;
    private Array<Point> _points;
    private static int maxFaceID = 0;

    //trackFaceInPoints default = true
    public Triangle(Point point1, Point point2, Point point3, boolean trackFaceInPoints)
    {
        _id = "" + maxFaceID++;

        float centerX = (point1.getPosition().x + point2.getPosition().x + point3.getPosition().x) / 3;
        float centerY = (point1.getPosition().y + point2.getPosition().y + point3.getPosition().y) / 3;
        float centerZ = (point1.getPosition().z + point2.getPosition().z + point3.getPosition().z) / 3;
        Vector3 center = new Vector3(centerX, centerY, centerZ);

        //Determine correct winding order
        Vector3 normal = GetNormal(point1, point2, point3);
        _points = new Array<Point>();
        if(IsNormalPointingAwayFromOrigin(center, normal))
        {
            _points.add(point1);
            _points.add(point2);
            _points.add(point3);
        }
        else
        {
            _points.add(point1);
            _points.add(point3);
            _points.add(point2);
        }
        //_points = IsNormalPointingAwayFromOrigin(center, normal) ?
        //    new Array<Point> {point1, point2, point3} :
        //new Array<Point> {point1, point3, point2};

        if (trackFaceInPoints)
        {
            //_points.ForEach(point => point.AssignFace(this));
            for(Point p : _points)
            {
                p.AssignFace(this);
            }
        }
    }

    public String getID()
    {
        return _id;
    }

    public Array<Point> getPoints()
    {
        return _points;
    };

    public Array<Point> GetOtherPoints(Point point)
    {
        if (!IsPointPartOfFace(point))
        {
            //throw new Exception("Given point must be one of the points on the face!");
        }

        return _points.Where(facePoint => facePoint.ID != point.ID).ToList();
    }

    public boolean IsAdjacentToFace(Triangle face)
    {
        Array<String> thisFaceIds = _points.Select(point => point.ID).ToList();
        Array<String> otherFaceIds = face.Points.Select(point => point.ID).ToList();
        return thisFaceIds.Intersect(otherFaceIds).ToList().Count == 2;
    }

    public Point GetCenter()
    {
        float centerX = (_points.get(0).getPosition().x + _points.get(1).getPosition().x + _points.get(2).getPosition().x) / 3;
        float centerY = (_points.get(0).getPosition().y + _points.get(1).getPosition().y + _points.get(2).getPosition().y) / 3;
        float centerZ = (_points.get(0).getPosition().z + _points.get(1).getPosition().z + _points.get(2).getPosition().z) / 3;

        return new Point(new Vector3(centerX, centerY, centerZ));
    }

    private boolean IsPointPartOfFace(Point point)
    {
        return _points.Any(facePoint => facePoint.getID() == point.getID());
    }

    private static Vector3 GetNormal(Point point1, Point point2, Point point3)
    {
        Vector3 side1 = point2.getPosition().cpy().sub(point1.getPosition());
        Vector3 side2 = point3.getPosition().cpy().sub(point1.getPosition());

        Vector3 cross = side1.cpy().crs(side2);


        return cross / cross.magnitude;
    }

    private static float getMagnitude(Vector3 vec)
    {
        return 0f;
    }

    private static boolean IsNormalPointingAwayFromOrigin(Vector3 surface, Vector3 normal)
    {
        //Does adding the normal vector to the center point of the face get you closer or further from the center of the polyhedron?
        return Vector3.Distance(Vector3.zero, surface) < Vector3.Distance(Vector3.zero, surface + normal);
    }
}
*/
