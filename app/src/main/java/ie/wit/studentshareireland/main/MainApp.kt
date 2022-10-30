package ie.wit.studentshareireland.main

import android.app.Application
import ie.wit.studentshareireland.models.*
import timber.log.Timber

class MainApp : Application() {

    lateinit var studentShares: StudentShareStore
    lateinit var institutions: InstitutionStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Starting Application")
        institutions = InstitutionJSONStore(applicationContext)
        users= UserJSONStore(applicationContext)
        studentShares = StudentShareJSONStore(applicationContext)
    }
}