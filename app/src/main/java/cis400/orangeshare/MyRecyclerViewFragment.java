package cis400.orangeshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;


// This file is abstract so that we do not have to create the same file over and over with
// different database queries. Each viewpager tab does the same thing
// (which is display a recyclerview) so instead of implementing the same thing 3 times,
// this abstract class is used and each other fragment implements its own database query
public abstract class MyRecyclerViewFragment extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyFirebaseRecyclerViewAdapter mFirebaseRecyclerViewAdapter;

     private OnFragmentInteractionListener mListener;

    public MyRecyclerViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);                    // sets recyclerview
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());                                        // sets layout style
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();                                      // sets query type: NEARBY, MINE, FAVORITES
        Query postsQuery = getQuery(mDatabase);

        mFirebaseRecyclerViewAdapter = new MyFirebaseRecyclerViewAdapter(Post.class, R.layout.card_view,    // initializes adapter and sets it with rv
                MyFirebaseRecyclerViewAdapter.PostViewHolder.class, postsQuery, getContext());
        mRecyclerView.setAdapter(mFirebaseRecyclerViewAdapter);

        mFirebaseRecyclerViewAdapter.setOnItemClickListener(new MyFirebaseRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onListItemSelected(View v, int position, String postId) {
                mListener.onListItemClick(postId);
                Log.d("RV Fragment", "Fragment received click");
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
        void onListItemClick(String postId);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);                                // abstract method
}
