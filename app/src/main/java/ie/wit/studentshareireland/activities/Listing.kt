package ie.wit.studentshareireland.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.adapters.ListAdapter
import ie.wit.studentshareireland.adapters.ListListener
import ie.wit.studentshareireland.databinding.ActivityListBinding
import ie.wit.studentshareireland.main.MainApp
import ie.wit.studentshareireland.models.StudentShareModel
import timber.log.Timber
import kotlin.collections.List

class Listing : AppCompatActivity(), ListListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Listing Activity Started")
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadStudentShares()
        registerRefreshCallback()
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

    override fun onCardClick(studentShare: StudentShareModel) {
        val launcherIntent = Intent(this, Create::class.java)
        launcherIntent.putExtra("edit", studentShare)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadStudentShares() }
    }

    private fun loadStudentShares() {
        showStudentShares(app.studentShares.findUsersOnly(app.users.getActiveUser()!!.id))
    }

    fun showStudentShares (studentShares: List<StudentShareModel>) {
        binding.recyclerView.adapter = ListAdapter(studentShares, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}