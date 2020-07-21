package com.teamwork.teamwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
	//Creating the tag to handle success or cancel events.
	private static final String TAG = "LoginActivity";
	//The url for the LoginAPI
	private static final String URL_FOR_LOGIN = "https://myteamworkproject.herokuapp.com/v1/auth/signin";

	ProgressDialog progressDialog;
	TextInputEditText password, email;
	Button loginBtn;
	String userImage, jobRole, address, department, gender, emailAddress, firstName, lastName, dateOn, userId;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Progress dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		//Getting the email, password and login button.
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		loginBtn = findViewById(R.id.btnLogin);


		//Triggering the login button
		loginBtn.setOnClickListener(e -> {
			if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
				Toast.makeText(this, "Email and Password can't be empty", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this, UserOptions.class));//subject to removal
			}
			//Calling the login button.
			else {
				loginUser(email.getText().toString(), password.getText().toString());
			}
		});

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

			hideDialog();
			try {
				JSONObject jObj = new JSONObject(response);
				String status = jObj.getString("status");

				//Logic for correct login information
				if (status.equalsIgnoreCase("success")) {
					//storing the token in a string
					String usersToken = jObj.getJSONObject("data").getString("token");

					JSONArray userCredentials = jObj.getJSONObject("data").getJSONArray("userData");

					//storing the user image URL in string
					for (int i = 0; i < userCredentials.length(); i++) {
						//getting the json object of the particular index inside the array
						JSONObject postObject = userCredentials.getJSONObject(i);

						userImage = postObject.getString("userImage");
						jobRole = postObject.getString("jobRole");
						gender = postObject.getString("gender");
						department = postObject.getString("dept");
						address = postObject.getString("address");
						emailAddress = postObject.getString("email");
						firstName = postObject.getString("firstName");
						lastName = postObject.getString("lastName");
						dateOn = postObject.getString("createdOn");
						userId = postObject.getString("user_id");

						Log.d("Users", userImage);
					}


					//add the token to android shared preferences
					preferences = getSharedPreferences("User Details", Context.MODE_PRIVATE);
					editor = preferences.edit();
					editor.putString("token", usersToken);//adding token to android shared preferences
					editor.putString("profileImage", userImage);//adding users image to android shared preferences
					editor.putString("jobRole", jobRole);//adding job role to android shared preferences
					editor.putString("gender", gender);//adding gender to android shared preferences
					editor.putString("department", department);//adding department to android shared preferences
					editor.putString("address", address);//adding address to android shared preferences
					editor.putString("email", emailAddress);//adding email to android shared preferences
					editor.putString("firstName", firstName);//adding firstName to android shared preferences
					editor.putString("lastName", lastName);//adding lastName to android shared preferences
					editor.putString("userId", userId);//adding userId to android shared preferences
					editor.putString("dateCreated", dateOn);//adding date to android shared preferences
					editor.apply();

					startActivity(new Intent(this, UserOptions.class));

				} else {

					String errorMsg = jObj.getString("error");
					Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				e.printStackTrace();

				Log.e("Errorzz", e.getMessage());
			}

		}, error -> {
			Log.e(TAG, "Login Error: " + error.getMessage());
			Toast.makeText(this, "Error Occured from fetching the data", Toast.LENGTH_SHORT).show();

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

	private void showDialog() {
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	private void hideDialog() {
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}
//	public void showResponseDialog(){
//		MaterialAlertDialogBuilder(context)
//				.setTitle(resources.getString(R.string.title))
//				.setMessage(resources.getString(R.string.supporting_text))
//				.setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
//			// Respond to neutral button press
//		}
//        .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
//			// Respond to negative button press
//		}
//        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
//			// Respond to positive button press
//		}
//        .show()
//	}
}
