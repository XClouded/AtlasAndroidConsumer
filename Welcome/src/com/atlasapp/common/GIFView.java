package com.atlasapp.common;

import java.io.IOException;

import com.atlasapp.section_appentry.R;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GIFView  extends View{        
    private Movie movie;    
    private long moviestart;  
    public GIFView(Context context) throws IOException {  
        super(context);
        movie=Movie.decodeStream(getResources().openRawResource(R.drawable.loading_gif_animation));
    }
    public GIFView(Context context, AttributeSet attrs) throws IOException{
        super(context, attrs);
    movie=Movie.decodeStream(getResources().openRawResource(R.drawable.loading_gif_animation));
    }
    public GIFView(Context context, AttributeSet attrs, int defStyle) throws IOException {
        super(context, attrs, defStyle);
        movie=Movie.decodeStream(getResources().openRawResource(R.drawable.loading_gif_animation));
    }
@Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);
    long now=android.os.SystemClock.uptimeMillis();
    Paint p = new Paint();
    p.setAntiAlias(true);
    if (moviestart == 0) 
            moviestart = now;
            int relTime;
            relTime = (int)((now - moviestart) % movie.duration());
            movie.setTime(relTime);
            movie.draw(canvas,200,200);
            this.invalidate();
       }                         
}   