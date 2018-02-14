package com.companybest.ondra.engineerclickernew.networkAndLoading;


import android.support.annotation.NonNull;
import android.util.Log;

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
import com.google.firebase.firestore.WriteBatch;

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
                                    if ((cur - machine.getTimeBeffore()) > (machine.getTimeToReach() - ((machine.getWorker().getTimeCutBy() / 100) * machine.getTimeToReach()))) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                machine.setTimeBeffore(System.currentTimeMillis() / 1000);
                                                Material mat = realm.where(Material.class).equalTo("id", machine.getIdMaterialToGive()).findFirst();

                                                if (mat != null) {
                                                    WriteBatch batch = getBaseRef().batch();
                                                    DocumentReference sfRef = getUserDocumentReferenc()
                                                            .collection(getMaterialPath())
                                                            .document(mat.getId());

                                                    if (!user.getMaterials().contains(mat)) {
                                                        mat.setNumberOf(machine.getNumberOfMaterialsToGive() * machine.getWorker().getMaterialMultiplayer());
                                                        user.addMaterial(mat);

                                                        batch.update(sfRef, "numberOf", mat.getNumberOf());


                                                    } else {
                                                        Material material = user.getMaterials().where().equalTo("id", machine.getIdMaterialToGive()).findFirst();
                                                        if (material != null) {
                                                            material.setNumberOf(material.getNumberOf() + (machine.getNumberOfMaterialsToGive() * machine.getWorker().getMaterialMultiplayer()));

                                                            batch.update(sfRef, "numberOf", material.getNumberOf());

                                                        }
                                                    }
                                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.i("usern", "Updated");
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
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
            mach.setNumberOfMaterialsToGive(machDef.getNumberOfMaterialsToGive());
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

            WriteBatch batch = getBaseRef().batch();
            DocumentReference sfRef = getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath()).document(mach.getId());
            batch.set(sfRef, new PostMachine(mach.getName(), mach.getTimeToReach(), mach.getIdMaterialToGive(), user.getIdUser(), mach.getLvl(), mach.getNumberOfMaterialsToGive()));
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("usern", "Updated");
                }
            });

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

    public void addWorker(final Worker worker, final User user) {
        getUserDocumentReferenc()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                ArrayList<String> oldData = (ArrayList<String>) document.getData().get("idWorkers");
                                ArrayList<String> newData = new ArrayList<>();
                                if (oldData != null)
                                    newData.addAll(oldData);
                                if (!worker.getId().isEmpty()) {
                                    newData.add(worker.getId());
                                    try (Realm realm = Realm.getDefaultInstance()) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                user.addWorker(worker);
                                                worker.setBought(true);
                                            }
                                        });
                                    }

                                    getUserDocumentReferenc()
                                            .update("idWorkers", newData);
                                }
                            }
                        }
                    }
                });
    }

    public void addWorkerToMachine(Machine machine, Worker worker) {
        WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath())
                .document(machine.getId());
        batch.update(sfRef, "workerId", worker.getId());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "Updated Machine");
            }
        });
    }

    public void removeWorkerToMachine(Machine machine) {
        WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath())
                .document(machine.getId());
        batch.update(sfRef, "workerId", null);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "Updated Machine");
            }
        });
    }

    public void setTimeOutOfAppForUser(Long time) {
        WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = getUserDocumentReferenc();
        batch.update(sfRef, "timeOutOfApp", time);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "updated time");
            }
        });
    }

    public void parseAllComponents() {
        parseDefaultMachines();
        parseMaterials();
        parseUser();
        parseWorkers();
        parseUsersMachines();
    }

    public void compose() {
        composeMachineWorker();
        composeUserMachine();
        composeUserMaterials();
        composeUserWorkers();
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
                                    m.setNumberOfMaterialsToGive(single.getLong("numberOfMaterialsToGive").intValue());
                                    m.setCost(single.getLong("cost").intValue());

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
                                    m.setNumberOfMaterialsToGive(single.getLong("numberOfMaterialsToGive").intValue());

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
                                    m.setName(single.getString("name"));

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
                                    worker.setMaterialMultiplayer(single.getLong("materialMultiplayer").intValue());
                                    worker.setTimeCutBy(single.getLong("timeCutBy").floatValue());
                                    worker.setPayment(single.getLong("payment").floatValue());

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
                                        if (document.getLong("timeOutOfApp") != null)
                                            userRealm.setLastTimeOutOfApp(document.getLong("timeOutOfApp"));

                                        realm.copyToRealmOrUpdate(userRealm);

                                        mCallBack.get(COMPONENTS).addOnSucsses(USER);
                                    }
                                });
                            }

                        }
                    }
                });

    }

    public void composeMachineWorker() {
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
                                    Machine machine = mRealm.where(Machine.class).equalTo("id", single.getId()).findFirst();
                                    if (single.getString("workerId") != null) {
                                        Worker worker = mRealm.where(Worker.class).equalTo("id", single.getString("workerId")).findFirst();
                                        if (worker != null && machine != null) {
                                            worker.setOnMachine(true);
                                            machine.setWorker(worker);
                                        }
                                    }
                                }
                                mRealm.commitTransaction();
                            }
                            mCallBack.get(COMPOSERS).addOnSucsses(USERS_MACHINE);
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

    public void composeUserWorkers() {
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
                                        if (document.getData().get("idWorkers") != null) {
                                            final User userRealm = realm.where(User.class).equalTo("idUser", userRef.getId()).findFirst();
                                            if (document.getData().get("idWorkers") instanceof ArrayList) {
                                                ArrayList workers = (ArrayList) document.getData().get("idWorkers");
                                                for (Object i : workers) {
                                                    if (i instanceof String) {
                                                        String id = (String) i;
                                                        Worker worker = realm.where(Worker.class).equalTo("id", id).findFirst();
                                                        if (userRealm != null && worker != null && !userRealm.getWorkers().contains(worker)) {
                                                            worker.setBought(true);
                                                            userRealm.addWorker(worker);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        mCallBack.get(COMPOSERS).addOnSucsses(WORKERS);
                                    }
                                });
                            }
                        }
                    }
                });

    }
}

