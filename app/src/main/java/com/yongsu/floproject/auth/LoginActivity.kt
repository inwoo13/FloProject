package com.yongsu.floproject.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yongsu.floproject.MainActivity
import com.yongsu.floproject.databinding.ActivityLoginBinding
import com.yongsu.floproject.retrofit.LoginView
import com.yongsu.floproject.retrofit.module.AuthService
import com.yongsu.floproject.retrofit.response.Result
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.User

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 화면으로 넘어가기
        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    // Validation Check
    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this@LoginActivity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this@LoginActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd : String = binding.loginPasswordEt.text.toString()

//        val songDB = SongDatabase.getInstance(this@LoginActivity)!!
//        val user = songDB.userDao().getUser(email, pwd)
//
//        user?.let{
//            Log.d("LOGIN_ACT/GET_USER", "userId : ${user.id}, $user")
//            //saveJwt(user.id)
//            startMainActivity()
//        }

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(User(email, pwd, ""))

        Toast.makeText(this@LoginActivity, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    // user의 id값을 jwt토큰으로 대체해서 사용
    // 따라서 로그인을 진행하기 전에 sharedPreference로 id를 저장
//    private fun saveJwt(jwt: Int){
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//
//        editor.putInt("jwt", jwt)
//        editor.apply()
//    }

    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        saveJwt2(result.jwt)
        startMainActivity()
    }

    override fun onLoginFailure(code: Int, message: String) {
        Log.e("LOGIN/RESPONSE", code.toString())
        when(code) {
            2015, 2019, 3014 -> {
                Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
            }
            2018 -> {

            }
        }
    }
}