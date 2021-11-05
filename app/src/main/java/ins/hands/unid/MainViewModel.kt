package ins.hands.unid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ins.hands.unid.MyApplication.Companion.prefs
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject

class MainViewModel : ViewModel(), KoinComponent {
    val dataSource : RemoteDateSourcePlace by inject()


    fun getUserData(token : String, onEnd : ()->Unit){
        viewModelScope.launch {
            dataSource.getUserData(token).apply{
                data.apply {
                    prefs.apply {
                        setInt("user_id", id)
                        setInt("user_likes", likes)
                        setString("user_accessToken", accessToken)
                        setString("user_age_range",ageRange)
                        setString("user_gender",gender)
                        setString("user_profile_url",data.profile_url)
                        setInt("user_age",when(age){null -> 20 else-> age})
                        onEnd()
                    }
                }
                Log.d("MainViewModel","Data : ${data}")
            }
        }
    }
}