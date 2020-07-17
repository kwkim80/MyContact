package ca.algonquin.kw2446.contactsapp;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application {
    public static final String APPLICATION_ID = "7856E223-081D-E430-FF2E-C20B76885F00";
    public static final String API_KEY = "4E998CFB-0FA8-462A-BD51-9257E363EA2E";
    public static final String SERVER_URL = "https://api.backendless.com";


    public static BackendlessUser user;
    public static List<Contact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
        contacts=new ArrayList<>();

    }
}
