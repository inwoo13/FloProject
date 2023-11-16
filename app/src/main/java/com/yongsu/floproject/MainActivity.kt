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
import com.yongsu.floproject.roomdb.database.AlbumDatabase
import com.yongsu.floproject.roomdb.database.SongDatabase


class MainActivity : AppCompatActivity(), HomeFragment.OnPlayClickListener {

    lateinit var binding: ActivityMainBinding

    private var gson: Gson = Gson()

    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    lateinit var albumDB: AlbumDatabase
    var nowPos = 0

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

        initPlayList()

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


        initClickListener()
        initMusicPlay()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this@MainActivity)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.testbtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        binding.mainNextSongBtn.setOnClickListener {
            moveSong(1)
        }
        binding.mainBackSongBtn.setOnClickListener {
            moveSong(-1)
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0 ){
            Toast.makeText(this@MainActivity, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this@MainActivity, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        songs[nowPos].isPlaying = false

        nowPos += direct

        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    override fun onStart() {
        super.onStart()
//        // 액티비티 전환이 될때 onStart()부터 해주기 때문에 여기서 Song 데이터를 가져옴

        // spf에 sharedpreference에 저장되어있던 값을 가져옴
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        // spf Array의 가장 처음 song의 id를 가져옴
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("song ID", songs[nowPos].id.toString())
        setMiniPlayer(songs[nowPos])
    }




    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.mainProgressSb.progress * songs[nowPos].playTime)/100)/1000
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    override fun onPlayClick(albumId: Int) {
        songs[nowPos].isPlaying = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null


        songs.clear()   // 재생하려고 넣어놨던 데이터들을 제거
        Log.d("id찾기", "$albumId")

        // 앨범 아이디가 같은 모든 song을 불러와서 songs에 넣는다
        songs.addAll(songDB.songDao().getSongsByalbumIdx(albumId))
        // 처음 곡부터 재생할 것이므로 nowPos 초기화
        nowPos = 0
        if(songs.isNotEmpty()) {
            songs[nowPos].isPlaying = true
            setMiniPlayer(songs[nowPos])
        } else {
            Log.d("id찾기", "빔")
        }
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
            songs[nowPos].isPlaying =  isPlaying

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

        if(songs.isNotEmpty()){
            songDB.songDao().deleteAllSongs()
        }

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
                1
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
                1
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
                2
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
                3
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
                2
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
                4
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
                5
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }


}