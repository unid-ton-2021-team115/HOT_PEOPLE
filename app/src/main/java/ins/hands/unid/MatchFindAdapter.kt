package ins.hands.unid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ins.hands.unid.MyApplication.Companion.prefs
import ins.hands.unid.data.MatchingStatusData
import ins.hands.unid.databinding.MatchCardWaitApplyBinding

class MatchFindAdapter(
    val getImageInfo: (image : ImageView,url : String) -> Unit,
    val getPlaceInfo:(bind : MatchCardWaitApplyBinding, id : String)->Unit,
val joinMatch:(id:Int)->Unit) : RecyclerView.Adapter<MatchFindAdapter.ViewHolder>() {
    var requestRefresh : (()->Unit)? = null
    lateinit var cancelMatch : (id : Int) -> Unit
    inner class ViewHolder(val binding : MatchCardWaitApplyBinding, val context : Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : MatchingStatusData){
            binding.apply{
                binding.matchApplyButton.setOnClickListener {
                    joinMatch(data.id)
                }
                if(type==2) hostView.visibility=View.GONE
                getImageInfo(profileCardPhoto,data.host.profile_url)
                profileCardName.setText(data.host.nickname)
                profileCardAge.setText(data.host.age_range)
                profileCardGender.setText(data.host.gender)
                time=data.matching_datetime
                if(titleShow) {
                    getPlaceInfo(binding, data.place_id)
                }
                else placeAddress = data.description
                if(data.joinRequests.filter { it.guest.id ==prefs.getInt("user_id",0)  }.size>0) {
                    matchCancelBt.visibility = View.VISIBLE
                    matchApplyButton.visibility=View.GONE
                    matchCancelBt.setOnClickListener {
                        cancelMatch(data.joinRequests.filter { it.guest.id ==prefs.getInt("user_id",0)  }[0].id)
                    }
                }
                else{
                    matchCancelBt.visibility=View.GONE
                    matchApplyButton.visibility=View.VISIBLE

                }
                matchApplyButton.setOnClickListener {
                    Toast.makeText(context,"요청이 전송되었습니다.",Toast.LENGTH_LONG).show()
                    joinMatch(data.id)
                    matchCancelBt.visibility=View.VISIBLE
                    matchApplyButton.visibility=View.GONE
                }
            }

        }
    }
    var titleShow = false
    var type = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.match_card_wait_apply,parent,false),parent.context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
    var dataList = mutableListOf<MatchingStatusData>()
}