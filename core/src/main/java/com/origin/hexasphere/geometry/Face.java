package com.origin.hexasphere.geometry;

import com.origin.hexasphere.HexaGame;

public class Face
{
    private HexaGame hexasphere;
    private short index1, index2, index3;

    public Face(Hexasphere hexasphere, short idx1, short idx2, short idx3, boolean trackFaceInPoints)
    {
        this.index1 = idx1;
        this.index2 = idx2;
        this.index3 = idx3;
    }

    public short getIdx1()
    {
        return index1;
    }

    public short getIdx2()
    {
        return index2;
    }

    public short getIdx3()
    {
        return index3;
    }
}
