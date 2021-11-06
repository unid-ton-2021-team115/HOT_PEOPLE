package ins.hands.unid

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import ins.hands.unid.databinding.PlaceInfoFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceInfoFragment : Fragment() {


    lateinit var bindMainImage : (url : String) -> Unit

    private val viewModel: PlaceInfoViewModel by viewModel()
    var bind : PlaceInfoFragmentBinding? = null
    lateinit var adapter : ImageRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind =  PlaceInfoFragmentBinding.inflate(inflater,container,false)

        adapter = ImageRecyclerAdapter({
            bindMainImage(it)
        },{image, url ->
            //bindImage(image,url)
        })
        bind?.apply{

            btGotoList.setOnClickListener {
                startActivity(Intent(context,MatchWaitActivity::class.java).apply{
                    putExtra("placeId",placeId)
                })
            }
        }
        return bind?.root
    }

    var placeName =""
    var placeAddress = ""
    var placeId = ""
    var imageUrls = mutableListOf<String>()
    fun setPlace(name : String, address : String, id : String, imageList : MutableList<String> = mutableListOf()){
        placeName = name
        placeAddress = address
        placeId = id
        Log.d("Fragment","${placeAddress} ${placeName} ${id}")
        bind?.apply{
            tvAddress.setText(placeAddress)
            tvTitle.setText(placeName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}