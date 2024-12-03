package com.origin.hexasphere.geometry.g3d;

import com.badlogic.gdx.math.Vector3;

public class Triangle
{
    private Hexasphere hexasphere;
    private int index1, index2, index3;

    public Triangle(Hexasphere hexasphere, int idx1, int idx2, int idx3)
    {
        this(hexasphere, idx1, idx2, idx3, false);
    }

    public Triangle(Hexasphere hexasphere, int idx1, int idx2, int idx3, boolean trackFaceInPoints)
    {
        this.hexasphere = hexasphere;
        this.index1 = idx1;
        this.index2 = idx2;
        this.index3 = idx3;
    }

    public void subdivide()
    {
        //The three initial points.
        Point p1 = hexasphere.getPointAt(index1);
        Point p2 = hexasphere.getPointAt(index2);
        Point p3 = hexasphere.getPointAt(index3);
        Point[] oldPoints = new Point[] {p1, p2, p3};
        projectToSphere(p1.getPosition(), hexasphere.getCenter());
        projectToSphere(p2.getPosition(), hexasphere.getCenter());
        projectToSphere(p3.getPosition(), hexasphere.getCenter());

        //The three new points.
        Point p4 = getMidPoint(hexasphere.getPointAt(index1), hexasphere.getPointAt(index2));
        Point p5 = getMidPoint(hexasphere.getPointAt(index1), hexasphere.getPointAt(index3));
        Point p6 = getMidPoint(hexasphere.getPointAt(index2), hexasphere.getPointAt(index3));
        projectToSphere(p4.getPosition(), hexasphere.getCenter());
        projectToSphere(p5.getPosition(), hexasphere.getCenter());
        projectToSphere(p6.getPosition(), hexasphere.getCenter());

        Point[] newPoints = new Point[] {p4, p5, p6};
        for(int i = 0; i < newPoints.length; i++)
            newPoints[i].setColor(oldPoints[i].getColor().cpy());

        hexasphere.addRawTriangle(p6, p3, p5);
        hexasphere.addRawTriangle(p5, p1, p4);
        hexasphere.addRawTriangle(p4, p6, p5);
        hexasphere.addRawTriangle(p6, p4, p2);

        hexasphere.removeTriangle(this);
    }

    private Point getMidPoint(Point p1, Point p2)
    {
        Vector3 p1Pos = p1.getPosition();
        Vector3 p2Pos = p2.getPosition();

        float x1 = p1Pos.x, y1 = p1Pos.y, z1 = p1Pos.z;
        float x2 = p2Pos.x, y2 = p2Pos.y, z2 = p2Pos.z;
        return new Point((x1 + x2)/2f, (y1 + y2)/2f, (z1 + z2) / 2f);
    }

    private void projectToSphere(Vector3 objVector, Vector3 centerVector)
    {
        //Magnitude = sqrt((x₁ - x₀)² + (y₁ - y₀)² + (z₁ - z₀)²)
        float xDistance = (float)(Math.pow((objVector.x-centerVector.x), 2d));
        float yDistance = (float)(Math.pow((objVector.y-centerVector.y), 2d));
        float zDistance = (float)(Math.pow((objVector.z-centerVector.z), 2d));
        float magnitude = (float)Math.sqrt((xDistance + yDistance + zDistance));
        objVector.x /= magnitude / hexasphere.getRadius();
        objVector.y /= magnitude / hexasphere.getRadius();
        objVector.z /= magnitude / hexasphere.getRadius();
    }

    public Point getPoint1()
    {
        return hexasphere.getPointAt(index1);
    }

    public Point getPoint2()
    {
        return hexasphere.getPointAt(index2);
    }

    public Point getPoint3()
    {
        return hexasphere.getPointAt(index3);
    }

    public short getIdx1()
    {
        return (short)index1;
    }

    public short getIdx2()
    {
        return (short)index2;
    }

    public short getIdx3()
    {
        return (short)index3;
    }

    @Override
    public String toString()
    {
        return "{" + getIdx1() + ", " + getIdx2() + ", " + getIdx3() + "}";
    }
}
