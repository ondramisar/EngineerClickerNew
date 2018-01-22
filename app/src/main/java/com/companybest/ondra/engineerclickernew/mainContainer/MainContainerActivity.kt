package com.companybest.ondra.engineerclickernew.mainContainer

import android.graphics.PixelFormat
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.companybest.ondra.adron.BaseClasses.BasicAdrClass
import com.companybest.ondra.adron.Engine.Engine
import com.companybest.ondra.adron.Entity.Scene
import com.companybest.ondra.adron.OpenGl.TextureLibrary
import com.companybest.ondra.adron.Rendering.AdrGlSurfaceView
import com.companybest.ondra.engineerclickernew.R
import com.companybest.ondra.engineerclickernew.fragments.MachineFragment
import com.companybest.ondra.engineerclickernew.fragments.ShopFragment
import com.companybest.ondra.engineerclickernew.networkAndLoading.NetworkClient
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
        val net = NetworkClient()
        net.updateMachineWork()
    }

    fun startFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, fragment)
                .commit()

    }

    fun startFragmentTransactionWithBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()

    }

    override fun onBackPressed() {
        if(fragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
        else {
            fragmentManager.popBackStack()
        }
    }
}
