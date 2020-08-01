package com.teamwork.teamwork;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.teamwork.teamwork.ui.home.HomeFragment;
import com.teamwork.teamwork.ui.users_post.UsersPostViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPost extends AppCompatActivity implements PostArticle.OnFragmentInteractionListener, PostImage.OnFragmentInteractionListener {

    private UsersPostViewModel mPostViewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PostArticle mPostArticle;
    private PostImage mPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        /*-------------------------------------------------------*/
        viewPager = findViewById(R.id.viewPagerPost);

        tabLayout = findViewById(R.id.tabProfile);

        doTabLayoutItem();//calling the tab implementation method

        //back arrow btn
        TextView textView = findViewById(R.id.backPost);
        textView.setOnClickListener(event -> handleBackIntent());
    }

    private void doTabLayoutItem() {
        /* instantiating the two fragment classes*/
        mPostArticle = new PostArticle();
        mPostImage = new PostImage();

        //adding the viewpager to the tabLayout
        tabLayout.setupWithViewPager(viewPager);


        //setting the color of the icons
        tabLayout.setTabIconTint(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorWhite));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        //adding tabs title to the viewPager adapter
        viewPagerAdapter.addFragment(mPostArticle, "Post Article");
        viewPagerAdapter.addFragment(mPostImage, "Post Image");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);


        //adding icons to tabs
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_postarticle_white_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_postimage_white_24dp);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    //creating the viewpager class that extend the FragmentPagerAdapter
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        //used to add a list of fragments
        private List<Fragment> fragments = new ArrayList<>();
        //list of fragment title
        private List<String> fragmentTitle = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behaviour) {
            super(fm, behaviour);
        }

        //method used to add fragments
        void addFragment(Fragment fragment, String title) {
                fragments.add(fragment);
                fragmentTitle.add(title);

        }

        //used get the fragment position
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        //used to the count of fragments
        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
        //Intent to back to home(when the back button is clicked)
        handleBackIntent();
    }

    //method used to navigate back to Home Activity(Fragment to Activity)
    public void handleBackIntent(){
        new Handler().postDelayed(() -> {
            this.finish();
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.posting, fragment);
//            transaction.commit();
        }, 1000);
    }


}
