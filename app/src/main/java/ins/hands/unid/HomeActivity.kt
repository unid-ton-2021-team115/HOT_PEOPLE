package ins.hands.unid

import android.os.Bundle
import android.os.PersistableBundle

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_home)
    }
}