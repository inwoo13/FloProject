package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.yongsu.floproject.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    // lateinit은 선언은 지금하는데 초기화는 나중에 해준다는 의미이다.
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    private var gson: Gson = Gson()

    // nullable인 이유는 activity가 소멸될떄 mediaPlayer를 해제시켜주어야 하기 때문
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate은 xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

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

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }


        initRepeatBtn()
        initRandomBtn()
    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000

        // 어플이 종료되어도 song의 상황이 저장되려면 어딘가에 저장이 되어야 함 => SharedPreference
        // sharedPreference는 내부 저장소에 데이터를 저장할 수 있게 해줌 => 앱이 종료되었다가 실행해도 남아있음
        // name은 이것의 이름, MODE_PRIVATE은 모드를 자기 앱에서만 사용할 수 있게 할 수 있음
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터
        val songJson = gson.toJson(song) // song이라는 java 객체를 Json 형태로 변환
        editor.putString("songData", songJson)

        editor.apply() // apply를 해줘야 실제 저장소에 저장이 됨
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }



    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this@SongActivity, music)
        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }

    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){
                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        // 현재 스레드가 UI 스레드라면 UI 자원을 사용하는 행동에 대해서는 즉시 실행된다는 것이고,
                        // 만약 현재 스레드가 UI 스레드가 아니라면 행동은 UI 스레드의 자원 사용 이벤트 큐에 들어가게 되는 것 입니다.
                        // Handler 사용해도 무관
                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","${e.message}")
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