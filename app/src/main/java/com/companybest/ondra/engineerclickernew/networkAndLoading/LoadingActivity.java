package com.companybest.ondra.engineerclickernew.networkAndLoading;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.companybest.ondra.engineerclickernew.R;

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
            final NetworkClient networkClient = new NetworkClient();
            networkClient.parseDefaultMachines();
            networkClient.parseWorkers();
            networkClient.parseMaterials();
            networkClient.parseUsersMachines();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        networkClient.mCallBack.put(NetworkClient.COMPONENTS, new CallBackFirebase() {
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
            public void addOnSucsses(String key) {
                components.add(key);

                if (components.size() == 4) {
                    networkClient.update();
                }
            }
        });


        networkClient.mCallBack.put(NetworkClient.UPDATE, new CallBackFirebase() {
            @Override
            public void addOnSucsses(String key) {
                Intent i = new Intent(getApplicationContext(), MainContainerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
        networkClient.parseAllComponents();*/

    }
}
