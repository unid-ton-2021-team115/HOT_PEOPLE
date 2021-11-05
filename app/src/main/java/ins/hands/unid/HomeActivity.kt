package ins.hands.unid

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import ins.hands.unid.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity(), OnMapReadyCallback {
    val bind by binding<ActivityHomeBinding>(R.layout.activity_home)
    lateinit var mapFragment: SupportMapFragment
    lateinit var locationManager : LocationManager
    lateinit var locationListener : LocationListener
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d("HomeActivity","map내놔라")
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        Log.d("HomeActivity","map내놔라")
        mapFragment.getMapAsync(this)

        Log.d("HomeActivity","map내놔라")
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        findViewById<ImageView>(R.id.bt_centor).setOnClickListener {
            setMapLocation(googlemap!!)
            Log.d("HomeActivity","RequestCentor")
        }
        fusedLocationClient = FusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(LocationRequest(), object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                location = p0?.lastLocation
                super.onLocationResult(p0)
            }
            }, Looper.getMainLooper())


    }
    var location : Location? = null
    lateinit var fusedLocationClient : FusedLocationProviderClient
    var locationX = 0f
    var locationY = 0f

    var googlemap : GoogleMap? = null
    override fun onMapReady(map: GoogleMap?) {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.4684266 , 126.93244),10f))
        googlemap = map
        Log.d("HomeActivity","map ready")
        setMapLocation(map!!)
    }
    @SuppressLint("MissingPermission")
    fun setMapLocation(map : GoogleMap)
    {
        if(location==null) return
        Log.d("HomeActivity","Location ${location!!.latitude} / ${location!!.longitude}")
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude,location!!.longitude),15f))
    }

}