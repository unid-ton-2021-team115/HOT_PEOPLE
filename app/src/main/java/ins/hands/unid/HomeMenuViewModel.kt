package ins.hands.unid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

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
}