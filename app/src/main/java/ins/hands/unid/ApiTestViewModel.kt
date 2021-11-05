package ins.hands.unid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
class ApiTestViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()


    fun getHotPlace(){
        viewModelScope.launch{
            dataSource.getHotPlace().apply{
                Log.d("ApiTest","${status} ${data.size} ${data[0]}")
            }
        }
    }
}