package com.mycompany.videoplayer;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.provider.*;
import android.transition.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class MainActivity extends Activity 
{
	//private Transition.TransitionListener entertransitionlistener ;
	
	ListView lv ;
	Cursor c ;

	int count ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		/*entertransitionlistener = new Transition.TransitionListener(){

			@Override
			public void onTransitionStart(Transition p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onTransitionEnd(Transition p1)
			{
				// TODO: Implement this method
				final View myview = findViewById(R.id.listlayout);
				
				Animator crtransition = ViewAnimationUtils.createCircularReveal(myview, myview.getMeasuredWidth()/2, myview.getMeasuredHeight()/2, 0, Math.max(myview.getWidth(),myview.getHeight())/2);
				myview.setVisibility(View.VISIBLE);
				crtransition.addListener(new Animator.AnimatorListener(){

					@Override
					public void onAnimationStart(Animator p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationCancel(Animator p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationRepeat(Animator p1)
					{
						// TODO: Implement this method
					}
					
					@Override
					public void onAnimationEnd(Animator animator){
						getWindow().getEnterTransition().removeListener(entertransitionlistener);
					}
				});
				crtransition.start();
			}

			@Override
			public void onTransitionCancel(Transition p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onTransitionPause(Transition p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onTransitionResume(Transition p1)
			{
				// TODO: Implement this method
			}

			
		};*/
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9000E0")));
        setContentView(R.layout.main);
		lv = (ListView)findViewById(R.id.lvvideo);

		String[] p = {MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID} ;
		c = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, p, null, null, null);
		count = c.getCount();
		int i = c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);

		lv.setAdapter(new myadapter(getApplicationContext()));

		final int whichdata = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);   //at a particular position of cursor which data? video path,resolution,duration etc

		String[] a= new String[count] ;
		final Intent intent = new Intent(MainActivity.this, Playback.class);
		for (int j=0 ; j < count ;j++)
		{
			c.moveToPosition(j);
			a[j] = c.getString(whichdata);
		}
		intent.putExtra("videopaths", a);

		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method

					c.moveToPosition(p3);
					intent.putExtra("pos", p3);
					startService(intent);
					
				}
			});

    }

	public class myadapter extends BaseAdapter
	{
		Context videoc ;

		public myadapter(Context c)
		{
			videoc = c ;
		}

		@Override
		public int getCount()
		{
			// TODO: Implement this method
			return count;
		}

		@Override
		public Object getItem(int p1)
		{
			// TODO: Implement this method
			return p1;
		}

		@Override
		public long getItemId(int p1)
		{
			// TODO: Implement this method
			return p1;
		}

		@Override
		public View getView(int p1, View v, ViewGroup vg)
		{
			// TODO: Implement this method
		//	v = null ;
			//if (v == null)
		//	{
				v = getLayoutInflater().from(videoc).inflate(R.layout.listitem, vg, false);
				int videocolumnindex = c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
				
				c.moveToPosition(p1);

				TextView t = (TextView)v.findViewById(R.id.tvvideoname);
				final ImageView thumbnail = (ImageView)v.findViewById(R.id.ivthumb);
				
				String s = c.getString(videocolumnindex);
				t.setText(s);
				
				// thumbnail code
				long videocolumnid = c.getLong(c.getColumnIndex(MediaStore.Video.Media._ID));
				ContentResolver cr = getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1 ;

				Bitmap cthumb = MediaStore.Video.Thumbnails.getThumbnail(cr, videocolumnid, MediaStore.Video.Thumbnails.MINI_KIND, options);
				thumbnail.setImageBitmap(cthumb);
				cthumb = null;
		
		//	}
			return v;

		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.layout.optionslist, menu);
		return true ;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setTitle("Who developed this stupid app ?");
		d.setMessage("App developed by Priyansh Balyan.\nCopyright © 2015.\nList Menu is developed by Lovey Kumar Varshney, a supporter.");
		d.setButton("FINE", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
				}
			});
		d.setButton2("Rate the App!", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
				}
			});
		d.show();
		
		return false ;
	}




}

