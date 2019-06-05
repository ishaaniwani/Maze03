package com.eimacs.drawmaze01;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.eimacs.createmaze01.Maze;

public class MazeSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private Maze maze;
    public MazePainter painter;

    public MazeSurfaceView(Context context)
    {
        super(context);
        init();
    }

    public MazeSurfaceView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public MazeSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        getHolder().addCallback(this);
        setFocusable(true); // make the SurfaceView focusable so it can handle events

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        painter = new MazePainter( metrics.widthPixels, metrics.heightPixels );
    }

    public void adjustDims( int buttonHeight ) { painter.adjustDims( buttonHeight ); }

    public void setMaze(Maze themaze)
    {
        maze = themaze;
        painter.setMaze( maze );
    }

    public void controlThread( boolean onOff )
    {
        if ( onOff )
        {
            if ( thread == null )
                thread = new MainThread(getHolder(), this);
            thread.setRunning( true );
        }
        else
        {
            thread.setRunning( false );
        }
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        // restrict the viewed drawing area
        canvas.clipRect( painter.lt - MazePainter.margin,
                painter.tp - MazePainter.margin,
                painter.rt + MazePainter.margin,
                painter.bm + MazePainter.margin );
        // finally, let the painter do the work
        painter.draw( canvas );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        setWillNotDraw(false);
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // blocks, waits for thread to die
        boolean retry = true;
        while (retry)
        {
            try
            {
                thread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                // try again shutting down the thread
            }
        }
    }

    public void update()
    {
    }
}