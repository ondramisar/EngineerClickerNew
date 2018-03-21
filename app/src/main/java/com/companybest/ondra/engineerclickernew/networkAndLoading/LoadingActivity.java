package com.companybest.ondra.engineerclickernew.networkAndLoading;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.companybest.ondra.engineerclickernew.R;
import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final ArrayList<String> keys = new ArrayList<>();
        final ArrayList<String> components = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            NetworkClient networkClient = new NetworkClient();

            networkClient.mCallBack.put(NetworkClient.COMPONENTS, key -> {
                keys.add(key);

                if (keys.size() == 7) {
                    networkClient.compose();
                }
            });

            networkClient.mCallBack.put(NetworkClient.COMPOSERS, key -> {
                components.add(key);

                if (components.size() == 4) {
                    networkClient.update();
                }
            });


            networkClient.mCallBack.put(NetworkClient.UPDATE, key -> {
                Intent i = new Intent(getApplicationContext(), MainContainerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            });
            networkClient.parseAllComponents();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
