package com.companybest.ondra.engineerclickernew.networkAndLoading;


import android.util.Log;

import com.companybest.ondra.engineerclickernew.firebasePostModels.PostMachine;
import com.companybest.ondra.engineerclickernew.firebasePostModels.PostWorker;
import com.companybest.ondra.engineerclickernew.firebasePostModels.UserPost;
import com.companybest.ondra.engineerclickernew.models.DefaultMachine;
import com.companybest.ondra.engineerclickernew.models.DefaultMaterial;
import com.companybest.ondra.engineerclickernew.models.DefaultWorker;
import com.companybest.ondra.engineerclickernew.models.User;
import com.companybest.ondra.engineerclickernew.models.UserMachine;
import com.companybest.ondra.engineerclickernew.models.UserMaterial;
import com.companybest.ondra.engineerclickernew.models.UserWorker;
import com.companybest.ondra.engineerclickernew.utilities.CallBackFirebase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NetworkClient {

    public static String DEFAULT_MACHINE = "DEFAULT_MACHINE";
    public static String USERS_MACHINE = "USERS_MACHINE";
    public static String DEFAULT_MATERIAL = "DEFAULT_MATERIAL";
    public static String USER_MATERIAL = "USER_MATERIAL";
    public static String USER = "USER";
    public static String DEFAULT_WORKERS = "DEFAULT_WORKERS";
    public static String USER_WORKERS = "USER_WORKERS";

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
        //mApiUrl = "http://localhost:8080/";

        if (initializeApiReference())
            throw new Exception("Api initialization error");
    }


    private boolean initializeApiReference() {


        OkHttpClient.Builder okHttpNetworkClient = new OkHttpClient.Builder();
        okHttpNetworkClient.addInterceptor(chain -> {

            Request request = chain.request();
            Request.Builder requestWithAddedHeaders = request.newBuilder()
                    .addHeader(mHeaderContentType, mContentType);

            return chain.proceed(requestWithAddedHeaders.build());
        });

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create(objectMapper);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(mApiUrl)
                .client(okHttpNetworkClient.connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build())
                .addConverterFactory(jacksonConverterFactory);

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
        try (Realm it = Realm.getDefaultInstance()) {
            final UserMachine mach = new UserMachine();
            mach.setId(UUID.randomUUID().toString());
            mach.setName(machDef.getName());
            mach.setTimeToReach(machDef.getTimeToReach());
            mach.setIdMaterialToGive(machDef.getIdMaterialToGive());
            mach.setLvl(1);
            mach.setNumberOfMaterialsToGive(machDef.getNumberOfMaterialsToGive());
            mach.setIdUser(user.getIdUser());
            it.beginTransaction();
            it.copyToRealmOrUpdate(mach);
            it.commitTransaction();

            final UserMachine mechTest = it.where(UserMachine.class).equalTo("id", mach.getId()).findFirst();
            if (mechTest != null) {
                it.executeTransaction(realm -> user.addMachine(mechTest));

            }

            Response<String> machineResponce = mApi.createUserMachines(new PostMachine(mach.getId(), mach.getName(), mach.getTimeToReach(), mach.getNumberOfMaterialsToGive(),
                    mach.getIdMaterialToGive(), mach.getLvl(), mach.getWorkerId(), mach.getIdUser())).execute();
            Log.i("usern", machineResponce.body());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWorker(final DefaultWorker defaultWorker, final User user) {
        try (Realm it = Realm.getDefaultInstance()) {
            UserWorker userWorker = new UserWorker();
            userWorker.setId(UUID.randomUUID().toString());
            userWorker.setIdUser(user.getIdUser());
            userWorker.setLvl(1);
            userWorker.setMaterialMultiplayer(defaultWorker.getMaterialMultiplayer());
            userWorker.setName(defaultWorker.getName());
            userWorker.setOnMachine(false);
            userWorker.setTimeCutBy(defaultWorker.getTimeCutBy());
            userWorker.setPayment(defaultWorker.getPayment());

            it.beginTransaction();
            it.copyToRealmOrUpdate(userWorker);
            it.commitTransaction();

            final UserWorker workerTest = it.where(UserWorker.class).equalTo("id", userWorker.getId()).findFirst();
            if (workerTest != null) {
                it.executeTransaction(realm -> user.addWorker(workerTest));

            }

            Response<String> machineResponce = mApi.createUserWorker(new PostWorker(userWorker.getId(), userWorker.getName(), userWorker.getTimeCutBy(), userWorker.getMaterialMultiplayer(),
                    userWorker.getPayment(), userWorker.getLvl(), userWorker.isOnMachine(), userWorker.getIdUser())).execute();
            Log.i("usern", machineResponce.body());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWorkerToMachine(String idMachine, String idWorker) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> {
                UserWorker userWorker = realm1.where(UserWorker.class).equalTo("id", idWorker).findFirst();
                UserMachine userMachine = realm1.where(UserMachine.class).equalTo("id", idMachine).findFirst();
                if (userMachine != null && userWorker != null) {
                    userMachine.setWorkerId(idWorker);
                    userMachine.setWorker(userWorker);
                }
            });
            Response<String> response = mApi.addWorkerToMachine("addWorker/" + idMachine + "/" + idWorker).execute();
            Log.i("usern", response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorkerToMachine(String idMachine) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> {
                UserMachine userMachine = realm1.where(UserMachine.class).equalTo("id", idMachine).findFirst();
                if (userMachine != null) {
                    userMachine.setWorkerId("");
                    userMachine.setWorker(null);
                }
            });
            Response<String> response = mApi.removeWorkerFromMachine("removeWorker/" + idMachine).execute();
            Log.i("usern", response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser() {
        try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            if (userFir != null) {
                User user = realm.where(User.class).equalTo("idUser", userFir.getUid()).findFirst();
                if (user != null) {
                    Response<User> userResponce = mApi.updateUser("updateUser/" + userFir.getUid(),
                            new UserPost(user.getIdUser(), user.getName(), user.getEmail(), user.getCoins(),
                                    user.getLastUpdateMaterial(), user.getLastTimeOutOfApp(), user.getLastPayment())).execute();
                    if (userResponce != null && userResponce.body() != null)
                        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(userResponce.body()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(FirebaseUser user, Runnable runnable) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(User.class));
            mApi.postUser(new UserPost(user.getUid(), "", user.getEmail(), 0, 0L, 0L, 0L)).enqueue(new ApiCallback<String>() {
                @Override
                public void onSuccess(Call<String> call, Response<String> response) {
                    Log.i("usern", response.body());
                    runnable.run();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    super.onFailure(call, t);
                    Log.i("usern", t.getMessage());
                }
            });
        }
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
        try (Realm realm = Realm.getDefaultInstance()) {
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
                        final UserMaterial material = realm.where(UserMaterial.class).equalTo("id", machine.getIdMaterialToGive()).findFirst();
                        if (user.getMaterials().contains(material) && material != null) {
                            realm.executeTransaction(realm12 -> {
                                //       material.setNumberOf(material.getNumberOf() + machTime.intValue());
                                user.getMaterials().where().equalTo("id", material.getId()).findFirst().setNumberOf(material.getNumberOf() + machTime.intValue());
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
                    realm.executeTransaction(realm1 -> {
                        for (UserWorker worker : user.getUserWorkers()) {
                            user.setCoins(user.getCoins() - (worker.getPayment() * difference));
                        }
                        user.setLastTimeOutOfApp(System.currentTimeMillis());
                    });
                    updateUser();
                }
                mCallBack.get(UPDATE).addOnSucsses(UPDATE);
            }
        }
    }

    public void parseDefaultMachines() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMachine.class));
            Response<List<DefaultMachine>> machines = mApi.getMachines().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(machines.body()));
            mCallBack.get(COMPONENTS).addOnSucsses(DEFAULT_MACHINE);
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
            mCallBack.get(COMPONENTS).addOnSucsses(USERS_MACHINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseDefaultMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMaterial.class));
            Response<List<DefaultMaterial>> materials = mApi.getDefaultMaterials().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(materials.body()));
            mCallBack.get(COMPONENTS).addOnSucsses(DEFAULT_MATERIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUserMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(UserMaterial.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserMaterial>> materials = mApi.getUserMaterials("userMaterials/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(materials.body()));
            mCallBack.get(COMPONENTS).addOnSucsses(USER_MATERIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseDefaultWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultWorker.class));
            Response<List<DefaultWorker>> workers = mApi.getDefaultWorkers().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(workers.body()));
            mCallBack.get(COMPONENTS).addOnSucsses(DEFAULT_WORKERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUserWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(UserWorker.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserWorker>> workers = mApi.getUserWorkers("userWorkers/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(workers.body()));
            mCallBack.get(COMPONENTS).addOnSucsses(USER_WORKERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void parseUser() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(User.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<User> user = mApi.getUser("user/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(user.body()));
            mCallBack.get(COMPONENTS).addOnSucsses(USER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void composeMachineWorker() {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<UserMachine> userMachines = realm.where(UserMachine.class).findAll();

            for (UserMachine m : userMachines) {
                if (!m.getWorkerId().equals("")) {
                    UserWorker userWorker = realm.where(UserWorker.class).equalTo("id", m.getWorkerId()).findFirst();
                    if (userWorker != null) {
                        realm.executeTransaction(realm1 -> m.setWorker(userWorker));
                    }
                }
            }

            mCallBack.get(COMPOSERS).addOnSucsses(USERS_MACHINE);
        }
    }

    public void composeUserMachine() {
        try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            if (userFir != null) {
                User user = realm.where(User.class).equalTo("idUser", userFir.getUid()).findFirst();
                if (user != null) {
                    RealmResults<UserMachine> machines = realm.where(UserMachine.class).equalTo("idUser", user.getIdUser()).findAll();

                    if (machines != null && machines.size() > 0) {
                        realm.executeTransaction(realm1 -> user.getUserMachines().addAll(machines));
                    }
                }
            }
            mCallBack.get(COMPOSERS).addOnSucsses(DEFAULT_MACHINE);
        }

    }

    public void composeUserMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            if (userFir != null) {
                User user = realm.where(User.class).equalTo("idUser", userFir.getUid()).findFirst();
                if (user != null) {
                    RealmResults<UserMaterial> materials = realm.where(UserMaterial.class).equalTo("idUser", user.getIdUser()).findAll();

                    if (materials != null && materials.size() > 0) {
                        realm.executeTransaction(realm1 -> user.getMaterials().addAll(materials));

                    }
                }
            }
            mCallBack.get(COMPOSERS).addOnSucsses(DEFAULT_MATERIAL);
        }
    }

    public void composeUserWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            if (userFir != null) {
                User user = realm.where(User.class).equalTo("idUser", userFir.getUid()).findFirst();
                if (user != null) {
                    RealmResults<UserWorker> workers = realm.where(UserWorker.class).equalTo("idUser", user.getIdUser()).findAll();

                    if (workers != null && workers.size() > 0) {
                        realm.executeTransaction(realm1 -> user.getUserWorkers().addAll(workers));
                    }

                    if (user.getLastPayment() == null) {
                        for (UserWorker worker : user.getUserWorkers()) {
                            realm.executeTransaction(realm13 -> user.setCoins(user.getCoins() - worker.getPayment()));
                        }
                        realm.executeTransaction(realm12 -> {
                            user.setLastPayment(System.currentTimeMillis());
                        });
                        updateUser();
                    }
                }
            }
            mCallBack.get(COMPOSERS).addOnSucsses(DEFAULT_MATERIAL);
        }
    }
}

