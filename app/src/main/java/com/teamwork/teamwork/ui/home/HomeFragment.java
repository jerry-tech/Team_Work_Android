package com.teamwork.teamwork.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamwork.teamwork.AddPost;
import com.teamwork.teamwork.AppSingleton;
import com.teamwork.teamwork.PostData;
import com.teamwork.teamwork.PostRecyclerAdapter;
import com.teamwork.teamwork.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String URL_FOR_FEEDS = "https://myteamworkproject.herokuapp.com/v1/feed";

    //Using Shared preferences.
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String token;
    private List<PostData> mData = new ArrayList<>();
    PostData postData;
    //progress bar
    ProgressBar isLoading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //floating action button
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fabAddPost);
        floatingActionButton.setOnClickListener(event -> addPost());

        //getting android shared preference
        //Getting the token of the user with shared preference.
        preferences = requireActivity().getSharedPreferences("User Details", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);

        //calling the method used to get all feeds
        getAllFeeds(root);

        return root;
    }

    //method used to get all post
    private void getAllFeeds(View root) {
        //progress bar
        isLoading = root.findViewById(R.id.postBar);
        isLoading.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_FOR_FEEDS, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");

                if (status.equalsIgnoreCase("success")) {

                    //Setting the visibility of a progress bar
                    isLoading.setVisibility(View.GONE);

                    JSONArray postArray = jsonObject.getJSONArray("data");

                    //now looping through all the elements of the json array
                    for (int i = 0; i < postArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject postObject = postArray.getJSONObject(i);

                        //passing data through one of the overloaded constructor of the post data class
                        postData = new PostData(postObject.optInt("gif_id"), postObject.optInt("article_id"), postObject.getInt("users_user_id"),
                                postObject.optString("imageUrl"), postObject.getString("title"), postObject.optString("article"), postObject.getString("firstName"),
                                postObject.getString("lastName"), postObject.getString("userImage"), postObject.optString("flagged"), postObject.getString("dateCreated"));

                        //adding post data to the arrayList
                        mData.add(postData);
                        Log.d("All Posts: ", mData.toString());
                    }
                    //method to setLayout and adapter
                    initializeDisplayContent(root);

                } else {
                    //Setting the visibility of a progress bar
                    isLoading.setVisibility(View.VISIBLE);

                    String errorMsg = jsonObject.getString("error");
                    Toasty.custom(getContext(), errorMsg, R.drawable.ic_close, R.color.colorPrimary, Toast.LENGTH_LONG, true, true).show();
                }
            } catch (JSONException e) {
                //Setting the visibility of a progress bar
                isLoading.setVisibility(View.VISIBLE);
//                Log.e("Feeds Error", "Error in getting all posts: " + e);
                Toasty.custom(getContext(), "No Network Connectivity", R.drawable.ic_close, R.color.colorPrimary, Toast.LENGTH_LONG, true, true).show();
            }

        }, error -> {
            //Setting the visibility of a progress bar
            isLoading.setVisibility(View.VISIBLE);

//            Log.e("Feeds Error", "Error in getting all posts: " + error.getMessage());
            Toasty.custom(getContext(), "Error in getting all posts: " + error.getMessage(), R.drawable.ic_close, R.color.colorPrimary, Toast.LENGTH_LONG, true, true).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(stringRequest, "feeds");
    }

    //intent used to navigate create post activity
    private void addPost() {
        Intent intent = new Intent(getContext(), AddPost.class);
        startActivity(intent);
    }

    //initializing display content
    private void initializeDisplayContent(View root) {
        //display content
        final RecyclerView displayPosts = root.findViewById(R.id.list_posts);
        final LinearLayoutManager postLayoutManager = new LinearLayoutManager(getContext());
        displayPosts.setLayoutManager(postLayoutManager);

        final PostRecyclerAdapter mPostRecycler = new PostRecyclerAdapter(getContext(), mData);
        displayPosts.setAdapter(mPostRecycler);
    }

}