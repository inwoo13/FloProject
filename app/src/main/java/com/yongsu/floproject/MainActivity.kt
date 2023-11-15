package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.yongsu.floproject.databinding.ActivityMainBinding
import com.yongsu.floproject.roomdb.entity.Song
import com.yongsu.floproject.fragment.HomeFragment
import com.yongsu.floproject.fragment.LockerFragment
import com.yongsu.floproject.fragment.LookFragment
import com.yongsu.floproject.fragment.SearchFragment
import com.yongsu.floproject.roomdb.database.SongDatabase


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

        //inputDummySongs() // 한번만 넣어도 됨
        initBottomNavigation()

//        // Foreground
//        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
//            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
//        }
//        startActivity(intent)
//
//        binding.startServBtn.setOnClickListener {
//            Log.d("servicesss", "버튼 누름")
//            val intent = Intent(this@MainActivity, Foregrounding::class.java)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(intent)
//            } else {
//                startService(intent)
//            }
//        }
//        binding.stopServBtn.setOnClickListener {
//            val intent = Intent(this, Foregrounding::class.java)
//            stopService(intent)
//        }

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", Mainsong.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.testbtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        initMusicPlay()
    }

    override fun onStart() {
        super.onStart()
//        // 액티비티 전환이 될때 onStart()부터 해주기 때문에 여기서 Song 데이터를 가져옴
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        Mainsong = if(songJson == null){
//            Song("TimmyTrumpet", "Timmy", 0, 96, false, "timmy_trumpet")
//        }else{
//            // gson을 사용하여 songJson을 Song class의 자바 객체로 변환해줘라고 하는거임
//            gson.fromJson(songJson, Song::class.java)
//        }

        // spf에 sharedpreference에 저장되어있던 값을 가져옴
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        // spf Array의 가장 처음 song의 id를 가져옴
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        // 0이면 id가 1인 것을 Mainsong에 저장
        // 아니라면 songId에 저장된 id를 가진 것을 Mainsong에 저장
        // 저장된 songId값으로 song초기화
        Mainsong = if(songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", Mainsong.id.toString())
        setMiniPlayer(Mainsong)
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

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this@MainActivity)!!
        val songs = songDB.songDao().getSongs()

        if(songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                96,
                false,
                "timmy_trumpet",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_nextlevel",
                R.drawable.img_album_exp3,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Weekend",
                "태연 (Tae Yeon)",
                0,
                246,
                false,
                "music_weekend",
                R.drawable.img_album_exp6,
                false,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }


}