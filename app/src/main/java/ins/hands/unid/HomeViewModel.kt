package ins.hands.unid

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ins.hands.unid.data.PlaceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.HttpURLConnection
import java.net.URL

class HomeViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()

    var placeList = MutableLiveData<MutableList<PlaceData>>()

    fun getHotPlace(placeTheme : Int = -1){
        viewModelScope.launch {
            if(placeTheme==-1)
            dataSource.getHotPlace().apply{
                placeList.value = this.data.filter{true}.toMutableList()
            }
            else{
                val type = when(placeTheme){
                    0->"식당"
                    1->"카페"
                    2->"술집"
                    3->"파티"
                    else->""
                }
                dataSource.getPlaceType(type).apply{
                    placeList.value=data
                }
            }
        }
    }
    fun bindImage(url : String, imageView : ImageView){
        viewModelScope. launch{

            val imgUrl = URL(url)
            val connection = imgUrl.openConnection() as HttpURLConnection
            connection.setDoInput(true) //url로 input받는 flag 허용
            CoroutineScope(Dispatchers.IO).launch {
                connection.connect() //연결

                val inputStream = connection.getInputStream() // get inputstream
                val decode = BitmapFactory.decodeStream(inputStream)
                CoroutineScope(Dispatchers.Main).launch {  imageView.setImageBitmap(decode) }

                Log.d("UrlToBitmap", "Finished")
            }
        }
    }
}