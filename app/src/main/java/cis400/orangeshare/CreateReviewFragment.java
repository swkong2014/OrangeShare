package cis400.orangeshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class CreateReviewFragment extends Fragment {

    String profileUid;
    EditText createTitle;
    EditText createDetails;
    FloatingActionButton createReview;
    String currentName;
    Boolean eatAgain;
    int rating;

    String title, details;

    DatabaseReference mDatabase;
    private onFragmentInteractionListener mListener;


    public CreateReviewFragment() {
        // Required empty public constructor
    }

    public static CreateReviewFragment newInstance(String uid) {
        CreateReviewFragment fragment = new CreateReviewFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bun = getArguments();
            profileUid = (String) bun.get("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_review, container, false);

        createTitle = (EditText) rootView.findViewById(R.id.create_title);
        createDetails = (EditText) rootView.findViewById(R.id.create_info);
        createReview = (FloatingActionButton) rootView.findViewById(R.id.fab_submit_review);


        createTitle = (EditText) rootView.findViewById(R.id.create_title);
        createDetails = (EditText) rootView.findViewById(R.id.create_info);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        RadioGroup rgRating = (RadioGroup) rootView.findViewById(R.id.radio_group_rating);
        rgRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_one:
                        rating = 1;
                        break;
                    case R.id.radio_two:
                        rating = 2;
                        break;
                    case R.id.radio_three:
                        rating = 3;
                        break;
                    case R.id.radio_four:
                        rating = 4;
                        break;
                    case R.id.radio_five:
                        rating = 5;
                        break;
                }
            }
        });

        RadioGroup rgBool = (RadioGroup) rootView.findViewById(R.id.radio_group);
        rgBool.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                        eatAgain = true;
                        break;
                    case R.id.radio_no:
                        eatAgain = false;
                        break;
                }
            }
        });

        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });

        return rootView;
    }

    public void submitReview() {
        title = createTitle.getText().toString();
        details = createDetails.getText().toString();

        // Checks if title and details is empty, if true then return error
        if (TextUtils.isEmpty(title)) {
            createTitle.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(details)) {
            createDetails.setError("Required");
            return;
        }
        if (eatAgain == null) {
            showSnackbar("Must select YES or NO");
            return;
        }

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {                                                                     // check if user exists, it should
                    Log.d(TAG, "User " + uid + "is null");
                    showSnackbar("Cannot create review: User is null");
                } else {
                    // String key =  mDatabase.child("user-reviews").child(profileUid).push().getKey();

                    Review review = new Review(user.getName(), title, details, uid, eatAgain, rating);
                    mDatabase.child("user-reviews").child(profileUid).push().setValue(review);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // TODO: add fragment interface and launch either profile or switch fragments
        mListener.switchFragments();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateReviewFragment.onFragmentInteractionListener) {
            mListener = (CreateReviewFragment.onFragmentInteractionListener) context;
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

    public void showSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(createTitle, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public interface onFragmentInteractionListener {
        public void switchFragments();
    }
}
