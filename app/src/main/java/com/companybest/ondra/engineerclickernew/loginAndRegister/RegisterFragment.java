package com.companybest.ondra.engineerclickernew.loginAndRegister;


import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.companybest.ondra.engineerclickernew.networkAndLoading.LoadingActivity;
import com.companybest.ondra.engineerclickernew.R;
import com.companybest.ondra.engineerclickernew.firebasePostModels.UserPost;
import com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    private ProgressDialog mDialog;

    private String mConfigKey;


    private Button mLogIn;
    private Button mRegister;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordSecond;
    private TextView mConditions;
    private TextView mRegisterOr;

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
        View v = inflater.inflate(R.layout.register_fragment, container, false);

        initUI(v);
        initOnClicks();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUI(View v) {
        // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mFirebaseRemoteConfig.getString("t_cart_module_register_header"));

        mRegisterOr = v.findViewById(R.id.loginregister_register_or);
        //   mRegisterOr.setText(mFirebaseRemoteConfig.getString("t_register_or_email"));

        mConditions = v.findViewById(R.id.loginregister_textview_podminky);
        //    mConditions.setText(mFirebaseRemoteConfig.getString("t_header_conditions"));
        mConditions.setActivated(true);

        TextView or = v.findViewById(R.id.loginregister_dif);
        //    or.setText(mFirebaseRemoteConfig.getString("t_register_or"));

        mRegister = v.findViewById(R.id.registration_btn);
        //    mRegister.setText(mFirebaseRemoteConfig.getString("t_cart_module_register_header"));
        mRegister.setActivated(true);


        mEmail = v.findViewById(R.id.loginregister_edt_email);
        TextInputLayout mEmailLayout = v.findViewById(R.id.detail_billing_email_layout);
        mPassword = v.findViewById(R.id.loginregister_edt_password);
        TextInputLayout mPasswordLayout = v.findViewById(R.id.detail_password_layout);
        TextInputLayout mPasswordAgainLayout = v.findViewById(R.id.detail_password_again_layout);
        mPasswordSecond = v.findViewById(R.id.loginregister_edt_password_second);

        //    mEmailLayout.setHint(mFirebaseRemoteConfig.getString("t_cart_module_email_label"));
        //   mPasswordLayout.setHint(mFirebaseRemoteConfig.getString("t_password_label"));
        //   mPasswordAgainLayout.setHint(mFirebaseRemoteConfig.getString("t_password_confirm_label"));


        mLogIn = v.findViewById(R.id.loginregister_btn_log_in);

        //    mLogIn.setText(mFirebaseRemoteConfig.getString("t_login_label"));
    }

    private void initOnClicks() {

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginRegisterActivity.Companion.checkInternetConnection(view.getContext())) {
                    //   Toast.makeText(view.getContext(), firebaseRemoteConfig.getString("t_login_register_internet"), Toast.LENGTH_SHORT).show();
                    return;
                }

                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mEmail.getText().toString().length() > 0 && mPassword.getText().toString().equals(mPasswordSecond.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("usern", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        if (user != null) {

                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            db.collection(QueryFirebaseUtilitiesKt.getUsersPath()).document(user.getUid())
                                                    .set(new UserPost( user.getDisplayName(), user.getEmail()))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent i = new Intent(getContext(), LoadingActivity.class);
                                                            startActivity(i);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });

                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("usern", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginRegisterActivity) getActivity()).goToLogInFramgent();
            }
        });
    }

}