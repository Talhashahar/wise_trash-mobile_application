package com.msl.talfinalproject

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.bnhp.network.Callback
import com.bnhp.network.NetworkApi
import com.bnhp.network.baseresponses.BaseResponse
import com.bnhp.network.calladaper.errorhandling.TrashWiseExaption
import com.bnhp.network.serverconfigs.ServerConfig
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.msl.talfinalproject.network.api.googlemaps.IGoogleMapsApi
import com.msl.talfinalproject.network.api.googlemaps.models.Directions
import com.msl.talfinalproject.network.api.trashwise.ITrashWiseApi
import com.msl.talfinalproject.network.api.trashwise.models.Destination
import com.msl.talfinalproject.network.api.trashwise.models.Locations
import com.msl.talfinalproject.network.api.trashwise.models.Route
import com.msl.talfinalproject.ui.dialogs.AlertDialog
import com.msl.talfinalproject.utils.MapsController
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.main_activity_view.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    val MY_PERMISSIONS_REQUEST_LOCATION: Int = 5150
    private lateinit var mMap: GoogleMap
    private var mMapsController: MapsController? = null
    private var mLocationManager : LocationManager? = null

    private lateinit  var googleDisposable : Disposable
    private lateinit  var disposable : Disposable

    private var myTrashesLocations : ArrayList<Destination> = ArrayList()
    private lateinit var myDriverStartLocation : String
    private lateinit var mCurrentDestination: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_view)

        NetworkApi.initNetworkApi(ServerConfig.Builder("").build())

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION)

        } else {
            val israel = LatLng(31.771959, 35.217018)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(israel))
            mMap.isMyLocationEnabled = true
            mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    var trashStatus : Int = 2;
                    AlertDialog.createTrashEventDialog(this@MainActivity, object : RadioGroup.OnCheckedChangeListener {
                        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                            if((group?.get(0) as RadioButton).isChecked())
                                trashStatus = 0

                            if((group?.get(1) as RadioButton).isChecked())
                                trashStatus = 1

                            if((group?.get(2) as RadioButton).isChecked())
                                trashStatus = 2
                        }
                    },
                        object : DialogInterface.OnDismissListener {
                            override fun onDismiss(dialog: DialogInterface?) {
                                NetworkApi.getInstance().makeNetworkRequest(NetworkApi.getInstance().getRestApi(ITrashWiseApi::class.java).sendTrashEvent("" + marker.position.latitude + "," + marker.position.longitude,"10", trashStatus),
                                    object : Callback<BaseResponse> {
                                        override fun onSuccess(t: BaseResponse) {

                                        }

                                        override fun onError(e: TrashWiseExaption?) {

                                        }

                                        override fun onEmptyResponse(e: TrashWiseExaption?) {

                                        }
                                    })
                            }
                        })
                    return false
                }
            })
            mMapsController = MapsController(this, mMap)



            runLocations()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val israel = LatLng(31.771959, 35.217018)
                    //mMap.addMarker(MarkerOptions().position(israel).title("Marker in Israel"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(israel))
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.isMyLocationEnabled = true
                    }

                    mMapsController = MapsController(this, mMap)



                    runLocations()
                } else {
                    AlertDialog.createWrongInputDialog(this,
                        "You must accept location permissions!",
                        dismiss = DialogInterface.OnDismissListener {
                        finish();
                    })

                }
                return
            }

        }
    }

    fun runLocations() {


        disposable = NetworkApi.getInstance()
            .makeNetworkRequest(NetworkApi.getInstance().getRestApi(ITrashWiseApi::class.java).getLocations(),
                object : Callback<Locations> {
                    override fun onSuccess(baseResponse: Locations) {
                        try {
                            myDriverStartLocation = "" + baseResponse.driver!!.lat + "," + baseResponse.driver!!.lng

                            count_bins.text = baseResponse.count_bins.toString()
                            total_weight.text = baseResponse.capacity.toString()

                            if(baseResponse.arr.size > 0) {
                                startNewRoute(myDriverStartLocation, "" + baseResponse.arr[0].lat + "," + baseResponse.arr[0].lng)

                                mCurrentDestination = "" + baseResponse.arr[0].lat + "," + baseResponse.arr[0].lng
                                for ((index, value) in baseResponse.arr.withIndex()) {
                                    if(index != 0) {
                                        startNewRoute(mCurrentDestination, "" + value.lat + "," + value.lng)
                                        mCurrentDestination = "" + value.lat + "," + value.lng
                                    }
                                }

                            } else {
                                AlertDialog.createWrongInputDialog(this@MainActivity, "Something goes wrong, Please try later.", dismiss = DialogInterface.OnDismissListener {
                                    finish()
                                })
                            }

                        } catch (e : NullPointerException) {
                            AlertDialog.createWrongInputDialog(this@MainActivity, "Something goes wrong, Please try later.", dismiss = DialogInterface.OnDismissListener {
                                finish()
                            })
                        }

                    }

                    override fun onError(e: TrashWiseExaption) {
                        AlertDialog.createWrongInputDialog(this@MainActivity, "Something goes wrong, Please try later.", dismiss = DialogInterface.OnDismissListener {
                            finish()
                        })
                    }

                    override fun onEmptyResponse(e: TrashWiseExaption) {
                        AlertDialog.createWrongInputDialog(this@MainActivity, "Something goes wrong, Please try later.", dismiss = DialogInterface.OnDismissListener {
                            finish()
                        })
                    }
                })

    }

    fun startNewRoute(start: String, end: String) {
        googleDisposable = NetworkApi.getInstance()
            .makeNetworkRequest(
                NetworkApi.getInstance().getRestApi(IGoogleMapsApi::class.java)
                    .getDirections(start, end, getString(R.string.google_maps_key)),
                object : Callback<Directions> {
                    override fun onSuccess(directions: Directions) {
                        if (directions.status.equals("OK")) {
                            val legs = directions.routes[0].legs[0]
                            val route = Route(
                                legs.start_address,
                                legs.end_address,
                                legs.start_location.lat,
                                legs.start_location.lng,
                                legs.end_location.lat,
                                legs.end_location.lng,
                                directions.routes[0].overview_polyline.points
                            )

                            try {
                                mMapsController!!.setMarkersAndRoute(route)
                            } catch (e: Exception) {
                                toast(e.message.toString())
                            }

                        } else {
                            toast(directions.status)
                        }
                    }

                    override fun onError(e: TrashWiseExaption) {
                        AlertDialog.createWrongInputDialog(this@MainActivity, "Something goes wrong, Please try later.", dismiss = DialogInterface.OnDismissListener {
                            finish()
                        })
                    }

                    override fun onEmptyResponse(e: TrashWiseExaption) {
                        AlertDialog.createWrongInputDialog(this@MainActivity, "Something goes wrong, Please try later.", dismiss = DialogInterface.OnDismissListener {
                            finish()
                        })
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        googleDisposable.dispose()
        disposable.dispose()
    }
}
