package ie.wit.studentshareireland.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import ie.wit.studentshareireland.R
import ie.wit.studentshareireland.databinding.ActivityViewItemBinding
import ie.wit.studentshareireland.models.StudentShareModel
import timber.log.Timber


class ViewItem : AppCompatActivity() {
    private lateinit var binding: ActivityViewItemBinding
    var studentShare = StudentShareModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("View Item Activity Started")
        binding = ActivityViewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("view")) {
            studentShare = intent.extras?.getParcelable("view")!!
            binding.streetContent.text = studentShare.street
            binding.costContent.text = (studentShare.cost)
            binding.detailsContent.text = studentShare.details
            binding.phoneContent.text= (studentShare.phone)
            Picasso.get()
                .load(studentShare.image)
                .into(binding.Image)
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
}