package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.yongsu.floproject.databinding.ActivityMainBinding
import com.yongsu.floproject.fragment.HomeFragment
import com.yongsu.floproject.fragment.LockerFragment
import com.yongsu.floproject.fragment.LookFragment
import com.yongsu.floproject.fragment.SearchFragment


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

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

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            // title이라는 key값으로 song.title을 intent에 담아줌
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            getResultText.launch(intent)
        }

        binding.testbtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        // 액티비티 전환이 될때 onStart()부터 해주기 때문에 여기서 Song 데이터를 가져옴
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null){
            Song("TimmyTrumpet", "Timmy", 0, 96, false, "timmy_trumpet")
        }else{
            // gson을 사용하여 songJson을 Song class의 자바 객체로 변환해줘라고 하는거임
            gson.fromJson(songJson, Song::class.java)
        }

        setMiniPlayer(song)
    }

    private fun setMiniPlayer(song: Song){
        binding.mainPlayingSongTitleTv.text = song.title
        binding.mainPlayingSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
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