package com.yongsu.floproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.yongsu.floproject.R
import com.yongsu.floproject.adapter.SavedSongRVAdapter
import com.yongsu.floproject.databinding.FragmentSavedSongBinding
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.Album
import com.yongsu.floproject.roomdb.entity.Song

class SavedSongFragment : Fragment() {

    private var _binding: FragmentSavedSongBinding? = null
    private val binding get() = _binding!!
    private val itemSongAdapter = SavedSongRVAdapter()

    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView(){
        binding.lockerSavedSongRecyclerView.adapter = itemSongAdapter
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.lockerSavedSongRecyclerView.layoutManager = manager
        itemSongAdapter.addSongs(songDB.songDao().getLikedSong(true) as ArrayList<Song>)

        itemSongAdapter.setMyItemClickListener(object: SavedSongRVAdapter.SavedSongClickListener{
            override fun onRemoveSavedSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })
    }

}

//private fun Dummy() : ArrayList<Album>{
//    val dummy1 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp)
//    val dummy2 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
//    val dummy3 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
//    val dummy4 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
//    val dummy5 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
//    val dummy6 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)
//    val dummy7 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp)
//    val dummy8 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
//    val dummy9 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
//    val dummy10 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
//    val dummy11 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
//    val dummy12 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)
//
//    val arr = ArrayList<Album>()
//    arr.add(dummy1)
//    arr.add(dummy2)
//    arr.add(dummy3)
//    arr.add(dummy4)
//    arr.add(dummy5)
//    arr.add(dummy6)
//    arr.add(dummy7)
//    arr.add(dummy8)
//    arr.add(dummy9)
//    arr.add(dummy10)
//    arr.add(dummy11)
//    arr.add(dummy12)
//
//    return arr
//}