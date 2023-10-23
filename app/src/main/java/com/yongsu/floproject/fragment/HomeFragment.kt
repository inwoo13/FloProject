package com.yongsu.floproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.yongsu.floproject.R
import com.yongsu.floproject.adapter.BannerVPAdapter
import com.yongsu.floproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayAlbumFirstIv.setOnClickListener {
            initAlbumFragment(binding.homeTodaySongTitleFirstTv.text.toString(),
            binding.homeTodaySingerFirstTv.text.toString())
        }
        binding.homeTodayAlbumSecondIv.setOnClickListener {
            initAlbumFragment(binding.homeTodaySongTitleSecondTv.text.toString(),
                binding.homeTodaySingerSecondTv.text.toString())
        }
        binding.homeTodayAlbumThirdIv.setOnClickListener {
            initAlbumFragment(binding.homeTodaySongTitleThirdTv.text.toString(),
                binding.homeTodaySingerThirdTv.text.toString())
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun initAlbumFragment(titleTV : String, singerTV : String){
        with(binding){
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("albumTitle", titleTV)
                    putString("albumSinger", singerTV)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}