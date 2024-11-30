/*package com.origin.hexasphere.geometry;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Point
{
    private final String _id;
    private final Vector3 _position;
    private Array<Face> _faces;

    private static final float PointComparisonAccuracy = 0.0001f;
    private static int maxID = 0;

    public Point(Vector3 position)
    {
        _id = "" + maxID++;
        _position = position;
        _faces = new Array<Face>();
    }

    private Point(Vector3 position, String id, Array<Face> faces)
    {
        _faces = new Array<Face>();

        _id = id;
        _position = position;
        this._faces = faces;
    }

    public Vector3 getPosition()
    {
        return this._position;
    }

    public String getID()
    {
        return _id;
    }

    public Array<Face> getFaces()
    {
        return _faces;
    }

    public void AssignFace(Face face)
    {
        _faces.add(face);
    }

    public Array<Point> Subdivide(Point target, int count, Func<Point, Point> findDuplicatePointIfExists)
    {
        Array<Point> segments = new Array<Point>();
        segments.add(this);

        for (int i = 1; i <= count; i++)
        {
            float x = _position.x * (1 - (float) i / count) + target.getPosition().x * ((float) i / count);
            float y = _position.y * (1 - (float) i / count) + target.getPosition().y * ((float) i / count);
            float z = _position.z * (1 - (float) i / count) + target.getPosition().z * ((float) i / count);

            Point newPoint = findDuplicatePointIfExists(new Point(new Vector3(x, y, z)));
            segments.add(newPoint);
        }

        segments.add(target);
        return segments;
    }

    public Point ProjectToSphere(float radius, float t)
    {
        float projectionPoint = radius / (float)(Math.pow(_position.x, 2) + Math.pow(_position.x, 2) + Math.pow(_position.z, 2));
        float x = _position.x * projectionPoint * t;
        float y = _position.y * projectionPoint * t;
        float z = _position.z * projectionPoint * t;
        return new Point(new Vector3(x, y, z), _id, _faces);
    }

    private Point findDuplicatePointIfExists(Array<Point> points, Point point)
    {
        Point existingPoint = points.FirstOrDefault(candidatePoint => Point.IsOverlapping(candidatePoint, point));
        for(Point candidatePoint : points)
        {
            if(Point.IsOverlapping(candidatePoint, point))
        }
        if (existingPoint != null)
        {
            return existingPoint;
        }
        points.add(point);
        return point;
    }

    public Array<Face> GetOrderedFaces()
    {
        if (_faces.size == 0) return _faces;
        Array<Face> orderedList = new Array<Face>();
        orderedList.add(_faces.get(0));

        Face currentFace = orderedList.get(0);
        while (orderedList.size < _faces.size)
        {
            Array<String> existingIds = this.selectAllFacesWithIDs(orderedList); //orderedList.Select(face => face.ID).ToList();
            Face neighbor = getNewNeighbor(existingIds, _faces); //First(face => !existingIds.Contains(face.ID) && face.IsAdjacentToFace(currentFace));
            currentFace = neighbor;
            orderedList.add(currentFace);
        }

        return orderedList;
    }

    private Face getNewNeighbor(Array<String>existingIds, Array<Face> faces)
    {
        for(Face face : faces)
        {
            if(!existingIds.contains(face.getID(), false) && face.IsAdjacentToFace(currentFace))
            {
                return face;
            }
        }
    }

    private Array<String> selectAllFaceIds(Array<Face> orderedList)
    {
        Array<String> retval = new Array<String>();
        for(Face face : orderedList)
        {
            retval.add(face.toString());
        }
        return retval;
    }

    public static boolean IsOverlapping(Point a, Point b)
    {
        return
            Math.abs(a.getPosition().x - b.getPosition().x) <= PointComparisonAccuracy &&
                Math.abs(a.getPosition().y - b.getPosition().y) <= PointComparisonAccuracy &&
                Math.abs(a.getPosition().z - b.getPosition().z) <= PointComparisonAccuracy;
    }

    @Override
    public String toString()
{
    return "{_position.x},{_position.y},{_position.z}";
}

    public String ToJson()
    {
        return "{{\"x\":{_position.x},\"y\":{_position.y},\"z\":{_position.z}, \"guid\":\"{_id}\"}}";
    }
}
*/
