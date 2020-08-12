package com.teamwork.teamwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ImageComment extends AppCompatActivity {

    private CircleImageView mUserImag;
    private TextView mComName;
    private TextView mComTitle;
    private ImageView mComImage;
    private TextView mComDate;
    private ProgressDialog mProgressDialog;
    private static final String URL_FOR_IMG = "https://myteamworkproject.herokuapp.com/v1/gifs";
    private final String URL = "https://myteamworkproject.herokuapp.com";
    private SharedPreferences mPreferences;
    private String mToken;
    private CommentData mCommentData;
    private List<CommentData> mCommentDataArrayList = new ArrayList<>();
    private TextInputEditText mMCommentField;
    private ImageButton mSubButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        //getting token
        mPreferences = this.getSharedPreferences("User Details", Context.MODE_PRIVATE);
        mToken = mPreferences.getString("token", null);

        mUserImag = findViewById(R.id.circleImageView2);
        mComName = findViewById(R.id.clickName2);
        mComTitle = findViewById(R.id.clickTitle2);
        mComImage = findViewById(R.id.clickContent2);
        mComDate = findViewById(R.id.clickDate2);
        mMCommentField = findViewById(R.id.imgcomment);
        mSubButton = findViewById(R.id.subComment2);

        //type-casting the article id from int to String
        Intent intent = getIntent();
        int id = intent.getIntExtra("ImageId", 0);

        StringBuilder sb = new StringBuilder();
        String imageId = sb.append(id).toString();

        //method used to get a particular image post
        getSingleImg(imageId);

        //action listener used for submitting comment
        mSubButton.setOnClickListener(v -> {
            String comment = Objects.requireNonNull(mMCommentField.getText()).toString();
            if (comment.isEmpty()) {
                new ResponseDialog().showCancelableDialog("Empty Field", "Cannot post an empty comment", R.drawable.ic_baseline_warning_24, ImageComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
            } else {
                addComment(imageId, mMCommentField);
            }

        });
    }

    private void getSingleImg(String id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_FOR_IMG + "/" + id
                , response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {

                    JSONObject postObject = jsonObject.getJSONObject("data");

                    //adding fullName
                    String fullName = toSentence(postObject.getString("firstname")) + " " + toSentence(postObject.getString("lastname"));
                    mComName.setText(fullName);

                    //adding title
                    String title = toSentence(postObject.getString("title"));
                    mComTitle.setText(title);

                    //adding Image
                    String image = postObject.getString("url");
                    new ImageDownloader(mComImage).execute(URL + "/images/" + image);

                    //adding user image
                    String userImage = postObject.getString("userImage");
                    new ImageDownloader(mUserImag).execute(URL + "/images/" + userImage);

                    //adding date
                    String date = postObject.getString("datecreated");
                    mComDate.setText(date);

                    //post array of comments
                    JSONArray commentArray = jsonObject.getJSONObject("data").getJSONArray("comments");
                    for (int i = 0; i < commentArray.length(); i++) {
                        //getting the json object of the particular index inside the array
                        JSONObject commentObject = commentArray.getJSONObject(i);

                        //passing data through one of the overloaded constructor of the comment data class
                        mCommentData = new CommentData(commentObject.getInt("gif_comment_id"), commentObject.getString("comment"), commentObject.getString("createdon"), commentObject.getString("firstName"), commentObject.getString("lastName"), commentObject.getString("userImage"));

                        //adding the object of the constructor to the arrayList
                        mCommentDataArrayList.add(mCommentData);

                        //method to setLayout and adapter
                        initializeDisplayContent();
                    }


                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }, error -> {

        }) {//overridden method used to add token to the header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", mToken);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(this).addToRequestQueue(stringRequest, "comment");

    }

    //method used to add comment
    private void addComment(String gifId, TextInputEditText commentText) {

        String comment = commentText.getText().toString();
//        //progressDialog
        mProgressDialog.setMessage("Saving Comment.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_IMG + "/" + gifId + "/" + "comment", response -> {

            try {
                JSONObject jObj = new JSONObject(response);
                String status = jObj.getString("status");


                //Logic for correct login information
                if (status.equalsIgnoreCase("success")) {
                    //hide progress dialog
                    hideDialog();

                    //setting text to an empty string
                    commentText.setText("");
                    new ResponseDialog().showCancelableDialog("Comment Success", "Comment added successfully", R.drawable.ic_baseline_check_circle_24, ImageComment.this, getResources().getDrawable(R.drawable.alert_bg, null));

                } else if (status.equalsIgnoreCase("forbidden")) {

                    new ResponseDialog().showCancelableDialog("Insertion Error", "Forbidden format of comment", R.drawable.ic_baseline_warning_24, ImageComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
                }

            } catch (JSONException e) {
                String exError = e.getMessage();

                //hide progress dialog
                hideDialog();

                new ResponseDialog().showCancelableDialog("Insertion Error", exError, R.drawable.ic_baseline_warning_24, ImageComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
            }

        }, error -> {
            Log.i("errorscomment", error.toString());

            //hide progress dialog
            hideDialog();
            new ResponseDialog().showCancelableDialog("Network Error", "No network or poor connection", R.drawable.ic_baseline_warning_24, ImageComment.this, getResources().getDrawable(R.drawable.alert_bg, null));
        }) {

            //adding headers for the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", mToken);
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

    //initializing display content for the recycler view
    private void initializeDisplayContent() {
        //display content
        //finding the recycler view by id
        final RecyclerView displayComment = findViewById(R.id.list_post_Img_comments);
        //setting the type of layout manager for the recycler view
        final LinearLayoutManager commentLayoutManager = new LinearLayoutManager(this);
        displayComment.setLayoutManager(commentLayoutManager);

//        instantiating the Comment recycler adapter so as to set the adapter type
        final ImgCommentRecyclerAdapter mCommentRecycler = new ImgCommentRecyclerAdapter(this, mCommentDataArrayList);
        displayComment.setAdapter(mCommentRecycler);
    }

    //method used to change a word to sentence case
    public String toSentence(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
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
