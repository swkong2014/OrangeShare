package cis400.orangeshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyRecyclerViewFragmentREVIEW extends Fragment {

    public MyRecyclerViewFragmentREVIEW() {
        // Required empty public constructor
    }

    public static MyRecyclerViewFragmentREVIEW newInstance(String profileName) {
        MyRecyclerViewFragmentREVIEW fragment = new MyRecyclerViewFragmentREVIEW();
        Bundle args = new Bundle();
        args.putString("name", profileName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_recycler_view_fragment_review, container, false);

        return rootView;
    }


}
