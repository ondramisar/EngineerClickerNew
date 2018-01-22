package com.companybest.ondra.engineerclickernew.networkAndLoading;


import android.support.annotation.NonNull;

import com.companybest.ondra.engineerclickernew.firebasePostModels.PostMachine;
import com.companybest.ondra.engineerclickernew.models.DefaultMachine;
import com.companybest.ondra.engineerclickernew.models.Machine;
import com.companybest.ondra.engineerclickernew.models.Material;
import com.companybest.ondra.engineerclickernew.models.User;
import com.companybest.ondra.engineerclickernew.models.Worker;
import com.companybest.ondra.engineerclickernew.utilities.CallBackFirebase;
import com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;

import static com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt.getBaseRef;
import static com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt.getFirebaseUser;
import static com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt.getMaterialPath;
import static com.companybest.ondra.engineerclickernew.utilities.QueryFirebaseUtilitiesKt.getUserDocumentReferenc;

public class NetworkClient {

    public static String DEFAULT_MACHINE = "DEFAULT_MACHINE";
    public static String USERS_MACHINE = "USERS_MACHINE";
    public static String MATERIAL = "MATERIAL";
    public static String USER = "USER";
    public static String WORKERS = "WORKERS";

    public static String COMPONENTS = "COMPONENTS";
    public static String COMPOSERS = "COMPOSERS";

    public HashMap<String, CallBackFirebase> mCallBack;

    public NetworkClient() {
        mCallBack = new HashMap<>();

    }

