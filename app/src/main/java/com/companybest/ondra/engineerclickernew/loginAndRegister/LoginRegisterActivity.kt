package com.companybest.ondra.engineerclickernew.loginAndRegister

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.companybest.ondra.engineerclickernew.networkAndLoading.LoadingActivity
import com.companybest.ondra.engineerclickernew.R
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask


class LoginRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)


        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent: Intent = Intent(applicationContext, LoadingActivity::class.java)
            startActivity(intent)
        }
        goToRegFragment()

    }

    fun goToRegFragment() {
        val mFragmentManager = supportFragmentManager
        val registerFragment = RegisterFragment()
        val args = Bundle()
        registerFragment.arguments = args
        mFragmentManager.beginTransaction().add(R.id.register_login_container, registerFragment).commit()
    }

    fun goToLogInFramgent() {
        val fragmentManager = supportFragmentManager
        val loginFragment = LoginFragment()
        val args = Bundle()
        loginFragment.arguments = args
        fragmentManager.beginTransaction().replace(R.id.register_login_container, loginFragment).addToBackStack(null).commit()
    }

    companion object {
        fun checkInternetConnection(c: Context): Boolean {
            return isNetworkConnected(c) && isOnline()
        }

        private fun isNetworkConnected(c: Context): Boolean {
            val connectivityManager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        private fun isOnline(): Boolean {
            val executorService = Executors.newFixedThreadPool(1)

            val isConnected = executorService.submit(object : Callable<Boolean> {
                @Throws(Exception::class)
                override fun call(): Boolean? {
                    try {
                        val timeoutMs = 1500
                        val sock = Socket()
                        val sockaddr = InetSocketAddress("8.8.8.8", 53)

                        sock.connect(sockaddr, timeoutMs)
                        sock.close()

                        return true
                    } catch (e: IOException) {
                        return false
                    }

                }
            }) as FutureTask<Boolean>

            try {
                return isConnected.get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            return false
        }
    }


}
