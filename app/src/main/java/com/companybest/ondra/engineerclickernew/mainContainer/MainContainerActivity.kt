package com.companybest.ondra.engineerclickernew.mainContainer

import android.graphics.PixelFormat
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import com.companybest.ondra.adron.BaseClasses.BasicAdrClass
import com.companybest.ondra.adron.Engine.Engine
import com.companybest.ondra.adron.Entity.Scene
import com.companybest.ondra.adron.OpenGl.TextureLibrary
import com.companybest.ondra.adron.Rendering.AdrGlSurfaceView
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.fragments.ShopFragment
import com.companybest.ondra.engineerclickernew.fragments.MachineFragment
import com.companybest.ondra.engineerclickernew.models.Machine
import com.companybest.ondra.engineerclickernew.models.User
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_main_container.*
import javax.microedition.khronos.opengles.GL10

class MainContainerActivity : BasicAdrClass() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                startFragmentTransaction(MachineFragment.newInstance(MachineFragment.userMachineType))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                startFragmentTransaction(ShopFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
        startFragmentTransaction(MachineFragment.newInstance(MachineFragment.userMachineType))
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val adrView = findViewById<AdrGlSurfaceView>(R.id.view)
        adrView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        adrView.holder.setFormat(PixelFormat.TRANSLUCENT)
        val engine = Engine(this, TextureLibrary(), false)
        setUpEngine(engine, this, adrView)
    }

    override fun setUpScene(): Scene {
        return Scene()
    }

    override fun onSurfaceCreated(gl10: GL10?) {
        super.onSurfaceCreated(gl10)
        gl10?.glClearColor(200f, 54f, 54f, 0.5f)
    }

    override fun update(dt: Float) {
        Realm.getDefaultInstance().use { realm ->
            val mAuth = FirebaseAuth.getInstance()
            val userFire = mAuth.currentUser
            val user = realm.where(User::class.java).equalTo("idUser", userFire?.uid).findFirst()
            val machines: RealmList<Machine>? = user?.machines
            Log.i("usern", machines?.size.toString())
            if (machines != null) {
                for (machine in machines) {
                    if (machine?.timeBeffore == 0L) {
                        realm.executeTransaction(Realm.Transaction {
                            machine.timeBeffore = System.currentTimeMillis() / 1000
                        })
                    } else {
                        val cur = System.currentTimeMillis() / 1000
                        if (cur - (machine?.timeBeffore ?: 0) > machine?.timeToReach ?: 0) {
                            realm.executeTransaction(Realm.Transaction {
                                machine?.timeBeffore = System.currentTimeMillis() / 1000
                                user.materials.forEach({
                                    if (it.id == machine.idMaterialToGive) {
                                        it.numberOf += 1
                                        Log.i("usern", it.numberOf.toString())
                                    }
                                })
                            })
                            Log.i("usern", "hey we in ${machine.name}")
                        }
                    }
                }
            }
        }
    }

    fun startFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, fragment)
                .commit()
    }
}
