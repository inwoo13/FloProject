package com.yongsu.floproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yongsu.floproject.R
import com.yongsu.floproject.adapter.AlbumVPAdapter
import com.yongsu.floproject.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    private var _binding : FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val title = arguments?.getString("albumTitle")
        val singer = arguments?.getString("albumSinger")

        binding.albumMusicTitleTv.text = title
        binding.albumSingerNameTv.text = singer

        initViewPager()
        initAlbumback()

        return binding.root
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