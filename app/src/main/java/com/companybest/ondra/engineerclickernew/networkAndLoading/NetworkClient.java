package com.companybest.ondra.engineerclickernew.networkAndLoading;


import android.util.Log;

import com.companybest.ondra.engineerclickernew.firebasePostModels.UserPost;
import com.companybest.ondra.engineerclickernew.models.DefaultMachine;
import com.companybest.ondra.engineerclickernew.models.DefaultMaterial;
import com.companybest.ondra.engineerclickernew.models.DefaultWorker;
import com.companybest.ondra.engineerclickernew.models.User;
import com.companybest.ondra.engineerclickernew.models.UserMachine;
import com.companybest.ondra.engineerclickernew.models.UserMaterial;
import com.companybest.ondra.engineerclickernew.models.UserWorker;
import com.companybest.ondra.engineerclickernew.utilities.CallBackFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static String DEFAULT_MACHINE = "DEFAULT_MACHINE";
    public static String USERS_MACHINE = "USERS_MACHINE";
    public static String MATERIAL = "MATERIAL";
    public static String USER = "USER";
    public static String WORKERS = "WORKERS";

    public static String COMPONENTS = "COMPONENTS";
    public static String COMPOSERS = "COMPOSERS";
    public static String UPDATE = "UPDATE";

    public HashMap<String, CallBackFirebase> mCallBack;


    private final String mHeaderContentType = "Content-Type";
    private final String mContentType = "application/json";

    private String mApiUrl;

    private Api mApi;

    public NetworkClient() throws Exception {
        mCallBack = new HashMap<>();
        initializeNetworkClient();
    }

    private void initializeNetworkClient() throws Exception {

        mApiUrl = "https://fluffy-fox-project.appspot.com/";

        if (initializeApiReference())
            throw new Exception("Api initialization error");
    }


    private boolean initializeApiReference() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mApiUrl);
        Retrofit retrofit = builder.build();
        mApi = retrofit.create(Api.class);
        return mApi == null;
    }

    public void updateMachineWork() {
    /*    try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser userFire = mAuth.getCurrentUser();
            if (userFire != null) {
                final User user = realm.where(User.class).equalTo("idUser", userFire.getUid()).findFirst();
                if (user != null) {
                    RealmList<UserMachine> machines = user.getUserMachines();
                    if (machines != null) {
                        for (final UserMachine machine : machines) {
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
                                                DefaultMaterial mat = realm.where(DefaultMaterial.class).equalTo("id", machine.getIdMaterialToGive()).findFirst();

                                                if (mat != null) {
                                                    WriteBatch batch = getBaseRef().batch();
                                                    DocumentReference sfRef = getUserDocumentReferenc()
                                                            .collection(getMaterialPath())
                                                            .document(mat.getId());

                                                    if (!user.getDefaultMaterials().contains(mat)) {
                                                        mat.setNumberOf(machine.getNumberOfMaterialsToGive() * machine.getWorker().getMaterialMultiplayer());
                                                        user.addMaterial(mat);

                                                        batch.update(sfRef, "numberOf", mat.getNumberOf());


                                                    } else {
                                                        DefaultMaterial material = user.getDefaultMaterials().where().equalTo("id", machine.getIdMaterialToGive()).findFirst();
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
        }*/
    }

    public void addMachine(DefaultMachine machDef, final User user) {
     /*   try (Realm it = Realm.getDefaultInstance()) {
            final UserMachine mach = new UserMachine();
            mach.setId(UUID.randomUUID().toString());
            mach.setName(machDef.getName());
            mach.setTimeToReach(machDef.getTimeToReach());
            mach.setIdMaterialToGive(machDef.getIdMaterialToGive());
            mach.setLvl(1);
            mach.setNumberOfMaterialsToGive(machDef.getNumberOfMaterialsToGive());
            it.beginTransaction();
            it.copyToRealmOrUpdate(mach);
            it.commitTransaction();

            final UserMachine mechTest = it.where(UserMachine.class).equalTo("id", mach.getId()).findFirst();
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
        }*/
    }

    public void addWorker(final DefaultWorker defaultWorker, final User user) {
   /*     getUserDocumentReferenc()
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
                                if (!defaultWorker.getId().isEmpty()) {
                                    newData.add(defaultWorker.getId());
                                    try (Realm realm = Realm.getDefaultInstance()) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                user.addWorker(defaultWorker);
                                                defaultWorker.setBought(true);
                                            }
                                        });
                                    }

                                    getUserDocumentReferenc()
                                            .update("idWorkers", newData);
                                }
                            }
                        }
                    }
                });*/
    }

    public void addWorkerToMachine(UserMachine userMachine, DefaultWorker defaultWorker) {
    /*    WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath())
                .document(userMachine.getId());
        batch.update(sfRef, "workerId", defaultWorker.getId());
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "Updated UserMachine");
            }
        });*/
    }

    public void removeWorkerToMachine(UserMachine userMachine) {
   /*     WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath())
                .document(userMachine.getId());
        batch.update(sfRef, "workerId", null);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "Updated UserMachine");
            }
        });*/
    }

    public void setTimeOutOfAppForUser(Long time) {
   /*     WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = getUserDocumentReferenc();
        batch.update(sfRef, "timeOutOfApp", time);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "updated time");
            }
        });*/
    }

    public void setLastPayment(Long lastPayment) {
     /*   WriteBatch batch = getBaseRef().batch();
        DocumentReference sfRef = getUserDocumentReferenc();
        batch.update(sfRef, "lastPayment", lastPayment);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("usern", "updated time");
            }
        });*/
    }

    public void createUser(FirebaseUser user) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(User.class));
            mApi.postUser(new UserPost(user.getUid(),"", user.getEmail(), 0, 0L, 0L, 0L)).enqueue(new ApiCallback<String>() {
                @Override
                public void onSuccess(Call<String> call, Response<String> response) {
                    Log.i("usern", response.body());
                }
            });
           /* Response<String> responce = handleNetworkOnMainThread(() ->
                            mApi.postUser(new UserPost(user.getUid(),"", user.getEmail(), 0, 0L, 0L, 0L)).execute());*/
        }
    }

    public <T> T handleNetworkOnMainThread(final Callable<T> callable) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> futureCart = executor.submit(callable);
        T object = null;
        try {
            object = futureCart.get();
        } catch (Exception e) {
        //    Development.debugLog(TAG, e.getMessage());
            e.printStackTrace();
        }
        return object;
    }

    public void parseAllComponents() {
        parseDefaultMachines();
        parseDefaultMaterials();
        parseDefaultWorkers();
        parseUser();
        parseUserWorkers();
        parseUserMaterials();
        parseUsersMachines();
    }

    public void compose() {
        composeMachineWorker();
        composeUserMachine();
        composeUserMaterials();
        composeUserWorkers();
    }

    public void update() {
        updateBackground();
    }

    private void updateBackground() {
     /*   try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser userFire = mAuth.getCurrentUser();
            final User user = realm.where(User.class).equalTo("idUser", userFire.getUid()).findFirst();
            if (user != null) {
                Long time = user.getLastTimeOutOfApp();
                Long timeNow = System.currentTimeMillis();
                Long difTime = (timeNow - time) / 1000;
                for (final UserMachine machine : user.getUserMachines()) {
                    if (machine.getWorker() != null) {
                        final Long machTime = difTime / machine.getTimeToReach();
                        final DefaultMaterial material = realm.where(DefaultMaterial.class).equalTo("id", machine.getIdMaterialToGive()).findFirst();
                        if (user.getDefaultMaterials().contains(material) && material != null) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    //       material.setNumberOf(material.getNumberOf() + machTime.intValue());
                                    user.getDefaultMaterials().where().equalTo("id", material.getId()).findFirst().setNumberOf(material.getNumberOf() + machTime.intValue());
                                }
                            });
                        }
                    }
                }

                Long lastPayment = user.getLastPayment();
                Date dateNow = new Date(timeNow);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateNow);
                Date dateLasPayment = new Date(lastPayment);
                Calendar calendarPayment = Calendar.getInstance();
                calendarPayment.setTime(dateLasPayment);
                int dayNow = calendar.get(Calendar.DAY_OF_YEAR);
                final int dayLastPayment = calendarPayment.get(Calendar.DAY_OF_YEAR);
                final int difference = dayNow - dayLastPayment;
                if (difference >= 1) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (DefaultWorker worker : user.getUserWorkers()) {
                                user.setCoins(user.getCoins() - (worker.getPayment() * difference));
                            }
                            user.setLastPayment(System.currentTimeMillis());
                            setLastPayment(user.getLastPayment());
                        }
                    });
                }
                mCallBack.get(UPDATE).addOnSucsses(UPDATE);
            }
        }*/
    }

    public void parseDefaultMachines() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMachine.class));
            Response<List<DefaultMachine>> machines = mApi.getMachines().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(machines.body()));
          //  mCallBack.get(COMPONENTS).addOnSucsses(DEFAULT_MACHINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUsersMachines() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(UserMachine.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserMachine>> machines = mApi.getUserMachines("userMachines/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(machines.body()));
          //  mCallBack.get(COMPONENTS).addOnSucsses(USERS_MACHINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseDefaultMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMaterial.class));
            Response<List<DefaultMaterial>> materials = mApi.getDefaultMaterials().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(materials.body()));
           // mCallBack.get(COMPONENTS).addOnSucsses(MATERIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUserMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMaterial.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserMaterial>> materials = mApi.getUserMaterials("userMaterials/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(materials.body()));
          //  mCallBack.get(COMPONENTS).addOnSucsses(MATERIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseDefaultWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultWorker.class));
            Response<List<DefaultWorker>> workers = mApi.getDefaultWorkers().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(workers.body()));
          //  mCallBack.get(COMPONENTS).addOnSucsses(WORKERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUserWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultWorker.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserWorker>> workers = mApi.getUserWorkers("userWorkers/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(workers.body()));
         //   mCallBack.get(COMPONENTS).addOnSucsses(WORKERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void parseUser() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultWorker.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<User> user = mApi.getUser("user/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(user.body()));
          //  mCallBack.get(COMPONENTS).addOnSucsses(USER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void composeMachineWorker() {
      /*  QueryFirebaseUtilitiesKt.getBaseRef().collection(QueryFirebaseUtilitiesKt.getUsersMachinePath())
                .whereEqualTo("userId", getFirebaseUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try (Realm mRealm = Realm.getDefaultInstance()) {
                                mRealm.beginTransaction();
                                for (DocumentSnapshot single : task.getResult()) {
                                    UserMachine machine = mRealm.where(UserMachine.class).equalTo("id", single.getId()).findFirst();
                                    if (single.getString("workerId") != null) {
                                        DefaultWorker worker = mRealm.where(DefaultWorker.class).equalTo("id", single.getString("workerId")).findFirst();
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
                });*/
    }

    public void composeUserMachine() {
     /*   final DocumentReference userRef = getUserDocumentReferenc();
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
                                                        UserMachine machine = realm.where(UserMachine.class).equalTo("id", id).findFirst();

                                                        if (machine != null && !userRealm.getUserMachines().contains(machine))
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
                });*/

    }

    public void composeUserMaterials() {
     /*   final DocumentReference userRef = getUserDocumentReferenc();
        userRef.collection(getMaterialPath())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        try (Realm realm = Realm.getDefaultInstance()) {
                            final User userRealm = realm.where(User.class).equalTo("idUser", userRef.getId()).findFirst();

                            for (final DocumentSnapshot single : documentSnapshots.getDocuments()) {
                                final DefaultMaterial material = realm.where(DefaultMaterial.class).equalTo("id", single.getId()).findFirst();

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        material.setNumberOf(single.getLong("numberOf").intValue());

                                        if (!userRealm.getDefaultMaterials().contains(material))
                                            userRealm.addMaterial(material);

                                        realm.copyToRealmOrUpdate(userRealm);
                                    }
                                });

                            }

                            mCallBack.get(COMPOSERS).addOnSucsses(MATERIAL);
                        }
                    }
                });*/

    }

    public void composeUserWorkers() {
      /*  final DocumentReference userRef = getUserDocumentReferenc();
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
                                                        DefaultWorker worker = realm.where(DefaultWorker.class).equalTo("id", id).findFirst();
                                                        if (userRealm != null && worker != null && !userRealm.getUserWorkers().contains(worker)) {
                                                            worker.setBought(true);
                                                            userRealm.addWorker(worker);
                                                        }
                                                    }
                                                }
                                            }

                                            if (userRealm != null && userRealm.getLastPayment() == null) {
                                                for (DefaultWorker worker : userRealm.getUserWorkers()) {
                                                    userRealm.setCoins(userRealm.getCoins() - worker.getPayment());
                                                }
                                                userRealm.setLastPayment(System.currentTimeMillis());
                                                setLastPayment(userRealm.getLastPayment());
                                            }
                                        }
                                    }
                                });

                                mCallBack.get(COMPOSERS).addOnSucsses(WORKERS);
                            }
                        }
                    }
                });*/

    }
}

