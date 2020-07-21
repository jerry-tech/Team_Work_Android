package com.teamwork.teamwork.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamwork.teamwork.ImageDownloader;
import com.teamwork.teamwork.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel notificationsViewModel;
    SharedPreferences preferences;
    public final String URL = "https://myteamworkproject.herokuapp.com";
    String userImage, jobRole, address, department, gender, emailAddress, firstName, lastName, dateOn, userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //calling the getUserData method
        getUsersData(root);

        return root;
    }
    public void getUsersData(View root){
        //getting values from android shared preferences
        preferences = requireActivity().getSharedPreferences("User Details", Context.MODE_PRIVATE);

        userImage = preferences.getString("profileImage",null);
        jobRole = preferences.getString("jobRole",null);
        gender = preferences.getString("gender",null);
        department = preferences.getString("department",null);
        address = preferences.getString("address",null);
        emailAddress = preferences.getString("email",null);
        firstName = preferences.getString("firstName",null);
        lastName = preferences.getString("lastName",null);
//        dateOn = preferences.getString("createdOn",null);
//        userId = preferences.getString("user_id",null);

        //calling the set data method
        setDataProfileData(root,firstName,lastName,gender,emailAddress,department,jobRole,address,userImage);
    }

    //method to used to set users data
    public void setDataProfileData(View root, String firstName, String lastName, String gender, String email, String department,String jobRole, String address, String userImage){

        //downloading the users profile image from server
        CircleImageView profileImage = root.findViewById(R.id.profileImage);
        new ImageDownloader(profileImage).execute(URL + "/images/" + userImage);

        //setting the text of the profile firstName
        TextView profileFirstName = root.findViewById(R.id.pFirstName);
        String name = sentenceCaseWords(firstName) + " " + sentenceCaseWords(lastName);
        profileFirstName.setText(name);

        //setting text for users profile
        TextView profileGender = root.findViewById(R.id.pGender);
        profileGender.setText(sentenceCaseWords(gender));

        //setting text for Email Address
        TextView profileEmail = root.findViewById(R.id.pEmail);
        profileEmail.setText(sentenceCaseWords(email));

        //for Department
        TextView profileDept = root.findViewById(R.id.pDepartment);
        profileDept.setText(sentenceCaseWords(department));

        //for Job Role
        TextView profileJob = root.findViewById(R.id.pJobRole);
        profileJob.setText(sentenceCaseWords(jobRole));

        //for address
        TextView profileAddress = root.findViewById(R.id.pAddress);
        profileAddress.setText(sentenceCaseWords(address));
    }

    //method used to convert a word to sentence case
    private String sentenceCaseWords(String value){
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}