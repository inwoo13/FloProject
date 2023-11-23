package com.yongsu.floproject.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yongsu.floproject.databinding.ActivitySignUpBinding
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.User

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    private var isFalse = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
            if(!isFalse) finish()
        }
    }

    private fun getUser(): User {
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd : String = binding.signUpPasswordEt.text.toString()

        return User(email, pwd)
    }

    // 아이디, 비밀번호 칸이 채워지지 않았거나 비밀번호 확인이 같지 않은 경우를 걸러내기
    // Validation Check
    private fun signUp() {
        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this@SignUpActivity, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            isFalse = true
            return
        }
        else isFalse = false
        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            isFalse = true
            return
        }
        else isFalse = false

        val userDB = SongDatabase.getInstance(this@SignUpActivity)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUsers()
        Log.d("SIGNUPACTTT", user.toString())

    }
}