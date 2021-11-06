package ins.hands.unid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.WindowManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import ins.hands.unid.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


var TOKEN : String = ""
class MainActivity : BaseActivity() {
    val TAG = "MainActivity"
    val bind by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel : MainViewModel  by viewModel()
    var hash = ""
    var telnum=""

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        requestPermission {  }
        super.onCreate(savedInstanceState)
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telnum = tm.line1Number
        Log.d("TelephonNumber","${telnum}")
        setContentView(R.layout.activity_main)
        hash=Utility.getKeyHash(this)
        bind.apply{
            kakaoSignInBtn.setOnClickListener {
                if(UserApiClient.instance.isKakaoTalkLoginAvailable(this@MainActivity))
                kakaoLogin()
                else KakaoLogin2()
            }
        }
    }

    private fun KakaoLogin2() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                TOKEN = token.accessToken

                viewModel.getUserData(token.accessToken) {
                    runOnUiThread {
                        startActivity(Intent(this, HomeActivity::class.java).apply {

                        })
                    }
                }
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
                TOKEN = token.accessToken

                viewModel.getUserData(token.accessToken) {
                    runOnUiThread {
                        startActivity(Intent(this, HomeActivity::class.java).apply {

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
            .setPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_PHONE_NUMBERS,
                android.Manifest.permission.READ_PHONE_STATE
            )
            .check()
    }
}