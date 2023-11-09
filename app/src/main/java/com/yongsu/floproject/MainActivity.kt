package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.yongsu.floproject.databinding.ActivityMainBinding
import com.yongsu.floproject.datas.Song
import com.yongsu.floproject.foreground.Foregrounding
import com.yongsu.floproject.fragment.HomeFragment
import com.yongsu.floproject.fragment.LockerFragment
import com.yongsu.floproject.fragment.LookFragment
import com.yongsu.floproject.fragment.SearchFragment


class MainActivity : AppCompatActivity(), HomeFragment.OnPlayClickListener {

    lateinit var binding: ActivityMainBinding

    private var gson: Gson = Gson()

    lateinit var Mainsong: Song
    private var mediaPlayer: MediaPlayer? = null

    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("songsong")
            Toast.makeText(applicationContext, returnString, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
        startActivity(intent)

        binding.startServBtn.setOnClickListener {
            Log.d("servicesss", "버튼 누름")
            val intent = Intent(this@MainActivity, Foregrounding::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
        binding.stopServBtn.setOnClickListener {
            val intent = Intent(this, Foregrounding::class.java)
            stopService(intent)
        }

        binding.mainNextSongBtn.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            // title이라는 key값으로 song.title을 intent에 담아줌
            intent.putExtra("title", Mainsong.title)
            intent.putExtra("singer", Mainsong.singer)
            intent.putExtra("second", Mainsong.second)
            intent.putExtra("playTime", Mainsong.playTime)
            intent.putExtra("isPlaying", Mainsong.isPlaying)
            intent.putExtra("music", Mainsong.music)
            getResultText.launch(intent)
        }

        binding.testbtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        initMusicPlay()
    }

    override fun onStart() {
        super.onStart()
        // 액티비티 전환이 될때 onStart()부터 해주기 때문에 여기서 Song 데이터를 가져옴
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)


        Mainsong = if(songJson == null){
            Song("TimmyTrumpet", "Timmy", 0, 96, false, "timmy_trumpet")
        }else{
            // gson을 사용하여 songJson을 Song class의 자바 객체로 변환해줘라고 하는거임
            gson.fromJson(songJson, Song::class.java)
        }

    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        Mainsong.second = ((binding.mainProgressSb.progress * Mainsong.playTime)/100)/1000
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    override fun onPlayClick(songData: Song) {
        Mainsong = songData
        Mainsong.isPlaying = true
        setMiniPlayer(Mainsong)
        mediaPlayer?.start()
    }

    private fun initMusicPlay(){
        with(binding){
            // 음악이 정지해있을때 재생
            mainMiniplayerBtn.setOnClickListener {
                setPlayerStatus(true)
            }
            // 음악이 재생중일때 정지
            mainPauseBtn.setOnClickListener {
                setPlayerStatus(false)
            }
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean){
        with(binding){
            Mainsong.isPlaying =  isPlaying

            if(isPlaying){
                binding.mainMiniplayerBtn.visibility = View.GONE
                binding.mainPauseBtn.visibility = View.VISIBLE
                mediaPlayer?.start()
            } else {
                binding.mainMiniplayerBtn.visibility = View.VISIBLE
                binding.mainPauseBtn.visibility = View.GONE
                if(mediaPlayer?.isPlaying == true){
                    mediaPlayer?.pause()
                }else{

                }
            }

        }
    }


    private fun setMiniPlayer(song: Song){
        binding.mainPlayingSongTitleTv.text = song.title
        binding.mainPlayingSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this@MainActivity, music)
        setPlayerStatus(song.isPlaying)

    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm", "Home")
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Look", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm", "look")
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Search", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm", "search")
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Locker", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm", "locker")
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}