package ie.wit.studentshareireland.models

import android.content.Context
import com.google.gson.reflect.TypeToken
import ie.wit.studentshareireland.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.ArrayList

class StudentShareJSONStore(private val context: Context) : StudentShareStore {

    val JSON_FILE = "studentShare.json"
    val listType: Type = object : TypeToken<ArrayList<StudentShareModel>>() {}.type
    var studentShares = mutableListOf<StudentShareModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<StudentShareModel> {
        logAll()
        return studentShares
    }

    override fun create(studentShare: StudentShareModel) {
        studentShare.id = generateRandomId()
        studentShares.add(studentShare)
        serialize()
    }

    override fun update(studentShare: StudentShareModel) {
        val studentSharesList = findAll() as ArrayList<StudentShareModel>
        val foundStudentShare: StudentShareModel? = studentSharesList.find { s -> s.id == studentShare.id }
        if (foundStudentShare != null) {
            foundStudentShare.street = studentShare.street
            foundStudentShare.cost = studentShare.cost
            foundStudentShare.details = studentShare.details
            foundStudentShare.phone = studentShare.phone
            foundStudentShare.image = studentShare.image
        }
        serialize()
    }

    override fun delete(studentShare: StudentShareModel) {
        studentShares.remove(studentShare)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(studentShares, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE, asset = false)
        studentShares = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        studentShares.forEach { Timber.i("$it") }
    }
}