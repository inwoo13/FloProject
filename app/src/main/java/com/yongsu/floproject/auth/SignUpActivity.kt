package com.yongsu.floproject.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yongsu.floproject.databinding.ActivitySignUpBinding
import com.yongsu.floproject.retrofit.SignUpView
import com.yongsu.floproject.retrofit.api.AuthRetrofitInstance
import com.yongsu.floproject.retrofit.getRetrofit
import com.yongsu.floproject.retrofit.module.AuthService
import com.yongsu.floproject.retrofit.response.AuthResponse
import com.yongsu.floproject.roomdb.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd : String = binding.signUpPasswordEt.text.toString()
        val name : String = binding.signUpNameEt.text.toString()

        return User(email, pwd, name)
    }

    // 아이디, 비밀번호 칸이 채워지지 않았거나 비밀번호 확인이 같지 않은 경우를 걸러내기
    // Validation Check
//    private fun signUp() {
//        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()){
//            Toast.makeText(this@SignUpActivity, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            isFalse = true
//            return
//        }
//        else isFalse = false
//        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
//            Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            isFalse = true
//            return
//        }
//        else isFalse = false
//
//        val userDB = SongDatabase.getInstance(this@SignUpActivity)!!
//        userDB.userDao().insert(getUser())
//
//        val user = userDB.userDao().getUsers()
//            Log.d("SIGNUPACTTT", user.toString())
//
//    }

    private fun signUp(){
        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this@SignUpActivity, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpNameEt.text.toString().isEmpty()){
            Toast.makeText(this@SignUpActivity, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this@SignUpActivity)
        authService.signUp(getUser())

    }

    override fun onSignUpSuccess() {
        Log.d("SIGNUP-ACT/RESPONSE", "성공")
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        Log.e("SIGNUP-ACT/RESPONSE", code.toString())
        when(code) {
                    2016, 2017 -> {
                        binding.signUpEmailErrorTv.text = message
                        binding.signUpEmailErrorTv.visibility = View.VISIBLE
                    }
                    2018 -> {

                    }
                }
    }
}