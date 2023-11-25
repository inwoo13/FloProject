package com.yongsu.floproject.fragment.fourmain

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.yongsu.floproject.R
import com.yongsu.floproject.adapter.AlbumRVAdapter
import com.yongsu.floproject.adapter.BannerVPAdapter
import com.yongsu.floproject.adapter.PannelVPAdapter
import com.yongsu.floproject.databinding.FragmentHomeBinding
import com.yongsu.floproject.fragment.AlbumFragment
import com.yongsu.floproject.fragment.BannerFragment
import com.yongsu.floproject.fragment.PannelFragment
import com.yongsu.floproject.roomdb.database.SongDatabase
import com.yongsu.floproject.roomdb.entity.Album

class HomeFragment : Fragment() {

    interface OnPlayClickListener {
        fun onPlayClick(albumId: Int)
    }

    private var listener: OnPlayClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPlayClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnPlayClickListener")
        }
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sliderHandler = Handler(Looper.getMainLooper())
    private var sliderRunnable: Runnable? = null

    private var gson: Gson = Gson()

    val albums = arrayListOf<Album>()
    lateinit var albumDB: SongDatabase
    var nowPos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initBannerVP()
        initPannelVP()

        initDummyAlbums()
        initAlbumList()

        val albumAdapter = AlbumRVAdapter(albums)

        binding.homeTodayMusicAlbumRv.adapter = albumAdapter
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayMusicAlbumRv.layoutManager = manager

        albumAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                initAlbumFragment(album)
            }

            override fun onPlayClick(album: Album) {
                Log.d("id찾기", "${album.id}")
                Log.d("id찾기", "${album.title}")


                listener?.onPlayClick(album.id) // MainActivity로 AlbumId 전달
            }
        })

        return binding.root
    }

//    private fun Dummy() : ArrayList<Album>{
//
//        val songArr = ArrayList<Song>()
//        val songDummy1 = Song("TimmyTrumpet", "Timmy", 0, 96, false, "timmy_trumpet")
//        songArr.add(songDummy1)
//
//        val dummy1 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp)
//        val dummy2 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
//        val dummy3 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
//        val dummy4 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
//        val dummy5 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
//        val dummy6 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)
//
//        val arr = ArrayList<Album>()
//        arr.add(dummy1)
//        arr.add(dummy2)
//        arr.add(dummy3)
//        arr.add(dummy4)
//        arr.add(dummy5)
//        arr.add(dummy6)
//
//        return arr
//    }

    private fun initAlbumFragment(album: Album){
        with(binding){
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }



    private fun initBannerVP(){
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun initPannelVP(){
        val pannelAdapter = PannelVPAdapter(this)
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp4))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp5))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp6))
        binding.homePannelBackgroundVp.adapter = pannelAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = binding.Indicator;
        indicator.setViewPager(binding.homePannelBackgroundVp);

        sliderRunnable = Runnable {
            val viewPager = binding.homePannelBackgroundVp
            viewPager.currentItem =
                if (viewPager.currentItem < pannelAdapter.itemCount - 1) {
                    viewPager.currentItem + 1
                } else {
                    0
                }
            sliderHandler.postDelayed(sliderRunnable!!, 3000L)
        }
        sliderHandler.post(sliderRunnable!!)
    }

    private fun initAlbumList(){
        albumDB = SongDatabase.getInstance(requireActivity())!!
        albums.addAll(albumDB.albumDao().getAlbums())
    }

    private fun initDummyAlbums(){
        val albumDB = SongDatabase.getInstance(requireActivity())!!
        val albums = albumDB.albumDao().getAlbums()

        if(albums.isNotEmpty()) return

        albumDB.albumDao().insertAlbum(
            Album(
                0,
                "IU 5th Album 'LILAC'",
                "아이유 (IU)",
                R.drawable.img_album_exp2
            )
        )

        albumDB.albumDao().insertAlbum(
            Album(
                1,
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp
            )
        )

        albumDB.albumDao().insertAlbum(
            Album(
                2,
                "iScreaM Vol.10: Next Level Remixes",
                "에스파 (AESPA)",
                R.drawable.img_album_exp2
            )
        )

        albumDB.albumDao().insertAlbum(
            Album(
                3,
                "GREAT!",
                "모모랜드",
                R.drawable.img_album_exp5
            )
        )

        albumDB.albumDao().insertAlbum(
            Album(
                4,
                "Weekend",
                "태연 (TAEYEON)",
                R.drawable.img_album_exp6
            )
        )

    }
}