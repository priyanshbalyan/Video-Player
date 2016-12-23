package com.mycompany.videoplayer;

import android.app.*;
import android.content.pm.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.SeekBar.*;

import android.view.View.OnClickListener;

public class PlaybackFullScreen extends Activity implements OnClickListener, OnSeekBarChangeListener
{

	VideoView v ;
	ImageView next,prev,play ;
	SeekBar sb ;
	String[] videopaths ;
	
	int currentduration, pos ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
		setContentView(R.layout.playback);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		v = (VideoView)findViewById(R.id.playback);
		next = (ImageView)findViewById(R.id.ivnext);
		prev = (ImageView)findViewById(R.id.ivprev);
		play = (ImageView)findViewById(R.id.ivplay);
		
		sb = (SeekBar)findViewById(R.id.sbvideo);
		sb.setOnSeekBarChangeListener(this);
		
		videopaths = getIntent().getStringArrayExtra("videolist");
		pos = getIntent().getIntExtra("position", 0);
		currentduration = getIntent().getIntExtra("currentduration", 0);
		
		v.setVideoPath(videopaths[pos]);
		v.seekTo(currentduration);
		v.start();
		
		sb.setProgress(currentduration);
		
		v.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

				@Override
				public void onPrepared(MediaPlayer p1)
				{
					// TODO: Implement this method
					sb.setMax(v.getDuration());
				}
		});
	
	}
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.ivnext:
					pos++ ;
					v.setVideoPath(videopaths[pos]);
					v.start();
				break;
				
			case R.id.ivprev:
					if(pos>0){
						pos-- ;
						v.setVideoPath(videopaths[pos]);
						v.start();
					}else
						v.seekTo(0);
				break;
				
			case R.id.ivplay:
					if(v.isPlaying()){
						v.pause();
					}else{
						v.start();
					}
					
				break;
	
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar p1, int p2, boolean p3)
	{
		// TODO: Implement this method
		v.seekTo(p2);
	}

	@Override
	public void onStartTrackingTouch(SeekBar p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onStopTrackingTouch(SeekBar p1)
	{
		// TODO: Implement this method
	}

}
