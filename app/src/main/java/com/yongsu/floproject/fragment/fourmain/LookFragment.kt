package com.yongsu.floproject.fragment.fourmain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.yongsu.floproject.adapter.SongRVAdapter
import com.yongsu.floproject.databinding.FragmentLookBinding
import com.yongsu.floproject.retrofit.LookView
import com.yongsu.floproject.retrofit.module.SongService
import com.yongsu.floproject.retrofit.response.FloChartResult

class LookFragment : Fragment(), LookView {

    private var _binding: FragmentLookBinding? = null
    private val binding get() = _binding!!
    private lateinit var floCharAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLookBinding.inflate(inflater, container, false)

        initTabLayout()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: FloChartResult) {
        floCharAdapter = SongRVAdapter(requireContext(), result)

        binding.lookFloChartRv.adapter = floCharAdapter
    }

    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()

    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }

    private fun initTabLayout(){
        with(binding){
            lookContentTb.addTab(lookContentTb.newTab().setText("차트"))
            lookContentTb.addTab(lookContentTb.newTab().setText("영상"))
            lookContentTb.addTab(lookContentTb.newTab().setText("장르"))
            lookContentTb.addTab(lookContentTb.newTab().setText("상황"))
            lookContentTb.addTab(lookContentTb.newTab().setText("분위기"))

            lookContentTb.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            // Tab 1 Selected
                        }
                        1 -> {
                            // Tab 2 Selected
                        }
                        2 -> {
                            // Tab 3 Selected
                        }
                        3 -> {

                        }

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })

            setTabItemMargin(binding.lookContentTb, 25)
        }
    }

    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int = 20) {
        for(i in 0 until 3) {
            val tabs = tabLayout.getChildAt(0) as ViewGroup
            for(i in 0 until tabs.childCount) {
                val tab = tabs.getChildAt(i)
                val lp = tab.layoutParams as LinearLayout.LayoutParams
                lp.marginEnd = marginEnd
                tab.layoutParams = lp
                tabLayout.requestLayout()
            }
        }
    }
}