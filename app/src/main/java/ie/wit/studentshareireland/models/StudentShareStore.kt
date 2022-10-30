package ie.wit.studentshareireland.models

interface StudentShareStore {
    fun findAll(): List<StudentShareModel>
    fun findUsersOnly(id: Long): List<StudentShareModel>
    fun create(studentShare: StudentShareModel)
    fun update(studentShare: StudentShareModel)
    fun delete(studentShare: StudentShareModel)
    fun findOne(id: Long): StudentShareModel?
}

interface InstitutionStore {
    fun findAll(): List<InstitutionModel>
}

interface UserStore {
    fun create(user: UserModel)
    fun checkIfActiveUserExists() : Boolean
    fun getActiveUser() : UserModel?
    fun checkIfUserExists(email: String) : Boolean
    fun authenticate (email: String, password: String): Pair<Boolean, String>
    fun signOut()
}