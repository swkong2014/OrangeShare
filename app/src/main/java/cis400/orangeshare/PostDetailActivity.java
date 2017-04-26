package cis400.orangeshare;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostDetailActivity extends AppCompatActivity implements PostDetailFragment.OnFragmentInteractionListener {

    private final String TAG = "PostDetailActivity";
   // CollapsingToolbarLayout collapsingToolbar;

    Post post;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bun = getIntent().getExtras();
        String postId = (String) bun.get("postid");

      //  collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

//        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
//        backdrop.setImageResource(R.drawable.ic_fruit_holder);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.post_maps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, MapsViewActivity.class);
                intent.putExtra("lat", post.getLat());
                intent.putExtra("lng", post.getLng());
                startActivity(intent);
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();                    // loads the details of the Post into a fragment
        mDatabase.child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Post.class);

                toolbar.setTitle(post.getTitle());

                if(savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.post_detail_container, PostDetailFragment.newInstance(post))
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toException());
            }
        });
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
            case R.id.action_profile:
                intent = new Intent(PostDetailActivity.this, ProfileActivity.class);
                intent.putExtra("uid", post.getUid());
                startActivity(intent);
                return true;
            case R.id.action_map:
                intent = new Intent(PostDetailActivity.this, MapsViewActivity.class);
                intent.putExtra("lat", post.getLat());
                intent.putExtra("lng", post.getLng());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAuthorClicked() {
        Intent intent = new Intent(PostDetailActivity.this, ProfileActivity.class);
        intent.putExtra("uid", post.getUid());
        startActivity(intent);
    }
}
