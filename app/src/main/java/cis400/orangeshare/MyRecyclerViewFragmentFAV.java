package cis400.orangeshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyRecyclerViewFragmentFAV extends MyRecyclerViewFragment {

    public MyRecyclerViewFragmentFAV() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-fav-posts")
                .child(getUid());
    }
}
