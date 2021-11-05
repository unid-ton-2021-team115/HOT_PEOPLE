package ins.hands.unid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject

class MainViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()


    fun getUserData(token : String){
        viewModelScope.launch {
            dataSource.getUserData(token).apply{

                Log.d("MainViewModel","Data : ${data}")
            }
        }
    }
}