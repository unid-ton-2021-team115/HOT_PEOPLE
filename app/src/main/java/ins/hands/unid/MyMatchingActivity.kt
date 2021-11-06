package ins.hands.unid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ins.hands.unid.databinding.ActivityMyMatchingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyMatchingActivity : BaseActivity() {
    val viewModel : MatchWaitViewModel by viewModel()
    val bind by binding<ActivityMyMatchingBinding>(R.layout.activity_my_matching)

    var mAdapter : MyMatchingAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_matching)
        mAdapter= MyMatchingAdapter({ image, url ->
            viewModel.getProfileImage(image,url)
        },{bind, id ->
            viewModel.getPlaceDataById(bind,id)
        }).apply{
            delete={

            }
        }
        bind.apply{
            adapter=mAdapter
        }

        viewModel.getMyMatching()
        viewModel.matchingList.observe(this,{
            mAdapter?.dataList = it
            mAdapter?.notifyDataSetChanged()
        })

    }
}