package ie.wit.studentshareireland.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.databinding.ActivityRegisterBinding
import ie.wit.studentshareireland.main.MainApp
import ie.wit.studentshareireland.models.UserModel
import timber.log.Timber

class Register : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivityRegisterBinding
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Register Activity Started")
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.registerButton.setOnClickListener {
            if (validateFields()) {
                user.firstName = binding.registerFirstName.text.toString()
                user.surname = binding.registerSurname.text.toString()
                user.email = binding.registerEmail.text.toString()
                user.password = binding.registerPassword.text.toString()
                app.users.create(user.copy())
                finish()
            }
        }
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

    private fun validateFields(): Boolean {
        resetErrors()
        var count = 0
        val emailRegex = Regex("^(.+)@(.+)\$")
        if (binding.registerEmail.length() != 0 && !binding.registerEmail.text.toString().matches(emailRegex)) {
            binding.registerEmailBox.error = "Enter valid email format"
            count += 1
        }
        if (app.users.checkIfUserExists(binding.registerEmail.text.toString())) {
            binding.registerEmailBox.error = "A user with this email already exists"
            count += 1
        }
        if (binding.registerPassword.length() < 6) {
            binding.registerPasswordBox.error = "Password should be at least 6 characters"
            count += 1
        }
        if (binding.registerEmail.length() == 0) {
            binding.registerEmailBox.error = "Required field"
            count += 1
        }
        if (binding.registerFirstName.length() == 0) {
            binding.registerFirstNameBox.error = "Required field"
            count += 1
        }
        if (binding.registerSurname.length() == 0) {
            binding.registerSurnameBox.error = "Required field"
            count += 1
        }
        if (binding.registerPassword.length() == 0) {
            binding.registerPasswordBox.error = "Required field"
            count += 1
        }
        if (count > 0) {
            return false
        }
        return true
    }

    private fun resetErrors() {
        binding.registerFirstNameBox.error = null
        binding.registerSurnameBox.error = null
        binding.registerEmailBox.error = null
        binding.registerPasswordBox.error = null
    }
}