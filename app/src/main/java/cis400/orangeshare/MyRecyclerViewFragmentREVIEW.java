package cis400.orangeshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MyRecyclerViewFragmentREVIEW extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyFirebaseRecyclerViewAdapterREV mFirebaseRecyclerViewAdapter;

    String profileName;
    String profileUid;

    public MyRecyclerViewFragmentREVIEW() {
        // Required empty public constructor
    }

    public static MyRecyclerViewFragmentREVIEW newInstance(String profileName, String uid) {
        MyRecyclerViewFragmentREVIEW fragment = new MyRecyclerViewFragmentREVIEW();
        Bundle args = new Bundle();
        args.putString("name", profileName);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bun = getArguments();
            profileName = (String) bun.get("name");
            profileUid = (String) bun.get("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_recycler_view_fragment_review, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_recycler_view);
   //     mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQuery = mDatabase.child("user-reviews").child(profileUid);

        mFirebaseRecyclerViewAdapter = new MyFirebaseRecyclerViewAdapterREV(Review.class, R.layout.card_view_review,    // initializes adapter and sets it with rv
                MyFirebaseRecyclerViewAdapterREV.ReviewViewHolder.class, mQuery, getContext());
        mRecyclerView.setAdapter(mFirebaseRecyclerViewAdapter);

        return rootView;
    }


}
