package cis400.orangeshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class PostDetailFragment extends Fragment {

    private static Map<String, Object> postDetails;
    private OnFragmentInteractionListener mListener;

    String postTitle;
    String postAuthor;
    String postUid;
    String postDate;
    String postAddress;
    String postInfo;
    double lat;
    double lng;
    Boolean isFavorited;
    String postId;
    HashMap<String, Boolean> postFavorites;

    final String TAG = "PostDetailFragment";

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();

        args.putString("title", post.getTitle());
        args.putString("author", post.getAuthor());
        args.putString("uid", post.getUid());
        args.putString("date", post.getDate());
        args.putString("address", post.getAddress());
        args.putString("info", post.getInfo());
        args.putDouble("lat", post.getLat());
        args.putDouble("lng", post.getLng());
        args.putString("postid", post.getPostid());
        args.putSerializable("favorites", post.favorites);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        Bundle bun = getArguments();

        postAuthor = (String) bun.get("author");
        TextView detailAuthor = (TextView) rootView.findViewById(R.id.detail_author);
        detailAuthor.setText(postAuthor);
        detailAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAuthorClicked();
            }
        });

        postDate = (String) bun.get("date");
        TextView detailDate = (TextView) rootView.findViewById(R.id.detail_date);
        detailDate.setText(postDate);

        postAddress = (String) bun.get("address");
        TextView detailAddress = (TextView) rootView.findViewById(R.id.detail_address);
        detailAddress.setText(postAddress);

        postTitle = (String) bun.get("title");
        TextView detailTitle = (TextView) rootView.findViewById(R.id.detail_title);
        detailTitle.setText(postTitle);

        postInfo = (String) bun.get("info");
        TextView detailInfo = (TextView) rootView.findViewById(R.id.detail_info);
        detailInfo.setText(postInfo);

        // TODO: check if user already favorited event - set background and state if so - also set on check changes

        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postId = (String) bun.get("postid");
        postFavorites = (HashMap<String, Boolean>) bun.get("favorites");



        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {   // Single event listener is used because we are only updating the one post, then returning to main activity
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                user = dataSnapshot.getValue(User.class);                                          // get the user info from database
//
//                if (user == null) {                                                                     // check if user exists, it should
//                    Log.d(TAG, "User " + uid + "is null");
//
//                } else {                                                                                  // create the post
//                    String key = post.getPostid();                                                      // get key
//                    Map<String, Object> childUpdates = new HashMap<>();
//                    if (user.favorites.containsKey(key)) {
//                        user.favorites.remove(key);
//                        mDatabase.child("user-fav-posts").child(uid).child(key).removeValue();
//                    } else {
//                        Map<String, Object> postValues = post.toMap();                                      // turn to map to make it easier for transfer
//                        user.favorites.put(key, true);
//
//                        childUpdates.put("/user-fav-posts/" + uid + "/" + key, postValues);                     // update values in "/user-posts/$uid/$postid"
//                        // updates all children
//                    }
//                    childUpdates.put("/users/" + uid + "/favorites/", user.favorites);
//                    mDatabase.updateChildren(childUpdates);
//                }
//                //finish();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: " + databaseError.toException());
//            }
//        });

        final ToggleButton btnFav = (ToggleButton) rootView.findViewById(R.id.detail_btn_favorite);
       // btnFav.setText("Are you going to this event?");
        btnFav.setTextOn("I am going!");
        btnFav.setTextOff("Are you going to this event?");
        if (postFavorites.containsKey(currentUid)) {
            // TODO: put code to change button view to checked
            btnFav.setChecked(true);
            btnFav.setTextColor(getResources().getColor(R.color.blue_primary));
        }
        else {
            // TODO: put code to change button view to checked
            btnFav.setChecked(false);
            btnFav.setTextColor(getResources().getColor(R.color.orange_primary));
        }
        btnFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d(TAG, " Favorite Clicked");
                mDatabase.child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {   // Single event listener is used because we are only updating the one post, then returning to main activity
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);                                          // get the post info from database

                        if (post == null) {                                                                     // check if post exists, it should
                            Log.d(TAG, "Post " + postId + " is null");

                        } else {                                                                                  // create the post
                            String key = post.getPostid();                                                      // get key
                            Map<String, Object> postValues = post.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            if (post.favorites.containsKey(uid)) {
                                post.favorites.remove(uid);
                                mDatabase.child("user-fav-posts").child(uid).child(key).removeValue();              // removes values at "user-fav-posts/$uid/$postid
                                mDatabase.child("posts").child(key).child("favorites").child(uid).removeValue();    // removes values at "posts/$postid/favorites/$uid
                                btnFav.setChecked(false);
                                btnFav.setTextColor(getResources().getColor(R.color.orange_primary));
                            } else {
                                btnFav.setChecked(true);
                                post.favorites.put(uid, true);                                                      // adds uid to favorites of post
                                childUpdates.put("/user-fav-posts/" + uid + "/" + key, postValues);                     // update values in "/user-posts/$uid/$postid"
                                btnFav.setTextColor(getResources().getColor(R.color.blue_dark));
                            }
                            childUpdates.put("/posts/" + key + "/favorites/", post.favorites);
                            mDatabase.updateChildren(childUpdates);
                        }
                        //finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.toException());
                    }
                });
            }
        });

        return rootView;
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


    public interface OnFragmentInteractionListener {
        void onAuthorClicked();
    }
}
