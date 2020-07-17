package ca.algonquin.kw2446.contactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;

public class ContactInfo extends AppCompatActivity {
    private View mProgressView;
    private View mainFormView;
    private TextView tvLoad;

    TextView tvChar, tvName, tvPhone,tvEmail;
    ImageView ivCall, ivEmail, ivEdit, ivShare, ivDel;
    LinearLayout llEdit, llContact;
    EditText etName, etPhone, etEmail;
    Button btnUpdate,btnCancel;

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mainFormView = findViewById(R.id.main_form);
        mProgressView = findViewById(R.id.main_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvChar=findViewById(R.id.tvChar);
        tvName=findViewById(R.id.tvName);
        tvPhone=findViewById(R.id.tvPhone);
        tvEmail=findViewById(R.id.tvEmail);

        ivCall=findViewById(R.id.ivCall);
        ivEmail=findViewById(R.id.ivEmail);
        ivEdit=findViewById(R.id.ivEdit);
        ivShare=findViewById(R.id.ivShare);
        ivDel=findViewById(R.id.ivDel);

        llEdit=findViewById(R.id.llEdit);
        llContact=findViewById(R.id.llContact);

        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);

        btnUpdate=findViewById(R.id.btnUpdate);
        btnCancel=findViewById(R.id.btnCancel);



        final int index=getIntent().getIntExtra("index",0);

        contact=ApplicationClass.contacts.get(index);

        String name=contact.getName();

        tvChar.setText(name.charAt(0)+"");
        tvName.setText(name);
        tvPhone.setText(contact.getPhone());
        tvEmail.setText(contact.getEmail());

        etName.setText(name);
        etPhone.setText(contact.getPhone());
        etEmail.setText(contact.getEmail());

        llEdit.setVisibility(View.GONE);

        ivCall.setOnClickListener((v) -> {
            String uri="tel:"+contact.getPhone();
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });

        ivEmail.setOnClickListener((v)->{
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, contact.getEmail());
            startActivity(intent.createChooser(intent, "Send mail to"+name));

        });

        ivEdit.setOnClickListener((v)->{
            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            llContact.setLayoutParams(layoutParams);*/
            llContact.setVisibility(View.GONE);
            llEdit.setVisibility(View.VISIBLE);
        });

        ivShare.setOnClickListener((v)->{

        });

        ivDel.setOnClickListener((v)->{

            final AlertDialog.Builder dialog = new AlertDialog.Builder(ContactInfo.this);
            dialog.setMessage("Are you sure you want to delete the contact?");

            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    showProgress(true);
                    tvLoad.setText("Deleting contact...please wait...");

                    Backendless.Persistence.of(Contact.class).remove(ApplicationClass.contacts.get(index),
                            new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {

                                    ApplicationClass.contacts.remove(index);
                                    Toast.makeText(ContactInfo.this,
                                            "Contact successfully removed!", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    ContactInfo.this.finish();

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(ContactInfo.this, "Error: " + fault.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog.show();



        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nName=etName.getText().toString();
                String nPhone=etPhone.getText().toString().trim();
                String nEmail=etEmail.getText().toString().trim();

                if(nName.isEmpty()){
                    Toast.makeText(ContactInfo.this,"Please enter Name!",Toast.LENGTH_SHORT).show();
                }
                else if( nPhone.isEmpty()) {
                    Toast.makeText(ContactInfo.this, "Please enter Phone Number!", Toast.LENGTH_SHORT).show();
                }
                else{
                    showProgress(true);
                    tvLoad.setText("Updating Contact ...please wait...");
                    Contact contact=ApplicationClass.contacts.get(index);
                    contact.setName(nName);
                    contact.setPhone(nPhone);
                    contact.setEmail(nEmail);

                    showProgress(true);
                    tvLoad.setText("Updating contact...please wait...");

                    Backendless.Persistence.save(contact, new AsyncCallback<Contact>() {
                        @Override
                        public void handleResponse(Contact response) {

                            tvChar.setText(contact.getName().toUpperCase().charAt(0)+"");
                            tvName.setText(contact.getName());
                            Toast.makeText(ContactInfo.this,
                                    "Contact successfully updated!", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(ContactInfo.this, "Error: " + fault.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });


                }
            }
        });

        btnCancel.setOnClickListener((v)->{
           llEdit.setVisibility(View.GONE);
           llContact.setVisibility(View.VISIBLE);
        });

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
