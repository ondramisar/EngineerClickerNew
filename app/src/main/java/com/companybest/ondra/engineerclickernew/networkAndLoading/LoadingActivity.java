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
            networkClient.componentsRunning.add(NetworkClient.DEFAULT_MACHINE);
            networkClient.componentsRunning.add(NetworkClient.DEFAULT_MATERIAL);
            networkClient.componentsRunning.add(NetworkClient.DEFAULT_WORKERS);
            networkClient.componentsRunning.add(NetworkClient.USER);
            networkClient.componentsRunning.add(NetworkClient.USER_WORKERS);
            networkClient.componentsRunning.add(NetworkClient.USERS_MACHINE);
            networkClient.componentsRunning.add(NetworkClient.USER_MATERIAL);

            networkClient.composersRunning.add(NetworkClient.Composers.MACHINE_WORKER);
            networkClient.composersRunning.add(NetworkClient.Composers.USER_MACHINE);
            networkClient.composersRunning.add(NetworkClient.Composers.USER_MATERIAL);
            networkClient.composersRunning.add(NetworkClient.Composers.USER_WORKER);



            networkClient.mCallBack.put(NetworkClient.COMPONENTS, networkClient::compose);

            networkClient.mCallBack.put(NetworkClient.COMPOSERS, () -> {
                Intent i = new Intent(getApplicationContext(), MainContainerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            });

            networkClient.mCallBack.put(NetworkClient.UPDATE, networkClient::parseAllComponents);

            networkClient.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
