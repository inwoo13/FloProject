package com.yongsu.floproject

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.yongsu.floproject.databinding.ActivitySongBinding
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.Song

class SongActivity : AppCompatActivity() {

    // lateinit은 선언은 지금하는데 초기화는 나중에 해준다는 의미이다.
    lateinit var binding: ActivitySongBinding

    lateinit var timer: Timer

    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    // nullable인 이유는 activity가 소멸될떄 mediaPlayer를 해제시켜주어야 하기 때문
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate은 xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()

        // 앨범 제목 표시
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        initRepeatBtn()
        initRandomBtn()
        initClickListener()
    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000

        // 어플이 종료되어도 song의 상황이 저장되려면 어딘가에 저장이 되어야 함 => SharedPreference
        // sharedPreference는 내부 저장소에 데이터를 저장할 수 있게 해줌 => 앱이 종료되었다가 실행해도 남아있음
        // name은 이것의 이름, MODE_PRIVATE은 모드를 자기 앱에서만 사용할 수 있게 할 수 있음
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터
//        val songJson = gson.toJson(songs[nowPos]) // song이라는 java 객체를 Json 형태로 변환
//        editor.putString("songData", songJson)

        // 앱이 종료될 때는 songId 값만 저장해도 됨
        editor.putInt("songId", songs[nowPos].id)

        editor.apply() // apply를 해줘야 실제 저장소에 저장이 됨
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initClickListener(){
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

            Log.d("id찾기", "${songs[nowPos].albumIdx}")
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        // 좋아요 버튼에 클릭리스너 달기
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0 ){
            Toast.makeText(this@SongActivity, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this@SongActivity, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike  // ex) 좋아요한 상태면 취소한 상태로 바꿈.(그 반대도 적용)
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            initToast("좋아요 한 곡에 담겼습니다.")
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            initToast("좋아요 한 곡이 취소되었습니다.")
        }
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this@SongActivity)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        // 지금 보여지는 song이 nowPos
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun initToast(likeText: String){
        // layoutInflater를 가져옴
        val inflater = layoutInflater

        // custom_toast 레이아웃을 inflate
        val layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

        // textView text 설정
        val text = layout.findViewById<TextView>(R.id.likeTv)
        text.text = likeText

        // 토스트 생성
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout

        // 위치와 마진 설정
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 100)


        toast.show()
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        // 여기서 음악이 어차피 재시작되므로 따로 재시작할 필요는 없다
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this@SongActivity, music)

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus (isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
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
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
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