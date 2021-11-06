package ins.hands.unid

import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlaceInfoViewModel : ViewModel(), KoinComponent {

    val dataSource : RemoteDateSourcePlace by inject()



}