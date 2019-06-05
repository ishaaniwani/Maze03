package com.eimacs.createmaze01;

public class DividerMaze extends Maze
{
    public DividerMaze(int nx, int ny)
    {
        super( nx, ny );

        divide( 0, 0, nx - 1, ny - 1 );
        normalize();
    }

    private void divide( int x0, int y0, int x1, int y1 )
    {
        int blockWidth = x1 - x0,
                blockHeight = y1 - y0;

        if ( blockWidth > 0 && blockHeight > 0 )
        {
            int xA = x0 + (int) (Math.random() * blockWidth - 1),
                    yA = y0 + (int) (Math.random() * blockHeight - 1);

            for ( int x = x0; x <= x1; x++ )
            {
                cells[ x ][ yA ].setWall( Cell.Direction.SOUTH, true );
            }

            for ( int y = y0; y <= y1; y++ )
            {
                cells[ xA ][ y ].setWall( Cell.Direction.EAST, true );
            }

            int randomNorth = y0 + random( yA - y0 ),
                    randomEast = (xA + 1) + random( x1 - (xA + 1) ),
                    randomSouth = (yA + 1) + random( y1 - (yA + 1) ),
                    randomWest = x0 + random( xA - x0 );

            int randomKeep = random(4);

            if (randomKeep != 0) { cells[ xA ][ randomNorth ].setWall( Cell.Direction.EAST, false ); }
            if (randomKeep != 1) { cells[ randomEast ][ yA ].setWall( Cell.Direction.SOUTH, false ); }
            if (randomKeep != 2) { cells[ xA ][ randomSouth ].setWall( Cell.Direction.EAST, false ); }
            if (randomKeep != 3) { cells[ randomWest ][ yA ].setWall( Cell.Direction.SOUTH, false ); }

            divide( x0, y0, xA, yA );
            divide( xA + 1, y0, x1, yA );
            divide( x0, yA + 1, xA, y1 );
            divide( xA + 1, yA + 1, x1, y1 );
        }
    }

    private void normalize()
    {
        for ( int x = 0; x < nCellsX; x++ )
        {
            for ( int y = 0; y < nCellsY; y++ )
            {
                Cell c = cells[ x ][ y ];

                if ( x == 0 )
                {
                    c.setWall( Cell.Direction.WEST, true );
                }
                else
                {
                    if ( x == nCellsX - 1 )
                    {
                        c.setWall( Cell.Direction.EAST, true );
                    }
                }

                if ( y == 0 )
                {
                    c.setWall( Cell.Direction.NORTH, true );
                }
                else
                {
                    if ( y == nCellsY - 1 )
                    {
                        c.setWall( Cell.Direction.SOUTH, true );
                    }
                }

                if ( x > 0 && cells[ x - 1 ][ y ].hasWall( Cell.Direction.EAST ) )
                {
                    c.setWall( Cell.Direction.WEST, true );
                }

                if ( y > 0 && cells[ x ][ y - 1 ].hasWall( Cell.Direction.SOUTH ) )
                {
                    c.setWall( Cell.Direction.NORTH, true );
                }
            }
        }

        cells[ 0 ][ 0 ].setWall( Cell.Direction.WEST, false ); // Start
        cells[ nCellsX - 1][ nCellsY - 1].setWall( Cell.Direction.EAST, false ); // Finish
    }
}