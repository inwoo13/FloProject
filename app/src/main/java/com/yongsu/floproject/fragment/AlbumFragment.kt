package com.yongsu.floproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.yongsu.floproject.R
import com.yongsu.floproject.adapter.AlbumVPAdapter
import com.yongsu.floproject.databinding.FragmentAlbumBinding
import com.yongsu.floproject.fragment.fourmain.HomeFragment
import com.yongsu.floproject.retrofit.response.FloChartAlbums
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.Album
import com.yongsu.floproject.roomdb.entity.Like

class AlbumFragment : Fragment() {

    private var _binding : FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        // Home에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val gson = Gson()
        val album = gson.fromJson(albumData, FloChartAlbums::class.java)

        val sharedPreferences = activity?.getSharedPreferences("album", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("albumIdx", "${album.albumIdx}")
        editor?.apply()

        // Home에서 넘어온 데이터 반영
        isLiked = isLikedAlbum(album.albumIdx)
        setInit(album)
        setOnClickListeners(album)

        initViewPager()
        initAlbumback()

        return binding.root
    }

    private fun setInit(album: FloChartAlbums){
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        if(album.coverImgUrl == "" || album.coverImgUrl == null){

        } else {
            Log.d("image",album.coverImgUrl )
            Glide.with(requireContext()).load(album.coverImgUrl).into(binding.albumAlbumIv)
        }
        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)

        }
    }

    private fun getJwt():Int{
        // activity?를 붙이는 이유는 프래그먼트에서 사용할 때의 방법이기 떄문
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        // 해당 앨범을 좋아요했는지 확인해주는 변수
        val likeId = songDB.albumDao().isLikedAlbum(userId, albumId)

        // 앨범을 좋아요 했으면 likeId가 null이 아니라 true를 반환
        return likeId != null
    }

    private fun disLikedAlbum(albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        // 해당 앨범을 좋아요했는지 확인해주는 변수
        songDB.albumDao().disLikedAlbum(userId, albumId)

    }

    private fun setOnClickListeners(album: FloChartAlbums){
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if(isLiked){
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.albumIdx)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.albumIdx)
            }
        }
    }

    private fun initViewPager(){
        with(binding){
            val adapter = AlbumVPAdapter(this@AlbumFragment)
            albumContentVp.adapter = adapter
            // TabLayout과 ViewPager2를 연결하는 중재자
            // Tab이 선택될 때 ViewPager2의 위치와 선택된 탭을 동기화하고
            // 사용자가 ViewPager2를 스크롤 할 때 TabLayout의 스크롤 위치를 동기화한다.
            TabLayoutMediator(albumContentTb, albumContentVp){
                tab, position ->
                tab.text = information[position]
            }.attach()
        }
    }

    private fun initAlbumback(){
        binding.albumBackIv.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }


}