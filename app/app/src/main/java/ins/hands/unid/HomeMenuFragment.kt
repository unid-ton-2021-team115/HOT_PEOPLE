package ins.hands.unid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ins.hands.unid.MyApplication.Companion.prefs
import ins.hands.unid.databinding.FragmentHomeMenuBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeMenuFragment : Fragment() {

    val viewModel : HomeMenuViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var bind : FragmentHomeMenuBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        bind = FragmentHomeMenuBinding.inflate(inflater,container,false)

        viewModel.getProfileImage(prefs.getString("user_profile_url","")!!)
        bind?.apply{

            tvAge.setText("${prefs.getString("user_age_range","20대")}(${prefs.getString("user_gender","무성")})")

            btMyMatching.setOnClickListener {
                startActivity(Intent(context,MyMatchingActivity::class.java))
            }
            btMatchWait.setOnClickListener {
                startActivity(Intent(context,MatchWaitActivity::class.java).apply{
                    putExtra("adapterMode",1)
                })
            }
            btMatchMakeup.setOnClickListener {
                startActivity(Intent(context,MatchWaitActivity::class.java).apply{
                    putExtra("adapterMode",2)
                })
            }
        }
        observeProfileImage()
        viewModel.getMatchingCnt()
        viewModel.myMatchingCnt.observe(viewLifecycleOwner,{
            bind?.matchCreate = it.toString()
        })

        Log.d("Fragment","${bind?.root?.width}")
        return bind?.root
    }

    fun observeProfileImage()
    {
        viewModel.profileImage.observe(viewLifecycleOwner,{
            bind?.profileImage?.setImageBitmap(it)
        })
    }
}