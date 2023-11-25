package com.yongsu.floproject.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.yongsu.floproject.MainActivity
import com.yongsu.floproject.R
import com.yongsu.floproject.databinding.ActivitySplashBinding
import com.yongsu.floproject.retrofit.AutoLoginView
import com.yongsu.floproject.retrofit.module.AuthService
import com.yongsu.floproject.roomdb.entity.User

class SplashActivity : AppCompatActivity(), AutoLoginView {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authService = AuthService()
        authService.setAutoLoginView(this)
        authService.autoLogin(getJwt())

    }

    override fun onAutoSuccess() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }, 1500)
    }

    override fun onAutoFailure(code: Int, message: String) {
        Log.e("AUTOLOGIN/RESPONSE", "$code  $message")
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }, 1500)
    }

    private fun getJwt(): String? {
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }
}