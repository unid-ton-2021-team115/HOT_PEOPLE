package ins.hands.unid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ins.hands.unid.databinding.ItemImageBinding

class ImageRecyclerAdapter(val mainImage : (url:String)->Unit, val imageBind : (image : ImageView, url : String)->Unit) : RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemImageBinding, val context :Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(url : String)
        {
            binding.apply{
                imageBind(image,url)
                image.setOnClickListener {
                    mainImage(url)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_image,parent,false),parent.context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() =dataList.size
    var dataList = mutableListOf<String>()
}