    public void updateMachineWork() {
        try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser userFire = mAuth.getCurrentUser();
            if (userFire != null) {
                final User user = realm.where(User.class).equalTo("idUser", userFire.getUid()).findFirst();
                if (user != null) {
                    RealmList<Machine> machines = user.getMachines();
                    if (machines != null) {
                        for (final Machine machine : machines) {
                            if (machine.getWorker() != null) {
                                if (machine.getTimeBeffore() == 0L) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            machine.setTimeBeffore(System.currentTimeMillis() / 1000);
                                        }
                                    });
                                } else {
                                    Long cur = System.currentTimeMillis() / 1000;
                                    if ((cur - machine.getTimeBeffore()) > machine.getTimeToReach()) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                machine.setTimeBeffore(System.currentTimeMillis() / 1000);
                                                Material mat = realm.where(Material.class).equalTo("id", machine.getIdMaterialToGive()).findFirst();
                                                if (!user.getMaterials().contains(mat)) {
                                                    mat.setNumberOf(1);
                                                    user.addMaterial(mat);
                                                    getUserDocumentReferenc()
                                                            .collection(getMaterialPath())
                                                            .document(mat.getId())
                                                            .update("numberOf", mat.getNumberOf());
                                                } else {
                                                    for (Material material : user.getMaterials()) {
                                                        if (material.getId().equals(machine.getIdMaterialToGive())) {
                                                            material.setNumberOf(material.getNumberOf() + 1);
                                                            getUserDocumentReferenc()
                                                                    .collection(getMaterialPath())
                                                                    .document(material.getId())
                                                                    .update("numberOf", material.getNumberOf());
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                    if (user.getLastUpdateMaterial() == null || user.getLastUpdateMaterial() == 0L) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                user.setLastUpdateMaterial(System.currentTimeMillis() / 1000);
                            }
                        });
                    }

                 /*   Long cur = System.currentTimeMillis() / 1000;
                    if ((cur - user.getLastUpdateMaterial()) > 45) {
                        for (Material m : user.getMaterials()) {
                            getUserDocumentReferenc()
                                    .collection(getMaterialPath())
                                    .document(m.getId())
                                    .update("numberOf", m.getNumberOf());
                        }
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                user.setLastUpdateMaterial(System.currentTimeMillis() / 1000);
                            }
                        });*/
                     /*   getUserDocumentReferenc()
                                .collection(getMaterialPath())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot documentSnapshots) {
                                        try (Realm realm = Realm.getDefaultInstance()) {

                                            for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                                                if (document != null) {

                                                    FirebaseUser userFire = getFirebaseUser();
                                                    if (userFire != null) {

                                                        final User user = realm.where(User.class).equalTo("idUser", userFire.getUid()).findFirst();
                                                        if (user != null) {
                                                            realm.executeTransaction(new Realm.Transaction() {
                                                                @Override
                                                                public void execute(Realm realm) {
                                                                    user.setLastUpdateMaterial(System.currentTimeMillis() / 1000);
                                                                }
                                                            });

                                                            Material material = user.getMaterials().where().equalTo("id", document.getId()).findFirst();
                                                            getUserDocumentReferenc()
                                                                    .collection(getMaterialPath())
                                                                    .document(material.getId())
                                                                    .update("numberOf", material.getNumberOf());
                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }
                                });*/

                }
            }
        }
    }

    public void addMachine(DefaultMachine machDef, final User user) {
        try (Realm it = Realm.getDefaultInstance()) {
            final Machine mach = new Machine();
            mach.setId(UUID.randomUUID().toString());
            mach.setName(machDef.getName());
            mach.setTimeToReach(machDef.getTimeToReach());
            mach.setIdMaterialToGive(machDef.getIdMaterialToGive());
            mach.setLvl(1);
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

            getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath()).document(mach.getId())
                    .set(new PostMachine(mach.getName(), mach.getTimeToReach(), mach.getIdMaterialToGive(), user.getIdUser(), mach.getLvl()));

            getUserDocumentReferenc()
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    ArrayList<String> oldData = (ArrayList<String>) document.getData().get("idMachines");
                                    ArrayList<String> newData = new ArrayList<String>();
                                    if (oldData != null)
                                        newData.addAll(oldData);
                                    if (!mach.getId().isEmpty()) {
                                        newData.add(mach.getId());

                                        getUserDocumentReferenc()
                                                .update("idMachines", newData);
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public void parseAllComponents() {
        parseDefaultMachines();
        parseMaterials();
        parseUser();
        parseWorkers();
        parseUsersMachines();
    }

    public void compose() {
        composeUserMachine();
        composeUserMaterials();
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
                            mCallBack.get(COMPONENTS).addOnSucsses(DEFAULT_MACHINE);
                        }
                    }
                });
    }

    public void parseUsersMachines() {
        QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath())
                .whereEqualTo("userId", getFirebaseUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try (Realm mRealm = Realm.getDefaultInstance()) {
                                mRealm.beginTransaction();
                                for (DocumentSnapshot single : task.getResult()) {
                                    Machine m = new Machine();
                                    m.setId(single.getId());
                                    m.setName((java.lang.String) single.get("name"));
                                    m.setTimeToReach(single.getLong("timeToReach").intValue());
                                    m.setIdMaterialToGive(single.getString("idMaterialToGive"));

                                    mRealm.copyToRealmOrUpdate(m);
                                }
                                mRealm.commitTransaction();
                            }
                            mCallBack.get(COMPONENTS).addOnSucsses(USERS_MACHINE);
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
                            mCallBack.get(COMPONENTS).addOnSucsses(MATERIAL);
                        }
                    }
                });
    }

    public void parseWorkers() {
        QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getWorkersPath())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try (Realm mRealm = Realm.getDefaultInstance()) {
                                mRealm.beginTransaction();
                                for (DocumentSnapshot single : task.getResult()) {
                                    Worker worker = new Worker();
                                    worker.setId(single.getId());
                                    worker.setName(single.getString("name"));

                                    mRealm.copyToRealmOrUpdate(worker);
                                }
                                mRealm.commitTransaction();
                            }
                            mCallBack.get(COMPONENTS).addOnSucsses(WORKERS);
                        }
                    }
                });
    }

    public void parseUser() {
        final DocumentReference userRef = getUserDocumentReferenc();
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try (Realm mRealm = Realm.getDefaultInstance()) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot document = task.getResult();

                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(final Realm realm) {
                                        realm.delete(User.class);
                                        final User userRealm = new User();
                                        userRealm.setIdUser(document.getId());

                                        if (document.getString("username") != null)
                                            if (!document.getString("username").isEmpty())
                                                userRealm.setName(document.getString("username"));
                                        if (document.getString("email") != null)
                                            if (!document.getString("email").isEmpty())
                                                userRealm.setEmail(document.getString("email"));

                                        realm.copyToRealmOrUpdate(userRealm);

                                        mCallBack.get(COMPONENTS).addOnSucsses(USER);
                                    }
                                });
                            }

                        }
                    }
                });

    }

    public void composeUserMachine() {
        final DocumentReference userRef = getUserDocumentReferenc();
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try (Realm realm = Realm.getDefaultInstance()) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot document = task.getResult();

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(final Realm realm) {
                                        if (document.getData().get("idMachines") != null) {
                                            final User userRealm = realm.where(User.class).equalTo("idUser", userRef.getId()).findFirst();
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

                                        mCallBack.get(COMPOSERS).addOnSucsses(DEFAULT_MACHINE);
                                    }
                                });
                            }
                        }
                    }
                });

    }

    public void composeUserMaterials() {
        final DocumentReference userRef = getUserDocumentReferenc();
        userRef.collection(getMaterialPath())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        try (Realm realm = Realm.getDefaultInstance()) {
                            final User userRealm = realm.where(User.class).equalTo("idUser", userRef.getId()).findFirst();

                            for (final DocumentSnapshot single : documentSnapshots.getDocuments()) {
                                final Material material = realm.where(Material.class).equalTo("id", single.getId()).findFirst();

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        material.setNumberOf(single.getLong("numberOf").intValue());

                                        if (!userRealm.getMaterials().contains(material))
                                            userRealm.addMaterial(material);

                                        realm.copyToRealmOrUpdate(userRealm);
                                    }
                                });

                            }

                            mCallBack.get(COMPOSERS).addOnSucsses(MATERIAL);
                        }
                    }
                });

    }


}

