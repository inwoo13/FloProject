package com.yongsu.floproject.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yongsu.floproject.fragment.MusicFileFragment
import com.yongsu.floproject.fragment.SavedAlbumFragment
import com.yongsu.floproject.fragment.SavedSongFragment

class LockerVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }

}