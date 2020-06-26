package com.teamwork.teamwork.ui.post;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.tabs.TabLayout;
import com.teamwork.teamwork.PostArticle;
import com.teamwork.teamwork.PostImage;
import com.teamwork.teamwork.R;
import com.teamwork.teamwork.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFragment extends Fragment implements PostArticle.OnFragmentInteractionListener, PostImage.OnFragmentInteractionListener {

    private PostViewModel mPostViewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PostArticle  mPostArticle;
    private PostImage  mPostImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mPostViewModel =
                ViewModelProviders.of(this).get(PostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_post,container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        mPostViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        /*-------------------------------------------------------*/
        viewPager = root.findViewById(R.id.viewPagerPost);

        tabLayout = root.findViewById(R.id.tabProfile);

        doTabLayoutItem();//calling the tab implementation method
        return root;
    }
    private void  doTabLayoutItem(){
        /* instantiating the two fragment classes*/
        mPostArticle = new PostArticle();
        mPostImage = new PostImage();

        //adding the viewpager to the tabLayout
        tabLayout.setupWithViewPager(viewPager);


        //setting the color of the icons
        tabLayout.setTabIconTint(ContextCompat.getColorStateList(requireContext(), R.color.colorWhite));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),0);

        //adding tabs title to the viewPager adapter
        viewPagerAdapter.addFragment(mPostArticle,"Post Article");
        viewPagerAdapter.addFragment(mPostImage,"Post Image");
        viewPager.setAdapter(viewPagerAdapter);


        //adding icons to tabs
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_postarticle_white_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_postimage_white_24dp);
    }


    //creating the viewpager class that extend the FragmentPagerAdapter
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        //used to add a list of fragments
        private List<Fragment> fragments = new ArrayList<>();
        //list of fragment title
        private List<String> fragmentTitle = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behaviour) {
            super(fm,behaviour);
        }

        //method used to add fragments
        void addFragment(Fragment fragment, String title){
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
    public void onFragmentInteraction(Uri uri) {

    }
}