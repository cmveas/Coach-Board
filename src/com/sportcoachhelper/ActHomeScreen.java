package com.sportcoachhelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.sportcoachhelper.fragments.ScreenSlidePageFragment;

public class ActHomeScreen extends FragmentActivity {

	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.home);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
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
