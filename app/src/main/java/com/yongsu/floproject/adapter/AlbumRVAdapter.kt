package com.yongsu.floproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yongsu.floproject.databinding.ItemAlbumBinding
import com.yongsu.floproject.retrofit.response.AlbumResult
import com.yongsu.floproject.retrofit.response.FloChartAlbums

class AlbumRVAdapter(val context: Context, val albumList: AlbumResult): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(album: FloChartAlbums)   // 아이템 클릭시 수행
        fun onPlayClick(album: FloChartAlbums)   // 재생 버튼 클릭시 수행
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

//    // 아이템 추가
//    fun addItem(album: Album){
//        albumList.add(album)
//        notifyDataSetChanged()  // 데이터가 바뀌었다는 것을 알려줌
//    }
//
//    // 아이템 삭제
//    fun removeItem(position: Int){
//        albumList.removeAt(position)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        //holder.bind(albumList.albums[position])

        if(albumList.albums[position].coverImgUrl == "" || albumList.albums[position].coverImgUrl == null){

        } else {
            Log.d("image",albumList.albums[position].coverImgUrl )
            Glide.with(context).load(albumList.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = albumList.albums[position].title
        holder.singer.text = albumList.albums[position].singer

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList.albums[position])
        }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            mItemClickListener.onPlayClick(albumList.albums[position])
        }
    }

    override fun getItemCount(): Int = albumList.albums.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        val coverImg : ImageView = binding.itemAlbumCoverImgIv
        val title : TextView = binding.itemAlbumTitleTv
        val singer : TextView = binding.itemAlbumSingerTv

//        fun bind(album: FloChartAlbums){
//            binding.itemAlbumTitleTv.text = album.title
//            binding.itemAlbumSingerTv.text = album.singer
//            binding.itemAlbumCoverImgIv.setImageResource(album.coverImgUrl.toInt())
//        }
    }
}