package ins.hands.unid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import ins.hands.unid.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.kakao.sdk.common.util.Utility

class MainActivity : BaseActivity() {
    val TAG = "MainActivity"
    val bind by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel : MainViewModel  by viewModel()
    var hash = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        hash=Utility.getKeyHash(this)
        bind.apply{
            kakaoSignInBtn.setOnClickListener {
                kakaoLogin()
            }
        }
    }

    private fun kakaoLogin() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패 ${hash}", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                viewModel.getUserData(token.accessToken)
            }
        }
    }
}