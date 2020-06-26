package com.teamwork.teamwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
	//Container to hold email and password.
	TextInputEditText email, password;
	//Creating the tag to handle success or cancel events.
	private static final String TAG = "LoginActivity";

	//The url for the LoginAPI
	private static final String URL_FOR_LOGIN = "https://myteamworkproject.herokuapp.com/v1/auth/signin";

	ProgressDialog progressDialog;
	Button loginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Progress dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		//Getting the username, password and login button.
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		loginBtn = findViewById(R.id.btnLogin);

		//Setting the click event of the login button.
		loginBtn.setOnClickListener(e -> {
					if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
						Toast.makeText(this, "Email and Password cannot be empty.", Toast.LENGTH_SHORT).show();
					} else {

						loginUser(email.getText().toString(), password.getText().toString());

					}

				}

		);


		//used for showing time
		TextView timeLabel = findViewById(R.id.displayTime);
		Calendar calendar = Calendar.getInstance();
		timeLabel.setText(calendar.getTime().toString());
	}

	//Logic for login button.
	private void loginUser(final String email, final String password) {
		// Tag used to cancel the request
		String cancel_req_tag = "login";
		progressDialog.setMessage("Logging you in...");
		showDialog();
		StringRequest strReq = new StringRequest(Request.Method.POST,
				URL_FOR_LOGIN, response -> {
			Log.d(TAG, "Login Response: " + response);
			hideDialog();
			try {
				JSONObject jObj = new JSONObject(response);
				String status = jObj.getString("status");

				//Logic for correct login information
				if (status.equalsIgnoreCase("success")) {
					Toast.makeText(this, "Working Just FIne", Toast.LENGTH_SHORT).show();
				} else {

					String errorMsg = jObj.getString("error");
					Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}, error -> {
			Log.e(TAG, "Login Error: " + error.getMessage());
			Toast.makeText(this, "An Error occured from fetching.", Toast.LENGTH_SHORT).show();
			hideDialog();
		}) {
			//Mapping the users input with the database user information
			@Override
			protected Map<String, String> getParams() {
				// Posting params to login url
				Map<String, String> params = new HashMap<>();
				params.put("email", email);
				params.put("password", password);
				return params;
			}
		};
		// Adding request to request queue
		AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
	}


	//Logic for the dialog spinner.

	private void showDialog() {
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	private void hideDialog() {
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}
}
