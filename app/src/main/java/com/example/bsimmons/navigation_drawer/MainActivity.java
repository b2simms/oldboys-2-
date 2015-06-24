package com.example.bsimmons.navigation_drawer;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
        implements Fragment_NavigationDrawer.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private Fragment_NavigationDrawer mNavigationDrawerFragment;

    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean show_login = false;
    private boolean show_edit = false;
    private String name;
    private MenuItem login_button;
    private MenuItem edit_button;
    private String tag;


    private final String ADMIN_USER = "Admin";
    private final String GUEST_USER = "guest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        mNavigationDrawerFragment = (Fragment_NavigationDrawer)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        Intent i = getIntent();
        // Receiving the Data
        name = i.getStringExtra("Name");
        String pass = i.getStringExtra("Password");
        String team = i.getStringExtra("Team");
        Log.e("Second Screen", name + "." + pass + "." + team);

        TextView inputName = (TextView) findViewById(R.id.textView);
        if(name == null) {
            inputName.setText("Signed in: Guest");
            name = "guest";
        } else {
            inputName.setText("Signed in: " + name);
        }


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();



        Fragment objFrag = null;
        objFrag = new Fragment_Schedule();


        objFrag = this.getSupportFragmentManager().findFragmentByTag(tag);
        if (objFrag != null && !objFrag.isDetached()) {
            fragmentManager.executePendingTransactions();
        }


        switch (position) {
            case 0:
                tag = "Schedule";
                objFrag = new Fragment_Schedule();

                fragmentManager.beginTransaction()
                    .replace(R.id.container, objFrag, tag)
                    .commit();
                break;
            case 1:
                tag = "Standings";
                objFrag = new Fragment_Standings();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, objFrag, tag)
                        .commit();
                break;
            case 2:
                tag = "Scorers";
                objFrag = new Fragment_Scorers();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, objFrag, tag)
                        .commit();
                break;
            case 3:
                tag = "Messaging";
                objFrag = new Fragment_Messaging();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, objFrag, tag)
                        .commit();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                Intent loginScreen = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(loginScreen);
                break;

            default: break;
        }


    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            //checks whether to display LOGIN OR EDIT buttons
            login_button = menu.findItem(R.id.action_login);
            edit_button = menu.findItem(R.id.action_example);

            if(name.equalsIgnoreCase(ADMIN_USER)){
                edit_button.setVisible(true);
            }else {
                edit_button.setVisible(false);
            }
            if(name.equalsIgnoreCase(GUEST_USER)){
                login_button.setVisible(true);
            } else {
                login_button.setVisible(false);
            }

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), LoginScreen.class);
            finish();
            startActivity(nextScreen);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //landscape view
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //portrait view
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }
}
