package ins.hands.unid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import ins.hands.unid.data.PlaceData
import ins.hands.unid.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import androidx.core.content.ContextCompat

import android.graphics.drawable.Drawable

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.model.BitmapDescriptor
import java.lang.IllegalArgumentException


class HomeActivity : BaseActivity(), OnMapReadyCallback {
    val bind by binding<ActivityHomeBinding>(R.layout.activity_home)
    val viewModel : HomeViewModel by viewModel()
    lateinit var mapFragment: SupportMapFragment
    lateinit var menuFragment : HomeMenuFragment
    lateinit var imageAdapter : ImageRecyclerAdapter

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        Log.d("HomeActivity","map내놔라")
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
//        Log.d("HomeActivity","map내놔라")
        mapFragment.getMapAsync(this)

//        Log.d("HomeActivity","map내놔라")
        findViewById<ImageView>(R.id.bt_centor).setOnClickListener {
            setMapLocation(googlemap!!)
            Log.d("HomeActivity","RequestCentor")
        }
        imageAdapter = ImageRecyclerAdapter(findViewById(R.id.big_image),findViewById(R.id.big_image_layout)){image, url ->
            viewModel.bindImage(url,image)
        }

        findViewById<RecyclerView>(R.id.image_recylcer).adapter = imageAdapter
        findViewById<View>(R.id.drawer_bt).setOnClickListener { openFrag() }
        fusedLocationClient = FusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(LocationRequest(), object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                location = p0?.lastLocation
                super.onLocationResult(p0)
            }
            }, Looper.getMainLooper())
        findViewById<View>(R.id.menu_fragment).visibility=View.GONE
        observePlaceData()
        observeMapTheme()
        findViewById<View>(R.id.bt_gotoList).setOnClickListener {
            if(currentMarker!=null)
            startActivity(Intent(this,MatchWaitActivity::class.java).apply{
                putExtra("placeId",viewModel.placeList.value!!.find{it.name==currentMarker!!.title}!!.id)
            })
        }
        bindNavigationBar()
        bitmapMarker()
        menuFragment = HomeMenuFragment()



        viewModel.getHotPlace(-1)
    }

    val mapTheme = MutableLiveData(-1)
    var btList =mutableListOf<View>()
    var location : Location? = null
    lateinit var fusedLocationClient : FusedLocationProviderClient
    var locationX = 0f
    var locationY = 0f

    var googlemap : GoogleMap? = null
    override fun onMapReady(map: GoogleMap?) {
        map?.setOnCameraMoveListener {
            if(map?.cameraPosition.zoom<13f)
            {
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(map?.cameraPosition.target,13f))
            }
        }
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.4684266 , 126.93244),15f))
        googlemap = map
        Log.d("HomeActivity","map ready")
        setMapLocation(map!!)
        map.setOnMapClickListener { closeInfo() }
        map.setOnMarkerClickListener {
            try {
                if (currentMarker != null) currentMarker?.setIcon(markerSmall)
            }
            catch(e:IllegalArgumentException){
                e.printStackTrace()
            }

            currentMarker = it
            it.setIcon(markerBig)


            findViewById<View>(R.id.info_layout).visibility=View.VISIBLE
            findViewById<TextView>(R.id.tv_title).setText(it.title)
            var address = viewModel.placeList.value?.find{it.name==currentMarker?.title}?.address
            if(address?.startsWith("대한민국")==true) address = address.substring(5)
            findViewById<TextView>(R.id.tv_address).setText(address)
            true

        }
    }
    fun openFrag(){
        findViewById<View>(R.id.menu_fragment).visibility=View.VISIBLE
        findViewById<View>(R.id.frag_shade).apply{
            visibility=View.VISIBLE
            setOnClickListener {
                closeFrag()
            }
        }
    }
    fun closeFrag(){
        findViewById<View>(R.id.menu_fragment).visibility=View.GONE
        findViewById<View>(R.id.frag_shade).visibility=View.GONE
    }
    @SuppressLint("MissingPermission")
    fun setMapLocation(map : GoogleMap)
    {

        if(location==null) return
        Log.d("HomeActivity","Location ${location!!.latitude} / ${location!!.longitude}")
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude,location!!.longitude),15f))
    }
    fun observePlaceData(){
        viewModel.placeList.observe(this,{
            googlemap?.clear()
            it.forEach {
                googlemap?.addMarker(markerBind(it))
            }
        })
    }
    var markerSmall : BitmapDescriptor? = null
    var markerBig : BitmapDescriptor? = null
    fun bitmapMarker(){
        markerSmall = bitmapDescriptorFromVector(this@HomeActivity,R.drawable.ic_baseline_place_24)
        markerBig = bitmapDescriptorFromVector(this@HomeActivity,R.drawable.ic_baseline_place_32)

    }
    fun markerBind(data : PlaceData) : MarkerOptions{
        return MarkerOptions().apply{
            position(LatLng(data.latitude,data.longitude))
            title(data.name)
            try {

                icon(markerSmall)
            }catch(e:Exception)
            {
                e.printStackTrace()
            }

        }
    }
    var currentMarker : Marker? = null

    fun closeInfo() {
        try {
            if (currentMarker != null) currentMarker?.setIcon(markerSmall)
        }
        catch(e:Exception)
        {
            e.printStackTrace()
        }
        findViewById<View>(R.id.info_layout).visibility = View.GONE
    }

    override fun onBackPressed() {
        if(findViewById<View>(R.id.frag_shade).visibility==View.VISIBLE) closeFrag()
        else if(findViewById<View>(R.id.info_layout).visibility == View.VISIBLE)
            closeInfo()
        else finishAffinity()
    }
    val colorOnCheck = Color.parseColor("#ffbeaed8")
    val colorDisCheck = Color.parseColor("#4dbeaed8")
    fun observeMapTheme(){
        mapTheme.observe(this,{
            viewModel.getHotPlace(it)
        })
    }

    fun bindNavigationBar(){
        btList.add(findViewById(R.id.btn_bottom_navi_home_tab))
        btList.add(findViewById(R.id.btn_bottom_navi_home_cafe))

        btList.add(findViewById(R.id.btn_bottom_navi_home_liquor))
        btList.add(findViewById(R.id.btn_bottom_navi_home_celeb))

        btList.forEach {
            val pos = btList.indexOf(it)
            it.background.alpha=0
            it.setOnClickListener {
                btList.forEach { it.background.setTint(colorDisCheck) }
                if(mapTheme.value==pos) mapTheme.value= -1
                else {
                    mapTheme.value = pos
                   // it.background.setTint(colorOnCheck)
                }
            }
        }
    }
    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        val ratio = 1.5
        vectorDrawable!!.setBounds(
            0,
            0,
            (vectorDrawable.intrinsicWidth*ratio).toInt(),
            (vectorDrawable.intrinsicHeight*ratio).toInt()
        )
        val bitmap = Bitmap.createBitmap(
            (vectorDrawable.intrinsicWidth*ratio).toInt(),
            (vectorDrawable.intrinsicHeight*ratio).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.alpha=255
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}