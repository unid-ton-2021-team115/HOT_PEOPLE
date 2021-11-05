package ins.hands.unid

import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject

class MainViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()

}