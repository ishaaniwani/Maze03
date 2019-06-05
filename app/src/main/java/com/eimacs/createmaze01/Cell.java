package com.eimacs.createmaze01;

import java.util.HashMap;

public class Cell
{
    public enum Direction
    {
        NORTH, SOUTH, EAST, WEST
    };

    private int x, y;
    private HashMap<Direction, Boolean> walls = new HashMap<Direction, Boolean>();

    public Cell(int x0, int y0)
    {
        x = x0;
        y = y0;
        for ( Direction dir : Direction.values() )
            walls.put( dir, false );
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public boolean hasWall( Direction dir )
    {
        return walls.get( dir );
    }

    public void setWall( Direction dir, boolean iswall )
    {
        walls.put( dir, iswall );
    }
}