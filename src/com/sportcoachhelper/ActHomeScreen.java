package com.sportcoachhelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.sportcoachhelper.fragments.ScreenSlidePageFragment;
import com.sportcoachhelper.util.Utility;

public class ActHomeScreen extends FragmentActivity {

	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;
	private ImageView imvLeftArrow;
	private ImageView imvRightArrow;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Utility.setHoloTheme(this);
		
		setContentView(R.layout.home);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        imvLeftArrow = (ImageView) findViewById(R.id.imvLeftArrow);
        imvRightArrow = (ImageView) findViewById(R.id.imvRightArrow);
        
        
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    
        
        
	    mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {

				
			}
			
			@Override
			public void onPageScrolled(int pos, float arg1, int arg2) {
				setPositionInAdapter(pos);
				
			}

			
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		int current = mPager.getCurrentItem();
		
		setPositionInAdapter(current);
	}
	
	
	private void setPositionInAdapter(int pos) {
		if(pos==0)  {
			imvLeftArrow.setVisibility(View.INVISIBLE);
		} else {
			imvLeftArrow.setVisibility(View.VISIBLE);
		}
		
		
		if(pos==mPagerAdapter.getCount()-1)  {
			imvRightArrow.setVisibility(View.INVISIBLE);
		} else {
			imvRightArrow.setVisibility(View.VISIBLE);
		}
	}
	
	
	 /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private String[] fields;

		public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fields = getResources().getStringArray(R.array.fields);
        }

        @Override
        public Fragment getItem(int position) {
        	ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        	fragment.setPosition(position);	
        	fragment.setLabel(fields[position]);
            return fragment;
        }

        @Override
        public int getCount() {
            return fields.length;
        }
    }
    
    
    @Override
    public void onBackPressed() {
    }
	
}
