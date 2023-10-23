package com.yongsu.floproject.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yongsu.floproject.fragment.DetailFragment
import com.yongsu.floproject.fragment.SongFragment
import com.yongsu.floproject.fragment.VideoFragment

class AlbumVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    // 여기서 우리는 수록곡, 상세정보, 영상 3개의 Fragment만 사용하므로 3으로 한다.
    override fun getItemCount(): Int = 3

    // position에 따라서 어떤 Fragment를 사용할지 설정해준다.
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}