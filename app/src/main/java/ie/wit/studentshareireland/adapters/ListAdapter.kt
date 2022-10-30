package ie.wit.studentshareireland.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.studentshareireland.databinding.CardStudentShareBinding
import ie.wit.studentshareireland.models.StudentShareModel

interface ListListener {
    fun onCardClick(studentShare: StudentShareModel)
}

class ListAdapter constructor(
    private var studentShares: List<StudentShareModel>,
    private val listener: ListListener
) :
    RecyclerView.Adapter<ListAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardStudentShareBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val studentShare = studentShares[holder.adapterPosition]
        holder.bind(studentShare, listener)
    }

    override fun getItemCount(): Int = studentShares.size

    class MainHolder(private val binding: CardStudentShareBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(studentShare: StudentShareModel, listener: ListListener) {
            binding.shareStreet.text = studentShare.street
            Picasso.get().load(studentShare.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onCardClick(studentShare) }
        }
    }
}