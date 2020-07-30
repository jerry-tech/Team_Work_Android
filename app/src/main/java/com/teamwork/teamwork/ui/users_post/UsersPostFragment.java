package com.teamwork.teamwork.ui.users_post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.teamwork.teamwork.AppSingleton;
import com.teamwork.teamwork.ArticleComment;
import com.teamwork.teamwork.ImageComment;
import com.teamwork.teamwork.PostData;
import com.teamwork.teamwork.R;
import com.teamwork.teamwork.UsersRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UsersPostFragment extends Fragment {

    private UsersPostViewModel mPostViewModel;
    private static final String URL_FOR_FEEDS = "https://myteamworkproject.herokuapp.com/v1/feed";
    //progress bar
    ProgressBar isLoading;
    ImageButton btArtCom;
    ImageButton btImgCom;

    //Using Shared preferences.
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String token;
    private List<PostData> mData = new ArrayList<>();
    PostData postData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mPostViewModel =
                ViewModelProviders.of(this).get(UsersPostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_users_post, container, false);
//        final TextView textView = root.findViewById(R.id.text_send);
//        mPostViewModel.getText().observe(this, textView::setText);


        //getting android shared preference
        //getting the token of the user with shared preference.
        preferences = requireActivity().getSharedPreferences("User Details", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);
        String userId = preferences.getString("userId", null);
        String firstName = preferences.getString("firstName", null);
        String lastName = preferences.getString("lastName", null);
        String UserImg = preferences.getString("profileImage", null);


        //calling the method used to get all feeds
        getUsersPost(root, userId, firstName, lastName, UserImg);

        return root;
    }

    private void getUsersPost(View root, String id, String firstName, String lastName, String userImg) {

        //progress bar
        isLoading = root.findViewById(R.id.postSecondBar);
        isLoading.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_FOR_FEEDS + "/" + id, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");

                if (status.equalsIgnoreCase("success")) {

//                    Setting the visibility of a progress bar
                    isLoading.setVisibility(View.GONE);

                    JSONArray postArray = jsonObject.getJSONArray("data");

                    //now looping through all the elements of the json array
                    for (int i = 0; i < postArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject postObject = postArray.getJSONObject(i);

                        //passing data through one of the overloaded constructor of the post data class
                        postData = new PostData(postObject.optInt("gif_id"), postObject.optInt("article_id"), postObject.getInt("users_user_id"), postObject.optString("imageUrl")
                                , postObject.getString("title"), postObject.optString("article"), lastName, firstName, userImg,
                                postObject.optString("flagged"), postObject.getString("dateCreated"));

                        //adding post data to the arrayList
                        mData.add(postData);
                    }

                    //method to setLayout and adapter
                    initializeUsersPosts(root);

                } else {
//                    //Setting the visibility of a progress bar
                    isLoading.setVisibility(View.VISIBLE);

                    String errorMsg = jsonObject.getString("error");
                    Toasty.custom(requireContext(), errorMsg, R.drawable.ic_close, R.color.colorPrimary, Toast.LENGTH_LONG, true, true).show();
                }
            } catch (JSONException e) {
                //Setting the visibility of a progress bar
                isLoading.setVisibility(View.VISIBLE);
                Log.e("Feeds Error", "Error in getting all posts: " + e);
                Toasty.custom(getContext(), "No Network Connectivity" + e, R.drawable.ic_close, R.color.colorPrimary, Toast.LENGTH_LONG, true, true).show();
            }

        }, error -> {
            //Setting the visibility of a progress bar
            isLoading.setVisibility(View.VISIBLE);

            Log.e("Feeds Error", "Error in getting all posts: " + error.getMessage());
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
        AppSingleton.getInstance(getContext()).addToRequestQueue(stringRequest, "user");
    }


    private void initializeUsersPosts(View root) {
//recycler view
        final RecyclerView displayUsersPost = root.findViewById(R.id.list_users_posts);
//linear manager
        final LinearLayoutManager userPostManager = new LinearLayoutManager(getContext());
        displayUsersPost.setLayoutManager(userPostManager);

        //recycler adapter
        final UsersRecyclerAdapter mRecycler = new UsersRecyclerAdapter(getContext(), mData);
        displayUsersPost.setAdapter(mRecycler);

    }

    private void ArticleComments(int articleId) {
        Intent intent = new Intent(getContext(), ArticleComment.class);
        intent.putExtra("articleId", articleId);
        startActivity(intent);
    }

    private void ImageComments(int gifId) {
        Intent intent = new Intent(getContext(), ImageComment.class);
        intent.putExtra("articleId", gifId);
        startActivity(intent);
    }

}