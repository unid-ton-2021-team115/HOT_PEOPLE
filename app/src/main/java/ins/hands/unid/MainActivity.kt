package ins.hands.unid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
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
        requestPermission {  }
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
                viewModel.getUserData(token.accessToken) {
                    runOnUiThread {
                        startActivity(Intent(this,HomeActivity::class.java).apply{

                        })
                    }
                }
            }
        }
    }
    private fun requestPermission(func: () -> Unit) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                func()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                //"PermissionDenied".showShortToast()
                finishAffinity()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\\n\\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
            .check()
    }
}