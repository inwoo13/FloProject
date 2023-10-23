package com.yongsu.floproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yongsu.floproject.adapter.LockerVPAdapter
import com.yongsu.floproject.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    private val information = arrayListOf("저장한 곡", "음악파일")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        initViewPager()

        return binding.root
    }

    private fun initViewPager(){
        with(binding){
            val lockerAdapter = LockerVPAdapter(this@LockerFragment)
            lockerContentVp.adapter = lockerAdapter
            TabLayoutMediator(lockerContentTb, lockerContentVp){
                tab, position ->
                tab.text = information[position]
            }.attach()
        }


    }

}