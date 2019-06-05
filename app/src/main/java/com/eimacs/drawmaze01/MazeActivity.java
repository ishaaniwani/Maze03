package com.eimacs.drawmaze01;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

// import com.eimacs.createmaze01.DividerMaze;
import com.eimacs.createmaze01.PrimMaze;

public class MazeActivity extends Activity implements OnTouchListener
{
    private MazeSurfaceView mazeView;
    private TextView escapeText;
    public ImageButton[] buttons;

    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView( R.layout.activity_maze );
        escapeText = (TextView) findViewById(R.id.escapeText);
        escapeText.setVisibility(View.INVISIBLE);
        setUpArrowButtons();

        mazeView = (com.eimacs.drawmaze01.MazeSurfaceView) findViewById(R.id.mazeSurfaceView);
// mazeView.setMaze( new DividerMaze( 10, 10 ) );
        mazeView.setMaze( new PrimMaze( 10, 10 ) );

        updateGameButtons();
    }

    private void setUpArrowButtons()
    {
        buttons = new ImageButton[]{
                (ImageButton)findViewById( R.id.buttonup ),
                (ImageButton)findViewById( R.id.buttondown ),
                (ImageButton)findViewById( R.id.buttonleft ),
                (ImageButton)findViewById( R.id.buttonright )
        };
        for ( int i = 0; i < 4; i++ )
        {
            buttons[ i ].setId( i );
            buttons[ i ].setOnTouchListener( this );
        }
    }

    // for OnTouchListener implementation
    @Override
    public boolean onTouch( View button, MotionEvent arg1 )
    {
        if( arg1.getAction() == MotionEvent.ACTION_DOWN )
        {
            mazeView.painter.go( button.getId() );
            updateGameButtons();
        }
        return false;
    }

    private void updateGameButtons()
    {
        for ( int i = 0; i < 4; i++ )
        {
            if ( !mazeView.painter.escaped && !mazeView.painter.publicFlags[ i ] )
                buttons[ i ].setVisibility( View.VISIBLE );
            else
                buttons[ i ].setVisibility( View.INVISIBLE );
        }

        if ( mazeView.painter.escaped )
            escapeText.setVisibility( View.VISIBLE );
    }

    // New Maze button code (pointed to by XML)
    public void newMaze(View view)
    {
// mazeView.setMaze( new DividerMaze( 10, 10 ) );
        mazeView.setMaze( new PrimMaze( 10, 10 ) );
        mazeView.painter.escaped = false;
        escapeText.setVisibility( View.INVISIBLE );
        updateGameButtons();
    }

    // Close button code (pointed to by XML)
    public void close(View view)
    {
        mazeView.controlThread( false );
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        // Determine the height of the bottom buttons and pass the data on
        // so the painter doesn't draw on top of them
        LinearLayout buttonBar = (LinearLayout) findViewById(R.id.buttonBar);
        mazeView.adjustDims( (int) buttonBar.getHeight() );
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mazeView.controlThread( true );
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mazeView.controlThread( false );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
