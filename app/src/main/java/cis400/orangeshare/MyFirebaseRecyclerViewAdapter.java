package cis400.orangeshare;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian and Songwei on 4/19/2017.
 */

public class MyFirebaseRecyclerViewAdapter extends FirebaseRecyclerAdapter<Post, MyFirebaseRecyclerViewAdapter.PostViewHolder> {

    private static Context mContext;
    private static DatabaseReference mDatabase;
    private final String TAG = "MyFireRVAdapter";
    private static onItemClickListener mItemClickListener;


    public MyFirebaseRecyclerViewAdapter(Class<Post> modelClass, int modelLayout,
                                         Class<PostViewHolder> holder, Query ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void populateViewHolder(final PostViewHolder viewHolder, final Post post, int i) {
        final DatabaseReference postRef = getRef(i);

        viewHolder.pAuthor.setText(post.getAuthor());
        viewHolder.pDate.setText(post.getDate());
        viewHolder.pTitle.setText(post.getTitle());
        viewHolder.pInfo.setText(post.getInfo());
        viewHolder.pAddress.setText(post.getAddress());
        viewHolder.pId = post.getPostid();
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView pAuthor;
        public TextView pDate;
        public TextView pTitle;
        public TextView pInfo;
        public TextView pAddress;
        public String pId;

        public PostViewHolder(View v) {
            super(v);

            pAuthor = (TextView) v.findViewById(R.id.post_author);
            pDate = (TextView) v.findViewById(R.id.post_date);
            pTitle = (TextView) v.findViewById(R.id.post_title);
            pInfo = (TextView) v.findViewById(R.id.post_info);
            pAddress = (TextView) v.findViewById(R.id.post_address);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onListItemSelected(v, getAdapterPosition(), pId); // general as possible to make it available for reuse
                        Log.d("FB Adapter", "View Clicked");
                    }
                }
            });
        }
    }

    @Override
    public Post getItem(int pos) {
        return super.getItem(pos);
    }

    public void setOnItemClickListener(final onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        public void onListItemSelected(View v, int position, String postId);
    }
}
