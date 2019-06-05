package com.eimacs.drawmaze01;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.eimacs.createmaze01.Cell;
import com.eimacs.createmaze01.Maze;

// Draws the current cell of a maze
public class MazePainter
{
    // class variables
    private static int count = 0; // number of painters that are in use
    public static int margin = 16; // for control points, also serves as radius of stones
    public static int wallWidth = 16; // don't exceed 2*margin
    private static float steps = 50f; // transition time in frames
    private static Paint wallPaint;
    private static Paint stonePaint;
    private static int[] mxs = new int[]{ 0, 0, 1, -1 };
    private static int[] mys = new int[]{ 1, -1, 0, 0 };

    // object variables
    private Maze maze;
    public int width;
    public int height;
    public int tp, bm, lt, rt; // four coords for "control points" to draw wall lines and stones
    public boolean[] wallFlags;
    public boolean[] publicFlags;
    public boolean[] transFlags;
    public boolean escaped = false;

    public int x, y; // (x,y) is the coord of the current cell in the maze

    // drawing and transition variables
    private boolean transition = false;
    private int mx, my; // 0, 1, or -1 each, depending on which way we're moving
    public float ttp, tbm, tlt, trt; // four coords for "transition control points" to draw wl/s
    private float step = 0f;
    private float scale;

    public MazePainter( int thewidth, int theheight )
    {
        width = thewidth;
        height = theheight;
        tp = margin;
        lt = margin;
        rt = width - margin;
        count++;
        setPaints();
        wallFlags = new boolean[ 4 ];
        publicFlags = new boolean[ 4 ];
        transFlags = new boolean[ 4 ];
    }

    private void setPaints()
    {
        if ( count == 1 )
        {
            wallPaint = new Paint();
            wallPaint.setColor( Color.WHITE );
            wallPaint.setTextSize( 100 );
            wallPaint.setStrokeWidth( 2 );
            wallPaint.setStrokeCap( Paint.Cap.SQUARE );
            stonePaint = new Paint();
            stonePaint.setColor( Color.BLUE );
            stonePaint.setTextSize( 100 );
            stonePaint.setStrokeWidth( 2 );
        }
    }

    public void setMaze( Maze themaze )
    {
        maze = themaze;
        x = maze.random( maze.getNCellsX() );
        y = maze.random( maze.getNCellsY() );
        setWallFlags();
        setPublicFlags( x, y );
    }

    public void adjustDims( int buttonHeight )
    {
        bm = height - buttonHeight - (4 * margin);
    }

    private void setWallFlags()
    {
        wallFlags[ 0 ] = maze.getCell( x, y ).hasWall( Cell.Direction.NORTH );
        wallFlags[ 1 ] = maze.getCell( x, y ).hasWall( Cell.Direction.SOUTH );
        wallFlags[ 2 ] = maze.getCell( x, y ).hasWall( Cell.Direction.WEST );
        wallFlags[ 3 ] = maze.getCell( x, y ).hasWall( Cell.Direction.EAST );
    }

    public void setPublicFlags( int inx, int iny )
    {
        publicFlags[ 0 ] = maze.getCell( inx, iny ).hasWall( Cell.Direction.NORTH );
        publicFlags[ 1 ] = maze.getCell( inx, iny ).hasWall( Cell.Direction.SOUTH );
        publicFlags[ 2 ] = maze.getCell( inx, iny ).hasWall( Cell.Direction.WEST );
        publicFlags[ 3 ] = maze.getCell( inx, iny ).hasWall( Cell.Direction.EAST );
    }

    // done once, don't have to worry about moving through a wall
    // since the corresponding arrow button will never be drawn
    public void go( int dir )
    {
        if ( !transition ) // don't allow preemptive taps before we're ready
        {
            mx = mxs[ dir ]; my = mys[ dir ];
            if ( ( x == 0 && mx == 1 ) || ( x == (maze.getNCellsX() - 1) && mx == -1 ) )
            {
                escaped = true;
                for ( int i = 0; i < 4; i++ )
                    publicFlags[ i ] = false;
            } else {
                step = 0f;
                transition = true;
                setPublicFlags(x - mx, y - my);
                // If moving N/S, we only need the E/W transition walls.
                // The N/S walls (old and new) should be hidden until we settle.
                if (my != 0) {
                    transFlags[0] = false;
                    transFlags[1] = false;
                    transFlags[2] = publicFlags[2];
                    transFlags[3] = publicFlags[3];
                }
                // If moving E/W, we only need the N/S transition walls.
                // The E/W walls (old and new) should be hidden until we settle.
                if (mx != 0) {
                    transFlags[0] = publicFlags[0];
                    transFlags[1] = publicFlags[1];
                    transFlags[2] = false;
                    transFlags[3] = false;
                }
            }
        }
    }

    public void drawWalls( Canvas canvas, float gtp, float gbm, float glt, float grt, boolean[] flags ) // using any four control coords
    {
        if ( flags[ 0 ] )
            canvas.drawRect( glt , gtp - wallWidth/2, grt , gtp + wallWidth/2, wallPaint );
        if ( flags[ 1 ] )
            canvas.drawRect( glt , gbm - wallWidth/2, grt , gbm + wallWidth/2, wallPaint );
        if ( flags[ 2 ] )
            canvas.drawRect( glt - wallWidth/2, gtp , glt + wallWidth/2, gbm, wallPaint );
        if ( flags[ 3 ] )
            canvas.drawRect( grt - wallWidth/2, gtp , grt + wallWidth/2, gbm, wallPaint );
    }

    public void drawStones( Canvas canvas, float gtp, float gbm, float glt, float grt )
    {
        canvas.drawCircle( glt, gtp, margin, stonePaint );
        canvas.drawCircle( grt, gtp, margin, stonePaint );
        canvas.drawCircle( glt, gbm, margin, stonePaint );
        canvas.drawCircle( grt, gbm, margin, stonePaint );
    }

    public void draw( Canvas canvas )
    {
        if ( !transition )
        { // sitting still
            // now draw walls
            drawWalls( canvas, tp, bm, lt, rt, wallFlags );
            // now draw the four stones
            drawStones( canvas, tp, bm, lt, rt );
        }
        else // in transition
        {
            if ( step < steps )
            {
                // establish transition control points
                // (now there will be the four control points and two (new) transition points)
                scale = step/steps;
                ttp = tp + ( ( bm - tp ) * scale * my );
                tbm = bm + ( ( bm - tp ) * scale * my );
                tlt = lt + ( ( rt - lt ) * scale * mx );
                trt = rt + ( ( rt - lt ) * scale * mx );
                // draw old walls
                drawWalls( canvas, ttp, tbm, tlt, trt, wallFlags );
                // draw new walls
                drawWalls( canvas, ( my < 0 ? tbm : tp ), ( my > 0 ? ttp : bm ), ( mx < 0 ? trt : lt ), ( mx > 0 ? tlt : rt ) , transFlags );
                // draw stones
                drawStones( canvas, ttp, tbm, tlt, trt );
                step++;
            }
            else
            { // done once
                transition = false;
                x -= mx;
                y -= my;
                setWallFlags();
            }
        }
    }
}
