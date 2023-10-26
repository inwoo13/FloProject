package com.yongsu.floproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yongsu.floproject.databinding.ActivitySongBinding
import com.yongsu.floproject.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestBinding

    private var nowTime:String = ""
    private var minute = 0
    private var second = 0
    private var started = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        initTimeStart(handler)
    }

    private fun initTimeStart(handler: Handler){
        with(binding){
            startBtn.setOnClickListener {


                val inputTime = mainTimeEt.text.toString().split(":").map{it.toInt()}
                minute = inputTime[0]
                second = inputTime[1]
                started = true

                initStartPause()

                Thread{
                    while(started){
                        Thread.sleep(1000)
                        if(!started) break

                        if(second == 0 && minute == 0){
                            started = false

                            handler.post{
                                initStartPause()
                                mainTimeEt.setText("00:00")
                            }

                            break
                        }

                        if(second == 0 && minute>0){
                            minute -= 1
                            second = 59
                        } else if(second > 0){
                            second -= 1
                        }
                        var finalMinute = minute
                        val finalSecond = second


                        handler.post{
                            mainTimeEt.setText("${finalMinute}:${finalSecond}")
                        }
                    }
                }.start()
            }

            pauseBtn.setOnClickListener {
                started = false
                initStartPause()
            }

            resetBtn.setOnClickListener {
                started = false
                initStartPause()

                mainTimeEt.setText("00:00")
            }
        }
    }

    private fun initStartPause(){
        with(binding){
            if(started){
                startBtn.visibility = View.GONE
                pauseBtn.visibility = View.VISIBLE
            }else{
                startBtn.visibility = View.VISIBLE
                pauseBtn.visibility = View.GONE
            }
        }
    }
}