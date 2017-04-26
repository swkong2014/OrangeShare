package cis400.orangeshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Brian on 4/26/2017.
 */

public class MyFirebaseRecyclerViewAdapterREV extends FirebaseRecyclerAdapter<Review, MyFirebaseRecyclerViewAdapter.PostViewHolder> {

    private static Context mContext;
    private static DatabaseReference mDatabase;
    private final String TAG = "MyFireRVAdapter";

    public MyFirebaseRecyclerViewAdapterREV(Class<Review> modelClass, int modelLayout,
                                            Class<MyFirebaseRecyclerViewAdapter.PostViewHolder> holder, Query ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void populateViewHolder(final MyFirebaseRecyclerViewAdapter.PostViewHolder viewHolder, final Review review, int i) {

//        viewHolder.pAuthor.setText(post.getAuthor());
//        viewHolder.pDate.setText(post.getDate());

    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {

//        public TextView pAuthor;
//        public TextView pDate;


        public CommentViewHolder(View v) {
            super(v);

//            pAuthor = (TextView) v.findViewById(R.id.post_author);
//            pDate = (TextView) v.findViewById(R.id.post_date);


        }
    }

}