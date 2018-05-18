package com.app.csulb.rentmybookfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.app.csulb.rentmybookfinal.login.AuthenticationManager;
import com.app.csulb.rentmybookfinal.login.Constants;
import com.app.csulb.rentmybookfinal.login.MSALAuthenticationCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.Logger;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;
import com.microsoft.identity.client.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Class for login the user
 */
public class LoginActivity extends AppCompatActivity implements MSALAuthenticationCallback {

    private static final String TAG = "LoginActivity";
    private Button mConnectButton;
    private TextView mDescriptionTextView;
    private ProgressBar mConnectProgressBar;
    private boolean mEnablePiiLogging = false; // Enable logging of Personal Identifiable Information
    private User mUser;
    private Handler mHandler;
    FirebaseDatabase firebase;
    DatabaseReference database;
    boolean pat;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_login);
        setTitle(R.string.app_name);

        // set up our views
        mConnectButton = findViewById(R.id.connectButton);
        mConnectProgressBar = findViewById(R.id.connectProgressBar);
        mDescriptionTextView = findViewById(R.id.descriptionTextView);

        Application.getInstance().setApplicationActivity(this);
        showConnectingInProgressUI();
        connect();

        // add click listener
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConnectingInProgressUI();
                connect();
            }
        });
    }

    private void connect() {

        // The sample app is having the PII enable setting on the MainActivity. Ideally, app should decide to enable Pii or not,
        // if it's enabled, it should be  the setting when the application is onCreate.
        if (mEnablePiiLogging) {
            Logger.getInstance().setEnablePII(true);
        } else {
            Logger.getInstance().setEnablePII(false);
        }

        AuthenticationManager mgr = AuthenticationManager.getInstance();

        /*
        Attempt to get a user and acquireTokenSilent
        If this fails we do an interactive request
         */
        List<User> users;

        try {
            users = mgr.getPublicClient().getUsers();

            if (users != null && users.size() == 1) {
          /* We have 1 user */
                mUser = users.get(0);
                mgr.callAcquireTokenSilent(
                        mUser,
                        true,
                        this);
            } else {
          /* We have no user */

          /* Let's do an interactive request */
                mgr.callAcquireToken(
                        this,
                        this);
            }
        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());
            showConnectErrorUI(e.getMessage());


        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
            showConnectErrorUI(e.getMessage());

        } catch (IllegalStateException e) {
            Log.d(TAG, "MSAL Exception Generated: " + e.toString());
            showConnectErrorUI(e.getMessage());

        } catch (Exception e) {
            showConnectErrorUI();
        }
    }

    /**
     * Handles redirect response from https://login.microsoftonline.com/common and
     * notifies the MSAL library that the user has completed the authentication
     * dialog
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AuthenticationManager
                .getInstance()
                .getPublicClient() != null) {
            AuthenticationManager
                    .getInstance()
                    .getPublicClient()
                    .handleInteractiveRequestRedirect(requestCode, resultCode, data);
        }
    }

    private void showConnectingInProgressUI() {
        mConnectProgressBar.setVisibility(View.VISIBLE);
    }

    private void showConnectErrorUI() {
        showConnectErrorUI(R.string.general_connection_error);
    }

    private void showConnectErrorUI(String errorMessage) {
        mConnectButton.setVisibility(View.VISIBLE);
        mConnectProgressBar.setVisibility(View.GONE);
        mDescriptionTextView.setText(errorMessage);
        mDescriptionTextView.setVisibility(View.VISIBLE);
        showMessage(errorMessage);
    }

    private void showConnectErrorUI(int stringResource) {
        showConnectErrorUI(getString(stringResource));
    }

    // buttered toast
    private void showMessage(final String msg) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMessage(final int stringResource) {
        showMessage(getString(stringResource));
    }

    private Handler getHandler() {
        if (mHandler == null) {
            return new Handler(LoginActivity.this.getMainLooper());
        }

        return mHandler;
    }

    @Override
    public void onSuccess(AuthenticationResult authenticationResult) {
        mConnectButton.setVisibility(View.INVISIBLE);
        mConnectProgressBar.setVisibility(View.INVISIBLE);
        mUser = authenticationResult.getUser();

        try {
            // get the user info from the id token
            Application.User.setDisplayName(authenticationResult.getUser().getName());
            Application.User.setAccessToken(authenticationResult.getAccessToken());
            Application.User.setEmailAddress(authenticationResult.getUser().getDisplayableId());
            RequestQueue queue = Volley.newRequestQueue(this);
            ImageRequest request = new ImageRequest(Constants.MSGRAPH_URL + "me/photo/$value",
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Application.User.setProfilePicture(response);
                        }
                    }, 360, 360, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Profile picture download error");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + Application.User.getAccessToken());
                    return headers;
                }

            };
            queue.add(request);

        } catch (NullPointerException npe) {
            Log.e(TAG, npe.getMessage());

        }

        //Making a user in Firebase
        firebase = FirebaseDatabase.getInstance();
        database = firebase.getReference("Users");
        pat = false;
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String tmp = dataSnapshot.getValue(String.class);
                Map<String,String> temp = (HashMap<String, String>)dataSnapshot.getValue();

                if(temp.containsKey(Application.User.getDisplayName())){
                    pat = true;
                }
                getAnswer(pat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
        mConnectButton.setVisibility(View.VISIBLE);
    }

    public void getAnswer(boolean ans){
        if(!ans){
            FirebaseDatabase firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference name = firebaseDatabase.getReference("Users");
            name.child(Application.User.getDisplayName());

            DatabaseReference email = name.child(Application.User.getDisplayName()).child("Email");
            email.setValue(Application.User.getEmailAddress());

            DatabaseReference point = name.child(Application.User.getDisplayName()).child("Point");
            point.setValue("0");

            DatabaseReference phone = name.child(Application.User.getDisplayName()).child("Phone");
            phone.setValue("None");
        }
    }

    @Override
    public void onError(MsalException exception) {
        // Check the exception type.
        if (exception instanceof MsalClientException) {
            // This means errors happened in the sdk itself, could be network, Json parse, etc. Check MsalError.java
            // for detailed list of the errors.
            showMessage(exception.getMessage());
            showConnectErrorUI(exception.getMessage());
        } else if (exception instanceof MsalServiceException) {
            // This means something is wrong when the sdk is communication to the service, mostly likely it's the client
            // configuration.
            showMessage(exception.getMessage());
            showConnectErrorUI(exception.getMessage());
        } else if (exception instanceof MsalUiRequiredException) {
            // This explicitly indicates that developer needs to prompt the user, it could be refresh token is expired, revoked
            // or user changes the password; or it could be that no token was found in the token cache.
            AuthenticationManager mgr = AuthenticationManager.getInstance();


            mgr.callAcquireToken(LoginActivity.this, this);
        }

    }

    @Override
    public void onError(Exception exception) {
        showConnectErrorUI(exception.getMessage());
    }

    @Override
    public void onCancel() {
        showConnectErrorUI(R.string.user_cancelled);
//        showConnectErrorUI("User cancelled the flow.");
    }

}
