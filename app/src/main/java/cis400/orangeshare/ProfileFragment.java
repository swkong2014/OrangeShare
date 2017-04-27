package cis400.orangeshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    String name;
    String email;
    String birthday;
    String number;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        args.putString("name", user.getName());
        args.putString("email", user.getEmail());
        args.putString("birthday", user.getBirthday());
        args.putString("number", user.getNumber());

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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bun = getArguments();

        name = (String) bun.get("name");
        TextView profileName = (TextView) rootView.findViewById(R.id.profile_name);
        profileName.setText(name);

        email = (String) bun.get("email");
        TextView profileEmail = (TextView) rootView.findViewById(R.id.profile_email);
        profileEmail.setText(email);

        birthday = (String) bun.get("birthday");
        TextView profileBirthday = (TextView) rootView.findViewById(R.id.profile_birthday);
        profileBirthday.setText(birthday);

        number = (String) bun.get("number");
        TextView profileNumber = (TextView) rootView.findViewById(R.id.profile_number);
        profileNumber.setText(number);

        Button btnReview = (Button) rootView.findViewById(R.id.btn_view_review);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.launchReview();
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
        public void launchReview();
    }
}
