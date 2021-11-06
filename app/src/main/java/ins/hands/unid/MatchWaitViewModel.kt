package ins.hands.unid

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ins.hands.unid.MyApplication.Companion.prefs
import ins.hands.unid.data.MatchingData
import ins.hands.unid.data.MatchingStatusData
import ins.hands.unid.databinding.ActivityMatchWaitBinding
import ins.hands.unid.databinding.ItemMyMatchingBinding
import ins.hands.unid.databinding.MatchCardWaitApplyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.HttpURLConnection
import java.net.URL

class MatchWaitViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()
    var matchingList = MutableLiveData(mutableListOf<MatchingStatusData>())
    fun getMatchByPlace(placeid : String){
        placeId = placeid
        viewModelScope.launch {
            dataSource.getMatchingSearchId(placeid).apply{
                Log.d("Recieved","${data.size} matches")
                matchingList.value=data
            }

        }

    }
    var placeId : String = ""
    fun getProfileImage(image: ImageView, url: String) {
        viewModelScope. launch{

            val imgUrl = URL(url)
            val connection = imgUrl.openConnection() as HttpURLConnection
            connection.setDoInput(true) //url로 input받는 flag 허용
            CoroutineScope(Dispatchers.IO).launch {
                connection.connect() //연결

                val inputStream = connection.getInputStream() // get inputstream
                val decode = BitmapFactory.decodeStream(inputStream)
                CoroutineScope(Dispatchers.Main).launch{
                    image.setImageBitmap(decode)
                }
                Log.d("UrlToBitmap", "Finished")
            }
        }
    }

    fun getPlaceDataById(bind: MatchCardWaitApplyBinding, id: String) {
        viewModelScope.launch {
            dataSource.getPlace(id).apply{
                bind.placeTitle = data.name
                bind.placeAddress = data.address
            }
        }
    }
    fun getPlaceDataById(bind: ActivityMatchWaitBinding, id: String) {
        viewModelScope.launch {
            dataSource.getPlace(id).apply{
                bind.place = data.name
                bind.address = data.address
            }
        }
    }
    fun getPlaceDataById(bind: ItemMyMatchingBinding, id: String) {
        viewModelScope.launch {
            dataSource.getPlace(id).apply{
                bind.placeName = data.name
                bind.address = data.address.substring(4)
            }
        }
    }
    fun joinMatch(id: Int) {
        viewModelScope.launch{
            dataSource.joinMatching(MatchingIdClass(id)).apply{

            }
        }
    }

    fun createMatch(placeId: String, message: String, it: MyTimeData) {
        viewModelScope.launch{
            Log.d("DateFormat","${it.year.t()}-${it.month.t()}-${it.day.t()}T${it.hour.t()}:${it.minute.t()}:00")
            dataSource.createMatching(CreateMatchBody(placeId,"${it.year.t()}-${it.month.t()}-${it.day.t()}T${it.hour.t()}:${it.minute.t()}:00",message)).apply{
            getMatchByPlace(placeId)
            }
        }
    }
    fun Int.t():String{
        return "%02d".format(this)
    }
    val myMatchingList = MutableLiveData(mutableListOf<MatchingData>())
    fun getMyMatching(){
        viewModelScope.launch {
            dataSource.getMyMatching().apply{
                matchingList.value = data
                //Log.d("MyMatching","${data[0].matchiing_datetime}")
            }
        }
    }
    fun deleteMyMatching(id : Int){
        viewModelScope.launch{
            dataSource.closeMatching(id)
            dataSource.getMyMatching().apply{
                matchingList.value = data
            }
        }
    }
    fun makeupMatching(id : Int, joinId : Int)
    {
        viewModelScope.launch{
            dataSource.makeupMatching(id,joinId)
            dataSource.getMyMatching().apply{
                matchingList.value = data
            }
        }
    }

    fun cancelMatch(id : Int){
        viewModelScope.launch {
            dataSource.cancelMatching(id).apply{
            if(placeId =="")
            {

                getMyMatchingWait(prefs.getInt("user_id",0))
            }
            else

                getMatchByPlace(placeId)
            }
        }
    }

    fun getMyMatchingWait(id : Int) {
        viewModelScope.launch{
            dataSource.getMatchingByGuestId(id,"wait").apply{
                var matchings = mutableListOf<MatchingStatusData>()
                data.forEach {
                    dataSource.getMatchingById(it.matching_id).apply{
                       matchings.add(data)
                    }

                }
                matchingList.value = matchings
            }
        }
    }
    fun getMyMatchingMakeup() {
        viewModelScope.launch{
            dataSource.getSuccessMatching().apply{

                matchingList.value = data
            }
        }
    }
}