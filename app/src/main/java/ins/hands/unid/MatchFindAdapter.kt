package ins.hands.unid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ins.hands.unid.data.MatchingStatusData
import ins.hands.unid.databinding.MatchCardWaitApplyBinding

class MatchFindAdapter(
    val getImageInfo: (image : ImageView,url : String) -> Unit,
    val getPlaceInfo:(bind : MatchCardWaitApplyBinding, id : String)->Unit,
val joinMatch:(id:Int)->Unit) : RecyclerView.Adapter<MatchFindAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : MatchCardWaitApplyBinding, val context : Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : MatchingStatusData){
            binding.apply{
                binding.matchApplyButton.setOnClickListener {
                    joinMatch(data.id)
                }
                getImageInfo(binding.profileCardPhoto,data.joinRequests.guest.profile_url)
                getPlaceInfo(binding,data.place_id)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.match_card_wait_apply,parent,false),parent.context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
    var dataList = mutableListOf<MatchingStatusData>()
}