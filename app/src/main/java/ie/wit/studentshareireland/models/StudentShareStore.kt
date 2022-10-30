package ie.wit.studentshareireland.models

interface StudentShareStore {
    fun findAll(): List<StudentShareModel>
    fun create(studentShare: StudentShareModel)
    fun update(studentShare: StudentShareModel)
    fun delete(studentShare: StudentShareModel)
    fun findOne(id: Long): StudentShareModel?
}

interface InstitutionStore {
    fun findAll(): List<InstitutionModel>
}