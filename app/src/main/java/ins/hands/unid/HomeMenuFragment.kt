package ins.hands.unid

import android.content.Intent
import android.os.Bundle
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

            tvAge.setText("${prefs.getInt("user_age",20)}(${prefs.getString("user_gender","무성")})")

            btMyMatching.setOnClickListener {
                startActivity(Intent(context,MyMatchingActivity::class.java))
            }
        }
        observeProfileImage()
        viewModel.getMatchingCnt()
        viewModel.myMatchingCnt.observe(viewLifecycleOwner,{
            bind?.matchCreate = it.toString()
        })


        return bind?.root
    }

    fun observeProfileImage()
    {
        viewModel.profileImage.observe(viewLifecycleOwner,{
            bind?.profileImage?.setImageBitmap(it)
        })
    }
}