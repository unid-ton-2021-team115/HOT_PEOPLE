package ins.hands.unid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.annotations.SchedulerSupport.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class HomeMenuViewModel : ViewModel(), KoinComponent {

    val myMatchingCnt = MutableLiveData(0)
    val watingMatchingCnt = MutableLiveData(0)
    val joinMatchingCnt = MutableLiveData(0)
    val dataSource : RemoteDateSourcePlace by inject()
    fun getMatchingCnt(){
        viewModelScope.launch{
            dataSource.getMyMatching().apply{
                myMatchingCnt.value = data.size
            }
        }
    }
    val profileImage : MutableLiveData<Bitmap> = MutableLiveData(null)
    fun getProfileImage(url : String){
        viewModelScope. launch{

                val imgUrl = URL(url)
            val connection = imgUrl.openConnection() as HttpURLConnection
            connection.setDoInput(true) //url로 input받는 flag 허용
            CoroutineScope(Dispatchers.IO).launch {
                connection.connect() //연결

                val inputStream = connection.getInputStream() // get inputstream
                val decode = BitmapFactory.decodeStream(inputStream)
                viewModelScope.launch {  profileImage.value = decode }

                Log.d("UrlToBitmap", "Finished")
            }
        }
    }


}