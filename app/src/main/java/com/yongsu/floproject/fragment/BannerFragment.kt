package com.yongsu.floproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yongsu.floproject.R
import com.yongsu.floproject.databinding.FragmentBannerBinding
import com.yongsu.floproject.databinding.FragmentHomeBinding

class BannerFragment(val imgRes : Int) : Fragment() {

    private var _binding: FragmentBannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBannerBinding.inflate(inflater, container, false)

        // 이미지 설정
        binding.bannerImageIv.setImageResource(imgRes)

        return binding.root
    }
}