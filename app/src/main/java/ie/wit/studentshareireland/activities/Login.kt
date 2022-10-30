package ie.wit.studentshareireland.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ie.wit.studentshareireland.databinding.ActivityLoginBinding
import ie.wit.studentshareireland.main.MainApp
import timber.log.Timber

class Login : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Login Activity Started")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        if (app.users.checkIfActiveUserExists()) {
            val user = app.users.getActiveUser()
            val message = "Welcome back " + user!!.firstName
            startActivity(Intent(this, Search::class.java))
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            finish()
        }

        binding.loginButton.setOnClickListener {
            val (authenticated, message) = app.users.authenticate(binding.loginEmail.text.toString(), binding.loginPassword.text.toString())
            if (authenticated) {
                startActivity(Intent(this, Search::class.java))
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}