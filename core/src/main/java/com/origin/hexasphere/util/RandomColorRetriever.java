package com.origin.hexasphere.util;

import com.badlogic.gdx.graphics.Color;

public class RandomColorRetriever
{
    private static Color[] colors;

    public static Color randomColor()
    {
        float r = (float)Math.random();
        float g = (float)Math.random();
        float b = (float)Math.random();
        float a = 1f;
        return new Color(r, g, b, a);
    }
}
