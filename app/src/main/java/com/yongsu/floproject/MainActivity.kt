package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.yongsu.floproject.databinding.ActivityMainBinding
import com.yongsu.floproject.fragment.HomeFragment
import com.yongsu.floproject.fragment.LockerFragment
import com.yongsu.floproject.fragment.LookFragment
import com.yongsu.floproject.fragment.SearchFragment


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

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

        val song = Song(binding.mainPlayingSongTitleTv.text.toString(),
            binding.mainPlayingSingerTv.text.toString(), 0, 60, false)

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            // title이라는 key값으로 song.title을 intent에 담아줌
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            getResultText.launch(intent)
        }

        binding.testbtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

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