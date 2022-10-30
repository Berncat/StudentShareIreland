package ie.wit.studentshareireland.models

import android.content.Context
import com.google.gson.reflect.TypeToken
import ie.wit.studentshareireland.helpers.*
import java.lang.reflect.Type
import java.util.*

class UserJSONStore(private val context: Context) : UserStore {

    private val JSON_FILE = "users.json"
    private val listType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type
    var users = mutableListOf<UserModel>()
    var studentShares = mutableListOf<StudentShareModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }

    override fun checkIfActiveUserExists(): Boolean {
        return users.any { it.active }
    }

    override fun getActiveUser(): UserModel? {
        return users.find { user -> user.active }!!
    }

    override fun authenticate(email: String, password: String): Pair<Boolean, String> {
        var user: UserModel
        var authenticated = Pair( false, "No user exists with this email")
        if (checkIfUserExists(email)) {
            user = getUser(email)!!
            if (user.password.compareTo(password) == 0) {
                user.active = true
                serialize()
                authenticated = Pair(true, "login successful")
            } else {
                authenticated = Pair(false, "Password incorrect")
            }
        }
        return authenticated
    }

    override fun signOut() {
        val user = getActiveUser()!!
        user.active = false
        serialize()
    }

    override fun checkIfUserExists(email: String): Boolean {
        return users.any { email.equals(it.email, true) }
    }

    private fun getUser(email: String): UserModel? {
        return users.find { user -> user.email.equals(email, true) }!!
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE, asset = false)
        users = gsonBuilder.fromJson(jsonString, listType)
    }
}