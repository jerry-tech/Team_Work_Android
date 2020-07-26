package com.teamwork.teamwork;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.teamwork.teamwork.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostArticle.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostArticle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostArticle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    SharedPreferences preferences;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String URL = "https://myteamworkproject.herokuapp.com/v1/articles";
    TextInputEditText articleTitle, articleBody;
    Button btnPostArticle;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String token;
    private OnFragmentInteractionListener mListener;

    public PostArticle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostArticle.
     */
    // TODO: Rename and change types and number of parameters
    public static PostArticle newInstance(String param1, String param2) {
        PostArticle fragment = new PostArticle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_post_article, container, false);

        articleTitle = root.findViewById(R.id.postTitle);
        articleBody = root.findViewById(R.id.postArticle);
        btnPostArticle = root.findViewById(R.id.subArticle);

        preferences = requireActivity().getSharedPreferences("User Details", Context.MODE_PRIVATE);
        token = preferences.getString("token", null);

        btnPostArticle.setOnClickListener(v -> {
            if(articleTitle.getText().toString().isEmpty() || articleBody.getText().toString().isEmpty()){
                String warningMess = "Post title or description can't be empty";
                //warning material dialog
                new ResponseDialog().showCancelableDialog("Login Error",warningMess,R.drawable.ic_baseline_warning_24,getContext(), getResources().getDrawable(R.drawable.alert_bg,null));
            }
            else{
                addPostArticle(articleTitle.getText().toString().trim(), articleBody.getText().toString());
                //Shwoing the users that the aritcle got posted.
                Toast.makeText(root.getContext(), "Article Posted", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void addPostArticle(String postTitle, String postBody) {
        if (validateArticleFields()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URL, res -> {
                try {
                    JSONObject jObj = new JSONObject(res);
                    String status = jObj.getString("status");

                    if (status.equalsIgnoreCase("success")) {
                      //do something
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }, error -> {
                Log.e("Login Error: ", error.getMessage());
                Toast.makeText(getContext(), "Error Occured in inserting data", Toast.LENGTH_SHORT).show();
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
                    params.put("title", postTitle);
                    params.put("article", postBody);

                    return params;
                }

            };
            // Adding request to request queue
            AppSingleton.getInstance(
                    getContext()).
                    addToRequestQueue(stringRequest, "post");

        }

    }

    public boolean validateArticleFields() {
        boolean valid = true;
        if (articleTitle.getText().toString().isEmpty() || articleBody.getText().toString().isEmpty()) {
            valid = false;
        } else if (articleTitle.getText().toString().isEmpty() && articleBody.getText().toString().isEmpty()) {
            valid = false;
        }

        return valid;
    }

}
