package com.companybest.ondra.engineerclickernew.loginAndRegister;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.companybest.ondra.engineerclickernew.networkAndLoading.LoadingActivity;
import com.companybest.ondra.engineerclickernew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);


        final EditText email = v.findViewById(R.id.loginregister_login_edt_email);
        final TextInputLayout emailLayout = v.findViewById(R.id.login_email_address_layout);
        final EditText password = v.findViewById(R.id.loginregister_login_edt_password);
        final TextInputLayout passwordLayout = v.findViewById(R.id.login_password_layout);


        //     emailLayout.setHint(firebaseRemoteConfig.getString("t_cart_module_email_label"));
        //     passwordLayout.setHint(firebaseRemoteConfig.getString("t_password_label"));

        final Button register = v.findViewById(R.id.loginregister_login_button);
        register.setActivated(true);
        //  register.setText(firebaseRemoteConfig.getString("t_login_label"));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!LoginRegisterActivity.Companion.checkInternetConnection(view.getContext())) {
                    //   Toast.makeText(view.getContext(), firebaseRemoteConfig.getString("t_login_register_internet"), Toast.LENGTH_SHORT).show();
                    return;
                }

                String emailString = email.getText().toString();
                String passString = password.getText().toString();

                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(emailString, passString)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("usern", "createUserWithEmail:success");
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    Intent i = new Intent(getContext(), LoadingActivity.class);
                                    startActivity(i);
                                } else {
                                }

                            }
                        });
            }
        });

        return v;
    }


}