package ins.hands.unid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ins.hands.unid.data.MatchingStatusData
import ins.hands.unid.databinding.ItemMyGuestBinding
import ins.hands.unid.databinding.ItemMyMatchingBinding

class MyMatchingAdapter(val getProfileImage : (image : ImageView,url : String)->Unit) : RecyclerView.Adapter<MyMatchingAdapter.ViewHolder>() {
    var dataList = mutableListOf<MatchingStatusData>()
    inner class ViewHolder(val binding : ItemMyMatchingBinding, val context:Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : MatchingStatusData){
            binding.apply{


                data.joinRequests.forEach {
                    DataBindingUtil.inflate<ItemMyGuestBinding>(LayoutInflater.from(context),R.layout.item_my_guest,guestLayout,true).apply{
                        info= "${it.guest.nickname} ${it.guest.age} (${it.guest.gender})"
                        getProfileImage(profileImage,it.guest.profile_url)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int =dataList.size
}