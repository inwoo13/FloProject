package com.yongsu.floproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yongsu.floproject.R
import com.yongsu.floproject.databinding.FragmentAlbumBinding
import com.yongsu.floproject.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private var _binding : FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoBinding.inflate(inflater,container,false)

        return binding.root
    }
}