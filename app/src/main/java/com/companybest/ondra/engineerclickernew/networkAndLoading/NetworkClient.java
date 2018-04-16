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
import java.util.ArrayList;
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

    enum Composers {
        MACHINE_WORKER,
        USER_MACHINE,
        USER_MATERIAL,
        USER_WORKER
    }

    public static String COMPONENTS = "COMPONENTS";
    public static String COMPOSERS = "COMPOSERS";
    public static String UPDATE = "UPDATE";

    public HashMap<String, CallBackFirebase> mCallBack;

    public List<String> componentsRunning;
    public List<Composers> composersRunning;


    public List<String> componentsSuccessful;
    public List<Composers> composersSuccessful;

    private final String mHeaderContentType = "Content-Type";
    private final String mContentType = "application/json";

    private String mApiUrl;

    private Api mApi;

    private HashMap<String, Runnable> components;
    private HashMap<Composers, Runnable> composers;

    public NetworkClient() throws Exception {
        mCallBack = new HashMap<>();
        componentsRunning = new ArrayList<>();
        composersRunning = new ArrayList<>();

        componentsSuccessful = new ArrayList<>();
        composersSuccessful = new ArrayList<>();

        initializeNetworkClient();

        components = new HashMap<>();
        components.put(DEFAULT_MACHINE, this::parseDefaultMachines);
        components.put(DEFAULT_MATERIAL, this::parseDefaultMaterials);
        components.put(DEFAULT_WORKERS, this::parseDefaultWorkers);
        components.put(USERS_MACHINE, this::parseUsersMachines);
        components.put(USER_WORKERS, this::parseUserWorkers);
        components.put(USER_MATERIAL, this::parseUserMaterials);
        components.put(USER, this::parseUser);

        composers = new HashMap<>();
        composers.put(Composers.MACHINE_WORKER, this::composeMachineWorker);
        composers.put(Composers.USER_MACHINE, this::composeUserMachine);
        composers.put(Composers.USER_MATERIAL, this::composeUserMaterials);
        composers.put(Composers.USER_WORKER, this::composeUserWorkers);
    }

    private void initializeNetworkClient() throws Exception {
        mApiUrl = "https://fluffy-fox-project.appspot.com/";

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
                                    realm.executeTransaction(realm1 -> machine.setTimeBeffore(System.currentTimeMillis() / 1000));
                                } else {
                                    Long cur = System.currentTimeMillis() / 1000;
                                    if ((cur - machine.getTimeBeffore()) > (machine.getTimeToReach() - ((machine.getWorker().getTimeCutBy() / 100) * machine.getTimeToReach()))) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                machine.setTimeBeffore(System.currentTimeMillis() / 1000);
                                                UserMaterial mat = realm.where(UserMaterial.class).equalTo("id", machine.getIdMaterialToGive()).findFirst();
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

    public void timeOutOfApp(){
        try (Realm realm = Realm.getDefaultInstance()) {
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            if (userFir != null) {
                User user = realm.where(User.class).equalTo("idUser", userFir.getUid()).findFirst();
                if (user != null) {
                    Response<String> userResponce = mApi.timeOutOfApp("lastOutOfApp/" + userFir.getUid()).execute();
                }
            }
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
        for (String s : componentsRunning) {
            components.get(s).run();
            componentsSuccessful.add(s);
        }

        if (componentsSuccessful.size() == componentsRunning.size())
            mCallBack.get(COMPONENTS).addOnSucsses();
    }

    public void compose() {
        for (Composers s : composersRunning) {
            composers.get(s).run();
            composersSuccessful.add(s);
        }
        if (composersSuccessful.size() == composersRunning.size())
            mCallBack.get(COMPOSERS).addOnSucsses();
    }

    public void update() {
        updateBackground();
        mCallBack.get(UPDATE).addOnSucsses();
    }

    private void updateBackground() {
        try {
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            if (userFir != null) {
                Response<String> response = mApi.updateBackground("updateBackgroundUser/" + userFir.getUid()).execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDefaultMachines() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMachine.class));
            Response<List<DefaultMachine>> machines = mApi.getMachines().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(machines.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseUsersMachines() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(UserMachine.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserMachine>> machines = mApi.getUserMachines("userMachines/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(machines.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDefaultMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultMaterial.class));
            Response<List<DefaultMaterial>> materials = mApi.getDefaultMaterials().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(materials.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseUserMaterials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(UserMaterial.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserMaterial>> materials = mApi.getUserMaterials("userMaterials/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(materials.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseDefaultWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(DefaultWorker.class));
            Response<List<DefaultWorker>> workers = mApi.getDefaultWorkers().execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(workers.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseUserWorkers() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(UserWorker.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<List<UserWorker>> workers = mApi.getUserWorkers("userWorkers/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(workers.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseUser() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realm1 -> realm.delete(User.class));
            FirebaseUser userFir = FirebaseAuth.getInstance().getCurrentUser();
            Response<User> user = mApi.getUser("user/" + userFir.getUid()).execute();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(user.body()));
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
        }
    }
}

