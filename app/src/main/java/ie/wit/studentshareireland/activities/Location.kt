package ie.wit.studentshareireland.activities

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.databinding.ActivityLocationBinding
import ie.wit.studentshareireland.models.Coordinates

class Location : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityLocationBinding
    var location = Coordinates()
    var geocoder = Geocoder(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        location = intent.extras?.getParcelable<Coordinates>("location")!!
        setUpSearch()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Is this the correct location?")
            .snippet("If not please search or drag")
            .draggable(true)
            .position(loc)
        map.addMarker(options)?.showInfoWindow()
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onBackPressed() {
        val message = "Location was not saved"
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_location, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                val resultIntent = Intent()
                resultIntent.putExtra("location", location)
                setResult(Activity.RESULT_OK, resultIntent)
                val message = "Location added"
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
                super.onBackPressed()
            }
            R.id.action_cancel -> {
                val message = "Location was not saved"
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            }
            R.id.action_list -> {
                val launcherIntent = Intent(this, List::class.java)
                startActivity(launcherIntent)
                val message = "Location was not saved"
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


        private fun setUpSearch() {
        binding.searchBox.setEndIconOnClickListener {
            val search = geocoder.getFromLocationName(binding.search.text.toString(), 1)
            if (search != null) {
                if (search.size > 0) {
                    val response = search[0]
                    val loc = LatLng(response.latitude, response.longitude)
                    val options = MarkerOptions()
                        .title("Was the search correct?")
                        .snippet("If not drag to location")
                        .draggable(true)
                        .position(loc)
                    map.addMarker(options)?.showInfoWindow()
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 17f))
                    map.setOnMarkerDragListener(this)
                    map.setOnMarkerClickListener(this)
                    location.lat = response.latitude
                    location.lng = response.longitude
                }
            }
        }
    }

}