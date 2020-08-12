package com.teamwork.teamwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleComment extends AppCompatActivity {

    private static final String URL_FOR_FEEDS = "https://myteamworkproject.herokuapp.com/v1/articles";
    private final String URL = "https://myteamworkproject.herokuapp.com";
    Bitmap bitmap;
    private String token;
    SharedPreferences preferences;
    private TextView mTextName;
    private TextView mTextTitle;
    private TextView mTextContent;
    private TextView mTextDate;
    private CircleImageView mUserImage;
    private CommentData mMCommentData;
    private List<CommentData> mCommentDataArrayList = new ArrayList<>();
    private TextInputEditText mTxtComment;
    ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);


        preferences = this.getSharedPreferences("User Details", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);

        //type-casting the article id from int to String
        Intent intent = getIntent();
        int jj = intent.getIntExtra("ArticleId", 0);

        StringBuilder sb = new StringBuilder();
        String articleId = sb.append(jj).toString();

        //finding view by id
        mTextName = findViewById(R.id.clickName);
        mTextTitle = findViewById(R.id.clickTitle);
        mTextContent = findViewById(R.id.clickContent);
        mTextDate = findViewById(R.id.clickDate);
        mUserImage = findViewById(R.id.circleImageView);
        mProgressBar = findViewById(R.id.txtProgressComment);

        //textInputEditText
        mTxtComment = findViewById(R.id.comment);

        //method used to get a single post
        getSinglePost(articleId);

        //submit a comment by the click of  DONE key
        mTxtComment.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                   calling the method to be executed by the click of the DONE key
                comment(articleId);
            }
            return false;
        });

//        submitting a comment by the click of a button
        ImageButton subComment = findViewById(R.id.subComment);
        subComment.setOnClickListener(v -> comment(articleId));

    }

    //method used to get post details after the comment button click
    private void getSinglePost(String id) {

        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_FOR_FEEDS + "/" + id, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {

                    //setting the visibility of the progress bar
                    mProgressBar.setVisibility(View.GONE);

                    //getting the jsonObject for data
                    JSONObject postObject = jsonObject.getJSONObject("data");
                    Log.i("Comments", postObject.toString());

                    //adding full name
                    String fullName = toSentence(postObject.getString("firstname")) + " " +
                            toSentence(postObject.getString("lastname"));
                    mTextName.setText(fullName);

                    //adding title
                    String title = toSentence(postObject.getString("title"));
                    mTextTitle.setText(title);

                    //adding article
                    String article = postObject.getString("article");
                    mTextContent.setText(article);

                    //adding date
                    String date = postObject.getString("createdOn");
                    mTextDate.setText(date);

                    //adding userImage
                    String image = postObject.getString("userimage");

                    new ImageDownloader(mUserImage).execute(URL + "/images/" + image);
                    mUserImage.setImageBitmap(bitmap);

                    //getting the JSON array used to get all comment
                    JSONArray commentArray = jsonObject.getJSONObject("data").getJSONArray("comments");
                    Log.i("comm", commentArray.toString());

                    for (int i = 0; i < commentArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject commentObject = commentArray.getJSONObject(i);

                        //passing data through one of the overloaded constructor of the comment data class
                        mMCommentData = new CommentData(commentObject.getInt("comment_id"), commentObject.getString("comment"), commentObject.getInt("article_article_id"), commentObject.getString("createdon"), commentObject.getString("firstName"), commentObject.getString("lastName"), commentObject.getString("userImage"));

                        //adding the object of the constructor to the arrayList
                        mCommentDataArrayList.add(mMCommentData);

                        //method to setLayout and adapter
                        initializeDisplayContent();
                    }

                } else {
                    //setting the visibility of the progress bar
                    mProgressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());

                new ResponseDialog().showCancelableDialog("Network Error", "No network or poor connection", R.drawable.ic_baseline_warning_24, ArticleComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
                //setting the visibility of the progress bar
                mProgressBar.setVisibility(View.GONE);
            }

        }, error -> {

        }) {//overridden method used to add token to the header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(this).addToRequestQueue(stringRequest, "comment");
    }

    //method used to change a word to sentence case
    public String toSentence(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    //initializing display content for the recycler view
    private void initializeDisplayContent() {
        //display content
        //finding the recycler view by id
        final RecyclerView displayComment = findViewById(R.id.list_post_comments);
        //setting the type of layout manager for the recycler view
        final LinearLayoutManager commentLayoutManager = new LinearLayoutManager(this);
        displayComment.setLayoutManager(commentLayoutManager);

//        instantiating the Comment recycler adapter so as to set the adapter type
        final CommentRecyclerAdapter mCommentRecycler = new CommentRecyclerAdapter(this, mCommentDataArrayList);
        displayComment.setAdapter(mCommentRecycler);
    }


    private void comment(String articleId) {

        if (validateComment()) {
            addComment(articleId);
        } else {
            new ResponseDialog().showCancelableDialog("Empty Field", "Cannot enter empty comment", R.drawable.ic_baseline_warning_24, ArticleComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
        }

    }


    private void addComment(String articleId) {

        //progressDialog
        mProgressDialog.setMessage("Saving Comment.....");
        showDialog();

        String comment = Objects.requireNonNull(mTxtComment.getText()).toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_FEEDS + "/" + articleId + "/" + "comment", response -> {
            //showing progress dialog
            showDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                String status = jObj.getString("status");


                //Logic for correct login information
                if (status.equalsIgnoreCase("success")) {
                    //hide progress dialog
                    hideDialog();

                    new ResponseDialog().showCancelableDialog("Comment Success", "Comment added successfully", R.drawable.ic_baseline_check_circle_24, ArticleComment.this, getResources().getDrawable(R.drawable.alert_bg, null));

                    mTxtComment.setText("");
                } else if (status.equalsIgnoreCase("forbidden")) {

                    new ResponseDialog().showCancelableDialog("Insertion Error", "Forbidden format of comment", R.drawable.ic_baseline_warning_24, ArticleComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
                }

            } catch (JSONException e) {
                String exError = e.getMessage();
                //hide progress dialog
                hideDialog();

                new ResponseDialog().showCancelableDialog("Insertion Error", exError, R.drawable.ic_baseline_warning_24, ArticleComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
            }

        }, error -> {
            Log.i("errorscomment", error.toString());

            //hide progress dialog
            hideDialog();

            new ResponseDialog().showCancelableDialog("Network Error", "No network or poor connection", R.drawable.ic_baseline_warning_24, ArticleComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
        }) {

            //adding headers for the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }

            //Mapping the users input with the database user information
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<>();
                params.put("comment", comment);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, "articleComment");

    }

    //method used to validate comment field if it's empty or not
    private boolean validateComment() {
        String comment = mTxtComment.getText().toString().trim();

        if (comment.isEmpty()) {
            return false;
        }
        return true;
    }

    private void showDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

}
