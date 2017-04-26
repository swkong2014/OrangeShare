package cis400.orangeshare;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    private static final String TAG = "CreatePostActivity";
    DatabaseReference mDatabase;

    private EditText createTitle;
    private EditText createDate;
    private EditText createInfo;
    private FloatingActionButton createPost;
    private Button btnChangeAddress;
    private TextView addressContainer;

    private String title, date, info, address;
    private double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                                         // set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tTitle = (TextView) findViewById(R.id.toolbar_title);
        tTitle.setText("Create an Event");

        Bundle bun = getIntent().getExtras();
        lat = bun.getDouble("lat");
        lng = bun.getDouble("lng");
        address = bun.getString("address");

        mDatabase = FirebaseDatabase.getInstance().getReference();                                      // get reference

        createTitle = (EditText) findViewById(R.id.create_title);
        createDate = (EditText) findViewById(R.id.create_date);
        createInfo = (EditText) findViewById(R.id.create_info);

        createPost = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        addressContainer = (TextView) findViewById(R.id.address_container);
        addressContainer.setText(address);
        btnChangeAddress = (Button) findViewById(R.id.btn_change_address);
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePostActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void submitPost() {
        // get data from edit texts
        title = createTitle.getText().toString();
        date = createDate.getText().toString();
        info = createInfo.getText().toString();

        Bundle bun = getIntent().getExtras();
        lat = bun.getDouble("lat");
        lng = bun.getDouble("lng");
        address = bun.getString("address");

        // Checks if title is empty, if true then return error
        if (TextUtils.isEmpty(title)) {
            createTitle.setError("Required");
            return;
        }

        // Checks if date is empty, if true then return error
        // TODO: figure out how to force date format instead of string
        if (TextUtils.isEmpty(date)) {
            createDate.setError("Required");
            return;
        }

        // Checks if info is empty, if true then return error
        if (TextUtils.isEmpty(info)) {
            createInfo.setError("Required");
            return;
        }

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {   // Single event listener is used because we are only updating the one post, then returning to main activity
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);                                          // get the user info from database

                if (user == null) {                                                                     // check if user exists, it should
                    Log.d(TAG, "User " + uid + "is null");
                    showSnackbar("Cannot create post: User is null");
                } else {                                                                                  // create the post
                    String key = mDatabase.child("posts").push().getKey();                              // get key
                    HashMap favorites = new HashMap<String, Boolean>();
                    favorites.put("test", true);
                    Post post = new Post(uid, user.name, title, date, info, address, key, lat, lng, favorites);
                    Map<String, Object> postValues = post.toMap();                                      // turn to map to make it easier for transfer

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/posts/" + key, postValues);                                      // update values in "/posts/$postid"
                    childUpdates.put("/user-posts/" + uid + "/" + key, postValues);                     // update values in "/user-posts/$uid/$postid"


                    mDatabase.updateChildren(childUpdates);                                             // updates all children
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toException());
            }
        });
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_cancel:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(createTitle, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
