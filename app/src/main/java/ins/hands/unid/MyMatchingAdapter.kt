package ins.hands.unid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ins.hands.unid.data.MatchingStatusData
import ins.hands.unid.databinding.ItemMyGuestBinding
import ins.hands.unid.databinding.ItemMyMatchingBinding
import ins.hands.unid.databinding.MatchCardWaitApplyBinding

class MyMatchingAdapter(val getProfileImage : (image : ImageView,url : String)->Unit,
                        val getPlaceInfo:(bind : ItemMyMatchingBinding, id : String)->Unit) : RecyclerView.Adapter<MyMatchingAdapter.ViewHolder>() {
    var dataList = mutableListOf<MatchingStatusData>()
    lateinit var makeUp : (id : Int, joinId : Int)->Unit
    lateinit var delete : (id : Int)->Unit
    lateinit var cancel : (id : Int)->Unit
    inner class ViewHolder(val binding : ItemMyMatchingBinding, val context:Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : MatchingStatusData){
            binding.apply{
                getPlaceInfo(binding,data.place_id)
                time=data.matching_datetime
                guestLayout.visibility= View.GONE
                btOpen.setOnClickListener {
                    if(guestLayout.visibility==View.GONE) {
                        guestLayout.visibility = View.VISIBLE
                        btTog.visibility = View.VISIBLE
                    }
                    else {
                        guestLayout.visibility = View.GONE
                        btTog.visibility=View.GONE
                    }
                }
                btClose.setOnClickListener {
                    delete(data.id)
                }
                data.joinRequests.forEach {
                    val jid = it.id
                    DataBindingUtil.inflate<ItemMyGuestBinding>(LayoutInflater.from(context),R.layout.item_my_guest,null,true).apply{
                        info= "${it.guest.nickname} ${it.guest.age_range} (${it.guest.gender})"
                        getProfileImage(profileImage,it.guest.profile_url)
                        btAccept.setOnClickListener {
                            makeUp(data.id,jid)
                        }
                        btClose.setOnClickListener {
                            cancel(jid)
                        }

                        binding.guestLayout.addView(this.root)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    =ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_my_matching,parent,false),parent.context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int =dataList.size
}