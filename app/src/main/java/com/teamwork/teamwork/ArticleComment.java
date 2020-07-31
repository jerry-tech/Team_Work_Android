package com.teamwork.teamwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleComment extends AppCompatActivity {

    private static final String URL_FOR_FEEDS = "https://myteamworkproject.herokuapp.com/v1/articles";
    final String URL = "https://myteamworkproject.herokuapp.com";
    Bitmap bitmap;
    String token;
    SharedPreferences preferences;
    private TextView mTextName;
    private TextView mTextTitle;
    private TextView mTextContent;
    private TextView mTextDate;
    private CircleImageView mUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = this.getSharedPreferences("User Details", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);

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


        getSinglePost(articleId);


    }

    private void getSinglePost(String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_FOR_FEEDS + "/" + id, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {

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

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

        }, error -> {

        }) {
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

}
