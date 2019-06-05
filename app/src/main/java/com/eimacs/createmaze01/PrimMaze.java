package com.eimacs.createmaze01;

import java.util.ArrayList;

public class PrimMaze extends Maze
{
    private ArrayList<Cell> inTheMaze = new ArrayList<Cell>();
    private ArrayList<Wall> internalWalls = new ArrayList<Wall>();

    public PrimMaze(int nx, int ny)
    {
        super(nx, ny);

        for (int x = 0; x < nCellsX; x++)
        {
            for (int y = 0; y < nCellsY; y++)
            {
                for (Cell.Direction dir : Cell.Direction.values())
                {
                    cells[ x ][ y ].setWall(dir, true);
                }
            }
        }

        int startX = random(nCellsX),
                startY = random(nCellsY);

        Cell startCell = cells[ startX][ startY];

        inTheMaze.add(startCell);

        for (Cell.Direction dir : Cell.Direction.values())
        {
            if (startCell.hasWall(dir))
            {
                Wall wall = new Wall(startCell.getX(), startCell.getY(), dir);
                if (wall.isInternal())
                {
                    internalWalls.add(wall);
                }
            }
        }

        while (internalWalls.size() > 0)
        {
            Wall theWall = internalWalls.remove(random(internalWalls.size()));
            Wall otherSide = theWall.otherSide();
            Cell neighbor = cells[ otherSide.x ][ otherSide.y ];

            if (!inTheMaze.contains(neighbor))
            {
                inTheMaze.add(neighbor);

                for (Cell.Direction dir : Cell.Direction.values())
                {
                    if (neighbor.hasWall(dir) && dir != otherSide.d)
                    {
                        Wall wall = new Wall(otherSide.x, otherSide.y, dir);
                        if (wall.isInternal())
                        {
                            internalWalls.add(wall);
                        }
                    }
                }

                cells[ theWall.x ][ theWall.y ].setWall(theWall.d, false);
                neighbor.setWall(otherSide.d, false);
            }
        }

        cells[ 0][ 0].setWall(Cell.Direction.WEST, false); // Start
        cells[ nCellsX - 1][ nCellsY - 1].setWall(Cell.Direction.EAST, false); // Finish
    }

    private class Wall
    {
        int x, y;
        Cell.Direction d;

        public Wall(int x, int y, Cell.Direction d)
        {
            this.x = x;
            this.y = y;
            this.d = d;
        }

        public boolean isInternal()
        {
            switch (d)
            {
                case NORTH:
                    return y != 0;
                case SOUTH:
                    return y != nCellsY - 1;
                case WEST:
                    return x != 0;
                case EAST:
                    return x != nCellsX - 1;
                default: // included to please the IDE; in fact, it's never reached
                    return false;
            }
        }

        public Wall otherSide()
        {
            switch (d)
            {
                case NORTH:
                    return new Wall(x, y - 1, Cell.Direction.SOUTH);
                case SOUTH:
                    return new Wall(x, y + 1, Cell.Direction.NORTH);
                case WEST:
                    return new Wall(x - 1, y, Cell.Direction.EAST);
                case EAST:
                    return new Wall(x + 1, y, Cell.Direction.WEST);
                default: // included to please the IDE; in fact, it's never reached
                    return new Wall(-1, -1, Cell.Direction.NORTH);
            }
        }
    }
}
