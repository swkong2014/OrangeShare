package cis400.orangeshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

/**
 * Created by Brian on 4/26/2017.
 */

public class MyFirebaseRecyclerViewAdapterREV extends FirebaseRecyclerAdapter<Review, MyFirebaseRecyclerViewAdapterREV.ReviewViewHolder> {

    private static Context mContext;
    private static DatabaseReference mDatabase;
    private final String TAG = "MyFireRVAdapter";

    public MyFirebaseRecyclerViewAdapterREV(Class<Review> modelClass, int modelLayout,
                                            Class<MyFirebaseRecyclerViewAdapterREV.ReviewViewHolder> holder, Query ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void populateViewHolder(final MyFirebaseRecyclerViewAdapterREV.ReviewViewHolder viewHolder, final Review review, int i) {

        viewHolder.rTitle.setText(review.getTitle());
        viewHolder.rAuthor.setText(review.getUsername());
        viewHolder.rDetails.setText(review.getDescription());
        viewHolder.rRatingBar.setRating(review.getRating());
        if(review.getEatAgain()) {
            viewHolder.rAnswer.setText("YES");
        }
        else
            viewHolder.rAnswer.setText("NO");
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView rTitle;
        public TextView rAuthor;
        public TextView rDetails;
        public TextView rAnswer;
        public RatingBar rRatingBar;


        public ReviewViewHolder(View v) {
            super(v);

            rTitle = (TextView) v.findViewById(R.id.review_title);
            rAuthor = (TextView) v.findViewById(R.id.review_name);
            rDetails = (TextView) v.findViewById(R.id.review_description);
            rAnswer = (TextView) v.findViewById(R.id.review_answer);
            rRatingBar = (RatingBar) v.findViewById(R.id.review_stars);
        }
    }

}