package ca.algonquin.kw2446.contactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import org.json.JSONArray;

public class Login extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    EditText etEmail, etPwd;
    Button btnLogin, btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.main_form);
        mProgressView = findViewById(R.id.main_progress);
        tvLoad = findViewById(R.id.tvLoad);


        etEmail=findViewById(R.id.etEmail);
        etPwd=findViewById(R.id.etPwd);
        btnLogin=findViewById(R.id.btnLogin);
        btnReg=findViewById(R.id.btnReg);


        IsValidLoginCheck();

        btnReg.setOnClickListener((v)->{
            startActivity(new Intent(Login.this, Register.class));

        });

        btnLogin.setOnClickListener((v)->{
            String email=etEmail.getText().toString().trim();
            String pwd=etPwd.getText().toString().trim();

            if(email.isEmpty()||pwd.isEmpty()){
                Toast.makeText(this,"Please enter all fields", Toast.LENGTH_SHORT).show();

            }
            else{

                showProgress(true);
                tvLoad.setText("Logging.. please wait..");

                Backendless.UserService.login(email, pwd, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {

                        ApplicationClass.user = response;
                        Toast.makeText(Login.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        Login.this.finish();

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);

                    }
                }, true);

                showProgress(false);

            }


        });

    }

     private void IsValidLoginCheck(){
       if(ApplicationClass.user!=null)
       {
           startActivity(new Intent(Login.this, MainActivity.class));
       }
    }
    /**
     * Shows the progress UI and hides the login form.
     */

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

    }
}
