package com.companybest.ondra.engineerclickernew.networkAndLoading;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.companybest.ondra.engineerclickernew.mainContainer.MainContainerActivity;
import com.companybest.ondra.engineerclickernew.R;
import com.companybest.ondra.engineerclickernew.utilities.CallBackFirebase;
import com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt;

public class LoadingActivity extends AppCompatActivity {


    private int goToUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        goToUser = 0;
        final NetworkClient networkClient = new NetworkClient();
        networkClient.mCallBack.put(QueryFirebaseUtilitiesKt.getDefaultParsingCalbacks(), new CallBackFirebase() {
            @Override
            public void onSucsses() {
                goToUser += 1;
                if (goToUser == 2)
                    networkClient.parseUser();
            }
        });
        networkClient.mCallBack.put(QueryFirebaseUtilitiesKt.getUsersPath(), new CallBackFirebase() {
            @Override
            public void onSucsses() {
                Intent i = new Intent(getApplicationContext(), MainContainerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });

        networkClient.parseDefaultMachines();
        networkClient.parseMaterials();
    }
}
