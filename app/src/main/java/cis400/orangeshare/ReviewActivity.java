package cis400.orangeshare;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ReviewActivity extends AppCompatActivity implements CreateReviewFragment.onFragmentInteractionListener {

    String profileUid;
    String profileName;
    String currentUid;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle bun = getIntent().getExtras();
        profileName = (String) bun.get("name");
        profileUid = (String) bun.get("uid");

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        toolbar = (Toolbar) findViewById(R.id.toolbar);                                         // set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tTitle = (TextView) findViewById(R.id.toolbar_title);
        tTitle.setText("Review " + profileName);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.review_container, MyRecyclerViewFragmentREVIEW.newInstance(profileName, profileUid))                     // launch review rv
                .commit();
    }

    @Override
    public void switchFragments() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.review_container, MyRecyclerViewFragmentREVIEW.newInstance(profileName, profileUid))                     // launch review rv
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_write:
                if (currentUid.equals(profileUid)) {
                    showSnackbar("You cannot write a review about yourself!");
                }
                else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.review_container, CreateReviewFragment.newInstance(profileUid))                     // launch review rv
                            .commit();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSnackbar(String s){
        Snackbar snackbar = Snackbar.make(toolbar,s,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
