package com.eimacs.drawmaze01;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private boolean running;
    private SurfaceHolder surfaceHolder; // allows us to lock the screen
    private MazeSurfaceView gamePanel;

    public MainThread(SurfaceHolder surfaceHolder, MazeSurfaceView gamePanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning( boolean running )
    {
        this.running = running;
    }

    @Override
    public void run()
    {
        Canvas canvas;

        while ( running )
        {
            canvas = null;

            // try locking the canvas for exclusive pixel editing
            // in the surface
            try
            {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    // update game state
                    this.gamePanel.update(); // change state of game objects if necessary

                    // render state to the screen
                    // draws the canvas on the panel
                    if ( canvas != null )
                        this.gamePanel.draw( canvas ); // calls gamePanel's onDraw()

                    gamePanel.postInvalidate(); // docs say unnecessary, but it's actually necessary
                }
            }
            catch( Exception e )
            {
            }
            finally
            {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            } // end finally
        }
    }
}