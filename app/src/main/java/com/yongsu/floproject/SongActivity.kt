package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yongsu.floproject.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    // lateinit은 선언은 지금하는데 초기화는 나중에 해준다는 의미이다.
    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate은 xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앨범 제목 표시
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        // 종료하면서 데이터 넘겨주기
        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("songsong", binding.songMusicTitleTv.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            if(!isFinishing) finish()
        }

        initPlayPauseBtn()
        initRepeatBtn()
        initRandomBtn()
    }

    private fun initPlayPauseBtn(){
        with(binding){
            songMiniplayerIv.setOnClickListener {
                songMiniplayerIv.visibility = View.GONE
                songPauseIv.visibility = View.VISIBLE
            }
            songPauseIv.setOnClickListener {
                songMiniplayerIv.visibility = View.VISIBLE
                songPauseIv.visibility = View.GONE
            }
        }
    }

    private fun initRepeatBtn(){
        with(binding){
            songRepeatIv.setOnClickListener {
                songRepeatIv.visibility = View.GONE
                songRepeatActiveIv.visibility = View.VISIBLE
            }
            songRepeatActiveIv.setOnClickListener {
                songRepeatActiveIv.visibility = View.GONE
                songRepeatIv.visibility = View.VISIBLE
            }
        }
    }

    private fun initRandomBtn(){
        with(binding){
            songRandomIv.setOnClickListener {
                songRandomIv.visibility = View.GONE
                songRandomActiveIv.visibility = View.VISIBLE
            }
            songRandomActiveIv.setOnClickListener {
                songRandomActiveIv.visibility = View.GONE
                songRandomIv.visibility = View.VISIBLE
            }
        }
    }

}