package com.yongsu.floproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yongsu.floproject.databinding.ActivitySongBinding
import com.yongsu.floproject.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 객체를 만들어줌
        val a = A()
        val b = B()
        a.start()
        // join()을 통하여 다른 쓰레드가 실행되지 못하게 막음
        a.join()
        b.start()
    }

    // 새로운 클래스를 만들고 Thread를 상속받음
    class A : Thread(){
        // run을 override 하고 내부의 코드가 Thread가 시작함과 동시에 실행됨.
        override fun run(){
            super.run()
            for(i in 1..1000){
                Log.d("testtest", "first : $i")
            }
        }
    }

    class B : Thread(){
        override fun run(){
            super.run()
            for(i in 1000 downTo 1){
                Log.d("testtest", "second : $i")
            }
        }
    }
}