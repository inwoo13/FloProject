package com.yongsu.floproject.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yongsu.floproject.R
import com.yongsu.floproject.adapter.SavedSongRVAdapter
import com.yongsu.floproject.databinding.FragmentSavedSongBinding
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.Song

class SavedSongFragment : Fragment() {

    private var _binding: FragmentSavedSongBinding? = null
    private val binding get() = _binding!!
    private val itemSongAdapter = SavedSongRVAdapter()

    lateinit var songDB: SongDatabase

    private var isSelect = false

    // 메인에 전체 선택이 되었음을 알릴 수 있는 리스너
    interface OnSelectClickListener{
        fun onSelectClick(isSelectOn: Boolean)
    }
    private var listener: OnSelectClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // MainActivity가 OnSelectClickListener 인터페이스를 구현하고 있는지 확인하고
        // 구현하고 있다면 listener로 등록합니다.
        if (context is OnSelectClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSelectClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        // Fragment가 Activity에서 분리될 때 listener를 초기화합니다.
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedSongBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        initRecyclerView()

        // 전체선택을 눌러서 전체선택의 색을 변경하고 Main에 신호를 보냄
        binding.allSelectTv.setOnClickListener {
            isSelect = true
            listener?.onSelectClick(true)
            initSelectAll()
        }

        binding.allSelectTvOn.setOnClickListener {
            isSelect = false
            listener?.onSelectClick(false)
            initSelectAll()
        }

        return binding.root
    }

    fun deleteAll(){
        isSelect = false
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            initSelectAll()
        }
    }

    private fun initSelectAll(){
        if(isSelect){
            binding.checkIV.visibility = View.GONE
            binding.allSelectTv.visibility=View.GONE
            binding.checkIVSelect.visibility = View.VISIBLE
            binding.allSelectTvOn.visibility = View.VISIBLE
        }else{
            binding.checkIV.visibility = View.VISIBLE
            binding.allSelectTv.visibility=View.VISIBLE
            binding.checkIVSelect.visibility = View.GONE
            binding.allSelectTvOn.visibility = View.GONE
        }
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