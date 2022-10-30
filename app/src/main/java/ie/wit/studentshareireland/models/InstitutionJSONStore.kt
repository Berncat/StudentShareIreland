package ie.wit.studentshareireland.models

import android.content.Context
import com.google.gson.reflect.TypeToken
import ie.wit.studentshareireland.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.ArrayList

class InstitutionJSONStore (private val context: Context) : InstitutionStore {
    val JSON_FILE = "institutions.json"
    val listType: Type = object : TypeToken<ArrayList<InstitutionModel>>() {}.type
    var institutions = mutableListOf<InstitutionModel>()

    init {
        deserialize()
    }

    override fun findAll(): MutableList<InstitutionModel> {
        logAll()
        return institutions
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE, asset = true)
        institutions = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        institutions.forEach { Timber.i("$it") }
    }

}