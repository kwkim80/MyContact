package ca.algonquin.kw2446.contactsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mProgressView;
    private View mainFormView;
    private TextView tvLoad;
    private SwipeRefreshLayout sRefresh;
   // private List<Contact> contacts;
    ListView lvContact;
    Button btnCreate;
    ContactsAdapter contactsAdapter;
    DataQueryBuilder queryBuilder;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
             contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //contacts=new ArrayList<>();
        lvContact=findViewById(R.id.lvContact);

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this, ContactInfo.class);
                intent.putExtra("index",position);
                startActivityForResult(intent,1);
            }
        });
        mainFormView = findViewById(R.id.main_form);
        mProgressView = findViewById(R.id.main_progress);
        tvLoad = findViewById(R.id.tvLoad);
        btnCreate=findViewById(R.id.btnCreate);
        sRefresh=findViewById(R.id.sRefresh);

        String whereClause = "userEmail = '" + ApplicationClass.user.getEmail() + "'";
        queryBuilder= DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("name");




        contactsAdapter = new ContactsAdapter(MainActivity.this, ApplicationClass.contacts);
        lvContact.setAdapter(contactsAdapter);
        retrieveData();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, NewContact.class);
                startActivityForResult(intent,1);
            }
        });

        sRefresh.setColorSchemeColors(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN);
        sRefresh.setOnRefreshListener(()->{
            retrieveData();
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mainFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void retrieveData(){

        sRefresh.setRefreshing(true);
        Backendless.Persistence.of(Contact.class).find(queryBuilder, new AsyncCallback<List<Contact>>() {
            @Override
            public void handleResponse(List<Contact> response) {

                if(ApplicationClass.contacts.size()>0){
                    ApplicationClass.contacts.clear();
                }
                if(response!=null){
                    ApplicationClass.contacts.addAll(response);
                }
                contactsAdapter.notifyDataSetChanged();
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sRefresh.setRefreshing(false);
    }

//    @Override
//    public void onItemClicked(int i) {
//       int temp=i;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(ApplicationClass.user!=null) {

        }

    }
}
