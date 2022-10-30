package ie.wit.studentshareireland.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.databinding.ActivityCreateBinding
import ie.wit.studentshareireland.helpers.showImagePicker
import ie.wit.studentshareireland.main.MainApp
import ie.wit.studentshareireland.models.Coordinates
import ie.wit.studentshareireland.models.StudentShareModel
import timber.log.Timber

class Create : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    lateinit var app: MainApp
    var studentShare = StudentShareModel()
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Create Activity Started")
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        checkIfEdit()
        setUpAddButton()
        setUpLocationButton()
        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                app.studentShares.delete(studentShare)
                val message = "Your share was removed"
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            }
            R.id.action_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpAddButton() {
        binding.addButton.setOnClickListener {
            if (validateFields()) {
                studentShare.street = binding.street.text.toString()
                studentShare.cost = binding.cost.text.toString()
                studentShare.details = binding.details.text.toString()
                studentShare.phone = binding.phone.text.toString()
                val message = if (edit) {
                    app.studentShares.update(studentShare.copy())
                    "Your share was updated"
                } else {
                    val launcherIntent = Intent(this, Listing::class.java)
                    startActivity(launcherIntent)
                    app.studentShares.create(studentShare.copy())
                    "Your share was added"
                }
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setUpLocationButton() {
        binding.locationButton.setOnClickListener {
            val location = Coordinates(53.1424, -7.84, 6.5f, 0.0)
            if (edit) {
                location.lat = studentShare.lat
                location.lng = studentShare.lng
                location.zoom = 17f
                location.radius = 0.0
            }
            val launcherIntent = Intent(this, Location::class.java).putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()
    }

    private fun checkIfEdit() {
        if (intent.hasExtra("edit")) {
            edit = true
            studentShare = intent.extras?.getParcelable("edit")!!
            binding.street.setText(studentShare.street)
            binding.cost.setText(studentShare.cost)
            binding.details.setText(studentShare.details)
            binding.phone.setText(studentShare.phone)
            binding.locationButton.setText(R.string.create_location_update)
            binding.addButton.setText(R.string.create_update)
            Picasso.get()
                .load(studentShare.image)
                .into(binding.Image)
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            studentShare.image = (result.data!!.data!!)
                            Picasso.get()
                                .load(studentShare.image)
                                .into(binding.Image)
                        }
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Coordinates>("location")!!
                            Timber.i("Location == $location")
                            studentShare.lat = location.lat
                            studentShare.lng = location.lng
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun validateFields(): Boolean {
        resetErrors()
        var count = 0
        if (binding.street.length() == 0) {
            binding.streetBox.error = "Required field"
            count += 1
        }
        if (binding.cost.length() == 0) {
            binding.costBox.error = "Required field"
            count += 1
        }
        if (binding.details.length() == 0) {
            binding.detailsBox.error = "Required field"
            count += 1
        }
        if (binding.phone.length() == 0) {
            binding.phoneBox.error = "Required field"
            count += 1
        }
        if (count > 0) {
            return false
        }
        return true
    }

    private fun resetErrors() {
        binding.streetBox.error = null
        binding.costBox.error = null
        binding.detailsBox.error = null
        binding.phoneBox.error = null
    }

    fun imageEvent(view: View) {
        showImagePicker(imageIntentLauncher)
    }
}