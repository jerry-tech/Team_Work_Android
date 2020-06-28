package com.teamwork.teamwork;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserOptions extends AppCompatActivity implements PostImage.OnFragmentInteractionListener,PostArticle.OnFragmentInteractionListener {

    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_post, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //dialog for log out option
        myDialog = new Dialog(this);
        myDialog.setCancelable(false);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.itemProfile) {
            startActivity(new Intent(this,Profile.class));
        }
        if (id == R.id.itemLogOut) {
            //calling the show logout method
            showLogOutDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    //method to show dialog for log out and controls every component in the dialog /**N.b onclick listener was done in the layout **/
    public void showLogOutDialog() {
        myDialog.setContentView(R.layout.activity_logout);
        myDialog.show();
        //finding the buttons Id
        Button button = myDialog.findViewById(R.id.dialogCancel);
        button.setOnClickListener(V -> cancelLogOutDialog());
        //----------------------------------------------------------

    }
    //method for log out cancel dialog
    public void cancelLogOutDialog() {
        myDialog.dismiss();
    }
}
