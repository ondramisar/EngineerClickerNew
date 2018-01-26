package com.companybest.ondra.engineerclickernew.networkAndLoading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.companybest.ondra.engineerclickernew.R;
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity;
import com.companybest.ondra.engineerclickernew.utilities.CallBackFirebase;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final ArrayList<String> keys = new ArrayList<>();
        final ArrayList<String> components = new ArrayList<>();

        final NetworkClient networkClient = new NetworkClient();
        networkClient.mCallBack.put(NetworkClient.COMPONENTS, new CallBackFirebase() {
            @Override
            public void onSucsses() {

            }

            @Override
            public void addOnSucsses(String key) {
                keys.add(key);

                if (keys.size() == 5) {
                    networkClient.compose();
                }
            }
        });

        networkClient.mCallBack.put(NetworkClient.COMPOSERS, new CallBackFirebase() {
            @Override
            public void onSucsses() {

            }

            @Override
            public void addOnSucsses(String key) {
                components.add(key);

                if (components.size() == 4) {
                    Intent i = new Intent(getApplicationContext(), MainContainerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        });
        networkClient.parseAllComponents();

    }
}
