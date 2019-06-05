package com.eimacs.createmaze01;

public class Maze
{
    protected int nCellsX, nCellsY;
    protected Cell[][] cells;

    public Maze(int nx, int ny)
    {
        nCellsX = nx;
        nCellsY = ny;

        cells = new Cell[ nCellsX ][ nCellsY ];

        for ( int x = 0; x < nCellsX; x++ )
        {
            for ( int y = 0; y < nCellsY; y++ )
            {
                cells[ x ][ y ] = new Cell( x, y );
            }
        }
    }

    public Cell getCell( int x, int y )
    {
        return cells[ x ][ y ];
    }

    public int getNCellsX()
    {
        return nCellsX;
    }

    public int getNCellsY()
    {
        return nCellsY;
    }

    public int random( int n )
    {
        return (int)(Math.random() * n);
    }
}