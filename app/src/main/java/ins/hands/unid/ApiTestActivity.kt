package ins.hands.unid


import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import android.util.Log
import ins.hands.unid.databinding.ActivityApiTestBinding

class ApiTestActivity : BaseActivity() {
    val viewModel : ApiTestViewModel by viewModel()
    val bind by binding<ActivityApiTestBinding>(R.layout.activity_api_test)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)
        Log.d("ApiTestActivity","Activity On")
        bind.apply{
            btGetHotPlace.setOnClickListener {
                Log.d("ApiTestActivity","GetHotPlace Click")
                viewModel.getHotPlace()
            }
        }

    }
}