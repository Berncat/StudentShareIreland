package ie.wit.studentshareireland.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.databinding.ActivitySearchBinding
import ie.wit.studentshareireland.main.MainApp
import ie.wit.studentshareireland.models.Coordinates
import ie.wit.studentshareireland.models.InstitutionModel
import timber.log.Timber

class Search : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var institutuion: InstitutionModel
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Search Activity Started")
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        setUpSpinner()
        setUpAmountPicker()
        setUpSearchButton()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create -> {
                val launcherIntent = Intent(this, Create::class.java)
                startActivity(launcherIntent)
            }
            R.id.action_list -> {
                val launcherIntent = Intent(this, Listing::class.java)
                startActivity(launcherIntent)
            }
            R.id.action_sign_out -> {
                app.users.signOut()
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpSpinner() {
        val institutions = app.institutions.findAll()
        val spinner = binding.spinner
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, institutions)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                institutuion = institutions[position]
                binding.amountPickerText.isVisible = position != 0
                binding.amountPicker.isVisible = position != 0
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                institutuion = institutions[0]
            }
        }
    }

    private fun setUpAmountPicker() {
        val distances = resources.getStringArray(R.array.distance)
        val amountPicker = binding.amountPicker
        amountPicker.displayedValues = distances
        amountPicker.minValue = 1
        amountPicker.maxValue = distances.size
    }

    private fun setUpSearchButton() {
        val button = binding.searchButton
        val amountPicker = binding.amountPicker
        val zoomMap = mapOf(
            1 to Pair(14f, 1000.0),
            2 to Pair(13f, 2000.0),
            3 to Pair(12.4f, 3000.0),
            4 to Pair(12f, 4000.0),
            5 to Pair(11.6f, 5000.0),
            6 to Pair(10.6f, 10000.0),
            7 to Pair(10f, 15000.0),
            8 to Pair(9.6f, 20000.0),
            9 to Pair(9.3f, 25000.0),
            10 to Pair(9f, 30000.0),
            11 to Pair(8.6f, 40000.0),
            12 to Pair(8.3f, 50000.0)
        )
        button.setOnClickListener {
            var (zoom, radius) = zoomMap[amountPicker.value]!!
            if (institutuion.title == "All"){
                zoom = 6.5f
                radius = 0.0
            }
            val location =
                Coordinates(institutuion.lat, institutuion.lng, zoom, radius)
            val launcherIntent =
                Intent(this, Map::class.java).putExtra("institution", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { Timber.i("Map Loaded") }
    }
}