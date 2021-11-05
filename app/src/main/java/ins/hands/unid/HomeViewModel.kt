package ins.hands.unid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ins.hands.unid.data.PlaceData
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()

    var placeList = MutableLiveData<MutableList<PlaceData>>()

    fun getHotPlace(placeTheme : Int = -1){
        viewModelScope.launch {
            dataSource.getHotPlace().apply{
                placeList.value = this.data.filter{true}.toMutableList()
            }
        }
    }
}