package net.infobank.lab.testparseapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import net.infobank.lab.testparseapp.fragment.FragmentA;
import net.infobank.lab.testparseapp.fragment.FragmentB;
import net.infobank.lab.testparseapp.fragment.FragmentC;


public class MainActivity extends FragmentActivity {
    //이것 또한 Slack 추가

    private final String PARSE_APPLICATION_ID = "ch68mVW8T9mWjkm14MQqbXVzX8fzI9uuNwSubY53";
    private final String PARSE_CLIENT_KEY = "9PZBYzBJZH96Ou1M9ACV7m91GeKPfoZ92Lrps1VG";


    private PagerSlidingTabStrip slidingTabStrip;
    private ViewPager mViewPager;            // View pager를 지칭할 변수

    private String[] tabTitle = {" 단말 대여 ", " 단말 반납 ", " 대여 현황 "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingTabStrip = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);

        ParsePagerAdapter parsePagerAdapter = new ParsePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(parsePagerAdapter);
        slidingTabStrip.setViewPager(mViewPager);


        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);


    }

    class ParsePagerAdapter extends FragmentPagerAdapter {

        public ParsePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f;
            switch (position) {
                case 0:
                    f = FragmentA.newInstance("");
                    break;
                case 1:
                    f = FragmentB.newInstance("");
                    break;
                case 2:
                    f = FragmentC.newInstance("");
                    break;
                default:
                    throw new IllegalArgumentException("not this many fragments: " + position);
            }
            return f;

        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position % tabTitle.length].toUpperCase();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
