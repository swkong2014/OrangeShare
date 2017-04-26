package cis400.orangeshare; //change the package name to your project's package name

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity{

    EditText userNameET;
    EditText passwordET;

    String fullName = " ";
    String number = " ";
    String birthday = " ";

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;


    private static final int RC_SIGN_IN = 123;
    private static final String USER_CREATION_SUCCESS =  "Successfully created user";
    private static final String USER_CREATION_ERROR =  "User creation error";
    private static final String EMAIL_INVALID =  "email is invalid :";
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {                                        // listens if user is already logged in, launch main activity
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){

                        Log.d(TAG, "User is authenticated");
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(myIntent);

                }else{
                    Log.d(TAG, "User NOT authenticated");
                }

            }
        };

        userNameET = (EditText)findViewById(R.id.edit_text_email);
        passwordET = (EditText)findViewById(R.id.edit_text_password);

        Button btnSignIN = (Button) findViewById(R.id.button_sign_in );
        btnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(userNameET.getText().toString(), passwordET.getText().toString());
            }
        });

        Button btnSignUP = (Button) findViewById(R.id.button_sign_up);
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoEmail();
            }
        });

        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: " + connectionResult);
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button btnGoogle = (Button) findViewById(R.id.button_google);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoGoogle();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);  // attaches listener
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);   // removes listener
    }

    // Signs in a user
    private void signInUser(String email, String password) {
        if(password == null ||  !isEmailValid(email)) {                                                 // check if fields are null, if are return
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {                                                       // if successful, launch MainActivity
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(myIntent);                        }
                        else {                                                                          // if fail, log exception
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            showSnackbar("Sign in Failed");
                        }
                    }
                });
    }

    // create a new user in Firebase
    public void createUser(String email, String password) {
        if(password == null ||  !isEmailValid(email)) {                                                 // check if fields are null, if are return
            return;
        }
        mAuth.createUserWithEmailAndPassword(userNameET.getText().toString(),passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){                                                                // if successful, add user to DB and launch MainActivity
                    Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_SUCCESS, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
                    onAuthSuccessEmail(task.getResult().getUser());
                }else{
                    Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_ERROR, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }


    // Sign in user with Google
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);                                               // start Google Activity and handle result
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                        // handle result
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {                                                                // if resultCode is accurate, check success
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {                                                                   // if result is successful, send to firebase authentication and log
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                String displayName = account.getDisplayName();
                Log.d(TAG, displayName);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {                                                       // if firebase sign in is successful, add user to DB and launch MainActivity
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            FirebaseUser user = task.getResult().getUser();
                            fullName = acct.getDisplayName();
                            onAuthSuccessGoogle(user, acct.getDisplayName());

                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(myIntent);
                        }
                        else {                                                                          // else log error
                            Log.w(TAG, "signInWithCredential", task.getException());
                            showSnackbar("Authentication failed");
                        }
                    }
                });
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }

    // called when a user is created through EMAIL
    private void onAuthSuccessEmail(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
       // String name = getName();
        writeNewUser(user.getUid(), username, user.getEmail(), fullName);
    }

    // called when a user is created through GOOGLE
    private void onAuthSuccessGoogle(FirebaseUser user, String name) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail(), name);
    }

    // displays popup for user to put real name
    private void getInfoEmail(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please enter your..");
        //alert.setMessage("Message");

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set an EditText view to get user input
        final EditText nameET = new EditText(this);
        nameET.setHint("full name.. 'John Doe'");
        layout.addView(nameET);

        final EditText numberET = new EditText(this);
        numberET.setHint("phone number.. '(123)456-7890'");
        layout.addView(numberET);

        final EditText birthdayET = new EditText(this);
        birthdayET.setHint("date of birth.. 'Feb 23, 1990'");
        layout.addView(birthdayET);

        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (nameET.getText().toString().isEmpty()) {
                    showSnackbar("You must enter a name. Sign up Failed.");
                }
                else if (numberET.getText().toString().isEmpty()) {
                    showSnackbar("You must enter a name. Sign up Failed.");
                }
                else if (birthdayET.getText().toString().isEmpty()) {
                    showSnackbar("You must enter a name. Sign up Failed.");
                }
                else {
                    fullName = nameET.getText().toString();
                    number = numberET.getText().toString();
                    birthday = birthdayET.getText().toString();
                    createUser(userNameET.getText().toString(), passwordET.getText().toString());
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                showSnackbar("Sign up Failed.");
            }
        });

        alert.show();
    }

    private void getInfoGoogle(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please enter your..");
        //alert.setMessage("Message");

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set an EditText view to get user input
        final EditText numberET = new EditText(this);
        numberET.setHint("phone number.. '(123)456-7890'");
        layout.addView(numberET);

        final EditText birthdayET = new EditText(this);
        birthdayET.setHint("date of birth.. 'Feb 23, 1990'");
        layout.addView(birthdayET);

        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (numberET.getText().toString().isEmpty()) {
                    showSnackbar("You must enter a name. Sign up Failed.");
                }
                else if (birthdayET.getText().toString().isEmpty()) {
                    showSnackbar("You must enter a name. Sign up Failed.");
                }
                else {
                    number = numberET.getText().toString();
                    birthday = birthdayET.getText().toString();
                    googleSignIn();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                showSnackbar("Sign up Failed.");
            }
        });

        alert.show();
    }


    // seperates username from email in Email signup
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // writes a new user to "/user/$uid"
    private void writeNewUser(String userId, String username, String email, String name) {
        User user = new User(username, email, name, number, birthday);
        mDatabase.child("users").child(userId).setValue(user);
    }

    public void showSnackbar(String s){
        Snackbar snackbar = Snackbar.make(userNameET,s,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
