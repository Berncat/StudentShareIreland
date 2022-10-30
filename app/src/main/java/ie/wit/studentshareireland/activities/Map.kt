package ie.wit.studentshareireland.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.databinding.ActivityMapBinding
import ie.wit.studentshareireland.main.MainApp
import ie.wit.studentshareireland.models.Coordinates
import timber.log.Timber

class Map : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding
    var institution = Coordinates()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Map Activity Started")
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        institution = intent.extras?.getParcelable<Coordinates>("institution")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapSearch) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // create circle
        val loc = LatLng(institution.lat, institution.lng)
        val circle = CircleOptions()
            .center(loc)
            .radius(institution.radius)
        map.addCircle(circle)
        // create markers
        app.studentShares.findAll().forEach() {
            val locc = LatLng(it.lat, it.lng)
            val options = MarkerOptions()
                .title(it.street + "   €" + it.cost)
                .snippet("ID: " + it.id)
                .position(locc)
            map.addMarker(options)
        }
        map.setOnMarkerClickListener(this)
        map.setOnInfoWindowClickListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, institution.zoom))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onInfoWindowClick(marker: Marker) {
        val id = marker.snippet!!.replace("ID: ", "").toLong()
        val studentShare = app.studentShares.findOne(id)
        val launcherIntent = Intent(this, ViewItem::class.java)
        launcherIntent.putExtra("view", studentShare)
        startActivity(launcherIntent)
        Toast.makeText(applicationContext, studentShare.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}