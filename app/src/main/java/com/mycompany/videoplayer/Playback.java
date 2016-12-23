package com.mycompany.videoplayer;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.res.*;
import android.widget.RadioGroup.*;
import android.content.*;
import android.graphics.*;
import android.transition.*;
import android.view.animation.*;
import java.util.*;
import android.media.*;
import android.animation.*;
import android.widget.SeekBar.*;
import android.content.pm.*;
import android.content.pm.ActivityInfo;

public class Playback extends Service implements OnClickListener,OnTouchListener, OnSeekBarChangeListener
{

	private WindowManager wm ;
	private FrameLayout ll ;
	private LayoutInflater inflater ;
	private VideoView v ;
	private Handler updatehandler ;
	private ImageView play,prev,next ;
	private Button resize, fullscreen ;
	private SeekBar sb ;
	private View view ;
	private String[] videopaths ;

	private int initx,inity,sw,pos ;
	private float touchx,touchy ;

	final WindowManager.LayoutParams lparams = new WindowManager.LayoutParams( 320, 180, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
	
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method

		return null;
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO: Implement this method
		super.onStart(intent, startId);
		try{
			videopaths = intent.getStringArrayExtra("videopaths");
			pos = intent.getIntExtra("pos",0);
			v.setVideoPath(videopaths[pos]);
			v.requestFocus();
			v.start();
			
			Runnable seekbarupdater = new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					sb.setProgress(v.getCurrentPosition());
					//updatehandler.postDelayed(this, 1000);
				}
			};
			updatehandler = new Handler();
			updatehandler.postDelayed(seekbarupdater, 1000);
			
			v.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
					@Override
					public void onPrepared(MediaPlayer p1)
					{
						// TODO: Implement this method
						Toast.makeText(getApplicationContext(), ""+v.getDuration(), Toast.LENGTH_LONG).show();
						sb.setMax(v.getDuration());
					}		
			});
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
	}


	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
	
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		sw = wm.getDefaultDisplay().getWidth() ;
		
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ll = (FrameLayout) inflater.inflate(R.layout.playback,null);
		play = (ImageView)ll.findViewById(R.id.ivplay);
		prev = (ImageView)ll.findViewById(R.id.ivprev);
		next = (ImageView)ll.findViewById(R.id.ivnext);
		resize = (Button)ll.findViewById(R.id.bresize);
		fullscreen = (Button)ll.findViewById(R.id.bfullscreen);
		sb = (SeekBar)ll.findViewById(R.id.sbvideo);
	
		view = ll.findViewById(R.id.fl);
		
		v = (VideoView)ll.findViewById(R.id.playback);
		
		// sb.setProgress(0);
		sb.setOnSeekBarChangeListener(this);
		
		lparams.gravity = Gravity.TOP | Gravity.LEFT;
		lparams.x = 0;
		lparams.y = 100 ;
		wm.addView(ll, lparams);
		
		play.setOnClickListener(this);
		prev.setOnClickListener(this);
		next.setOnClickListener(this);
		resize.setOnTouchListener(this);
		fullscreen.setOnClickListener(this);
		
		ll.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					view.setAlpha(0f);
					view.setVisibility(View.VISIBLE);
					view.animate().alpha(1f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter(){
						public void onAnimationEnd(Animator a){
						}
					});
					
					final Handler h =  new Handler();
					final Timer t = new Timer();
					t.schedule(new TimerTask(){
							@Override
							public void run()
							{
								// TODO: Implement this method
								h.post(new Runnable(){
										@Override
										public void run()
										{
											// TODO: Implement this method
											view.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new AnimatorListenerAdapter(){
												public void onAnimationEnd(Animator animator){
													view.setVisibility(View.GONE);
												}
											});
										}
								});
							}
					},3000);
				}
		});

		ll.setOnTouchListener(new View.OnTouchListener(){
				@Override
				public boolean onTouch(View p1, MotionEvent e)
				{
					// TODO: Implement this method
					switch (e.getAction())
					{
						case MotionEvent.ACTION_DOWN :
							initx = lparams.x;
							inity = lparams.y;
							touchx = e.getRawX();
							touchy = e.getRawY();
							return false;

						case MotionEvent.ACTION_UP:
							if (((int)(e.getRawY()) >= (wm.getDefaultDisplay().getHeight() - 100)))
							{	
								v.stopPlayback();
								ll.removeAllViews();
								stopSelf() ;
							}
							return false ;

						case MotionEvent.ACTION_MOVE:
							lparams.x = initx + (int)(e.getRawX() - touchx);
							lparams.y = inity + (int)(e.getRawY() - touchy);
							wm.updateViewLayout(ll, lparams);

							return true;

					}
					return false;
				}
			});

	}
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		
		switch(p1.getId()){
			case R.id.ivplay :
					if(v.isPlaying())
					{
						v.pause();
						Toast.makeText(Playback.this,"Video paused", Toast.LENGTH_SHORT).show();
					}
					else
					{
						v.start();
						Toast.makeText(Playback.this,"Video resumed",Toast.LENGTH_SHORT).show();
					}
				break;
				
			case R.id.ivprev:
					if(pos>0)
					{
						pos-- ;
						v.setVideoPath(videopaths[pos]);
						v.start();
					}
				break;
			
			case R.id.ivnext:
					pos++ ;
					v.setVideoPath(videopaths[pos]);
					v.start();
				break;
				
			case R.id.bfullscreen: 
					int currentduration= v.getCurrentPosition();
					v.stopPlayback();
					ll.removeAllViews();
					stopSelf();
					Intent i = new Intent(getApplicationContext(), PlaybackFullScreen.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("currentduration",currentduration);
					i.putExtra("position",pos);
					i.putExtra("videolist",videopaths);
					startActivity(i);
				break;
		}
	}

	@Override
	public boolean onTouch(View p1, MotionEvent e)
	{
		// TODO: Implement this method
		switch(e.getAction()){
			case MotionEvent.ACTION_DOWN :
				initx = lparams.width ;
				inity = lparams.height ;
				touchx = e.getRawX() ;
				touchy = e.getRawY() ;
				break ;

			case MotionEvent.ACTION_MOVE :
				lparams.width = initx + (int)(e.getRawX() - touchx);
				lparams.height = (int)9*(initx + (int)(e.getRawX() - touchx))/16;

				v.layout(sw,sw,sw,sw);
				if(lparams.width <= sw && lparams.width > 0 && lparams.height > 0)
				{
					wm.updateViewLayout(ll,lparams);
				}
				break ;

			case MotionEvent.ACTION_UP :
				if(lparams.width >= sw-20)
				{
					Toast.makeText(Playback.this,"Auto Adjust", Toast.LENGTH_SHORT).show();
					lparams.width = sw ;
					v.layout(sw,sw,sw,sw);
					wm.updateViewLayout(ll,lparams);
				}
				break ;
		}
		return false;
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
	

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		v.stopPlayback();
		wm.removeView(ll);

	}




}

