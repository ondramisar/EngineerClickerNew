package com.companybest.ondra.engineerclickernew.networkAndLoading;


import android.support.annotation.NonNull;

import com.companybest.ondra.engineerclickernew.firebasePostModels.PostMachine;
import com.companybest.ondra.engineerclickernew.models.DefaultMachine;
import com.companybest.ondra.engineerclickernew.models.Machine;
import com.companybest.ondra.engineerclickernew.models.Material;
import com.companybest.ondra.engineerclickernew.models.User;
import com.companybest.ondra.engineerclickernew.utilities.CallBackFirebase;
import com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;

import static com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt.getUsersPath;

//TODO work on network
public class NetworkClient {

    HashMap<String, CallBackFirebase> mCallBack;

    public NetworkClient() {
        mCallBack = new HashMap<>();

    }

    public void addMachine(DefaultMachine machDef, final User user) {
        try (Realm it = Realm.getDefaultInstance()) {
            final Machine mach = new Machine();
            mach.setId(UUID.randomUUID().toString());
            mach.setName(machDef.getName());
            mach.setTimeBeffore(machDef.getTimeToReach());
            mach.setIdMaterialToGive(machDef.getIdMaterialToGive());
            it.beginTransaction();
            it.copyToRealmOrUpdate(mach);
            it.commitTransaction();

            final Machine mechTest = it.where(Machine.class).equalTo("id", mach.getId()).findFirst();
            if (mechTest != null) {
                it.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        user.addMachine(mechTest);
                    }
                });

            }

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(QueryFirebaseUtilitiesKt.getUsersMachinePath()).document(mach.getId())
                    .set(new PostMachine(mach.getName(), mach.getTimeToReach(), mach.getIdMaterialToGive()));

            db.collection(getUsersPath()).document(user.getIdUser())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    ArrayList<String> oldData = (ArrayList<String>) document.getData().get("idMachines");
                                    ArrayList<String> newData = new ArrayList<String>();
                                    newData.addAll(oldData);
                                    if (!mach.getId().isEmpty()) {
                                        newData.add(mach.getId());

                                        db.collection(QueryFirebaseUtilitiesKt.getUsersPath()).document(user.getIdUser())
                                                .update("idMachines", newData);
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public void parseDefaultMachines() {
        QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getDefaultMachinePath())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try (Realm mRealm = Realm.getDefaultInstance()) {
                                mRealm.beginTransaction();
                                for (DocumentSnapshot single : task.getResult()) {
                                    DefaultMachine m = new DefaultMachine();
                                    m.setId(single.getId());
                                    m.setName((java.lang.String) single.get("name"));
                                    m.setTimeToReach(single.getLong("timeToReach").intValue());
                                    m.setIdMaterialToGive(single.getString("idMaterialToGive"));


                                    mRealm.copyToRealmOrUpdate(m);
                                }
                                mRealm.commitTransaction();
                            }
                            mCallBack.get(QueryFirebaseUtilitiesKt.getDefaultParsingCalbacks()).onSucsses();
                        } else {
                        }
                    }
                });
    }

    public void parseMaterials() {
        QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getMaterialPath())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try (Realm mRealm = Realm.getDefaultInstance()) {
                                mRealm.beginTransaction();
                                for (DocumentSnapshot single : task.getResult()) {
                                    Material m = new Material();
                                    m.setId(single.getId());
                                    m.setValue(single.getLong("value").intValue());

                                    mRealm.copyToRealmOrUpdate(m);
                                }
                                mRealm.commitTransaction();
                            }
                            mCallBack.get(QueryFirebaseUtilitiesKt.getDefaultParsingCalbacks()).onSucsses();
                        } else {
                        }
                    }
                });
    }

    public void parseUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        QueryFirebaseUtilitiesKt.getBaseRef().collection(getUsersPath()).document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentSnapshot document = task.getResult();
                            try (Realm mRealm = Realm.getDefaultInstance()) {
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.delete(User.class);
                                        final User userRealm = new User();
                                        userRealm.setIdUser(document.getId());

                                        if (document.getString("username") != null)
                                            if (!document.getString("username").isEmpty())
                                                userRealm.setName(document.getString("username"));
                                        if (document.getString("email") != null)
                                            if (!document.getString("email").isEmpty())
                                                userRealm.setEmail(document.getString("email"));

                                        if (document.getData().get("idMachines") != null) {
                                            if (document.getData().get("idMachines") instanceof ArrayList) {
                                                ArrayList machiens = (ArrayList) document.getData().get("idMachines");
                                                for (Object i : machiens) {
                                                    if (i instanceof String) {
                                                        String id = (String) i;
                                                        Machine machine = realm.where(Machine.class).equalTo("id", id).findFirst();

                                                        if (machine != null && !userRealm.getMachines().contains(machine))
                                                            userRealm.addMachine(machine);

                                                    }
                                                }
                                            }
                                        }

                                        if (document.getData().get("idMaterials") != null) {
                                            if (document.getData().get("idMaterials") instanceof Map) {
                                                HashMap<String, Long> materials = (HashMap<String, Long>) document.getData().get("idMaterials");
                                                for (Map.Entry<String, Long> entry : materials.entrySet()) {
                                                    String id = entry.getKey();
                                                    int numberOf = entry.getValue().intValue();
                                                    Material material = realm.where(Material.class).equalTo("id", id).findFirst();
                                                    material.setNumberOf(numberOf);

                                                    if (material != null && !userRealm.getMaterials().contains(material))
                                                        userRealm.addMaterial(material);


                                                }
                                            }
                                        }

                                        realm.copyToRealmOrUpdate(userRealm);
                                    }
                                });
                            }
                            mCallBack.get(getUsersPath()).onSucsses();

                        } else {
                        }
                    }
                });

    }
}
