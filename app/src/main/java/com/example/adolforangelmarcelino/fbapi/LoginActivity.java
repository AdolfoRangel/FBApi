package com.example.adolforangelmarcelino.fbapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.session.MediaSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.adolforangelmarcelino.fbapi.Adpaters.Albumsadapter;
import com.example.adolforangelmarcelino.fbapi.Models.AlbunsRespons;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity"; //logt
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_main);
        recyclerView = findViewById(R.id.recyclerView);

        if(alreadyLoggin()){
            getAlbuns(AccessToken.getCurrentAccessToken());
        }else {
            Toast.makeText(this, "Logeate", Toast.LENGTH_SHORT).show();
        }

        //printHashKey(this);


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(LoginActivity.this, "Exito Adolfo ahora estas en el callbackManager", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        loginButton.setReadPermissions("email", "public_profile", "user_photos");
        // If using in a fragment
        //loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Exito Adolfo desde el callbak registration", Toast.LENGTH_SHORT).show();
                final AccessToken token = loginResult.getAccessToken();
                getAlbuns(token);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
                // App code
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
        Metodo para crear hsachkey para poner en la pagina de facebook
         */
    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


    public void getAlbuns(AccessToken token) {
        GraphRequest request_loginrequest = GraphRequest.newGraphPathRequest(token, "me/albums", new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response != null) {
                    Gson gson = new Gson();
                    AlbunsRespons albunsRespons = gson.fromJson(response.getRawResponse(), AlbunsRespons.class);

                    Albumsadapter albumsadapter = new Albumsadapter(albunsRespons.getData(), LoginActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
                    recyclerView.setAdapter(albumsadapter);
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id,name,count");
        request_loginrequest.setParameters(params);
        request_loginrequest.executeAsync();
        Toast.makeText(LoginActivity.this, "estamos dentro de Registrar", Toast.LENGTH_SHORT).show();

    }

    public boolean alreadyLoggin() {
        return AccessToken.getCurrentAccessToken() != null;
    }
}
