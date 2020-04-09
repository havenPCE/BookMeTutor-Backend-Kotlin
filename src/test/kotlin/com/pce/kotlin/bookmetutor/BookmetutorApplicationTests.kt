package com.pce.kotlin.bookmetutor

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.repository.SubjectRepo
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertEquals

@SpringBootTest
class BookmetutorApplicationTests {

    @Autowired
    lateinit var adminRepo: AdminRepo

    @Autowired
    lateinit var subjectRepo: SubjectRepo


    @Test
    fun `Test For AdminRepo`() {
        val admin = Admin(id = 5000, email = "def@gmail.com", password = "password")
        val updatedAdmin = admin.copy(password = "new-pass")
        val falseAdmin = Admin(id = 420, email = "xyz@gmail.com", password = "password")
        assertAll(
                {
                    assertEquals("SAVE METHOD SUCCESS", admin, adminRepo.save(admin))
                    assertEquals("SAVE METHOD FAILURE", null, adminRepo.save(admin))
                    assertEquals("FIND METHOD SUCCESS", admin, adminRepo.findByEmail(admin.email))
                    assertEquals("FIND METHOD FAILURE", null, adminRepo.findByEmail(falseAdmin.email))
                    assertEquals("UPDATE METHOD SUCCESS", updatedAdmin, adminRepo.update(updatedAdmin))
                    assertEquals("UPDATE METHOD FAILURE", null, adminRepo.update(falseAdmin))
                    assertEquals("DELETE METHOD SUCCESS", true, adminRepo.deleteByEmail(admin.email))
                    assertEquals("DELETE METHOD FAILURE", false, adminRepo.deleteByEmail(falseAdmin.email))
                }
        )
    }

    @Test
    fun `Test For SubjectRepo`() {
        val subject = Subject(id = 3000, subjectName = SubjectName.PHYSICS, classNumber = 8, topics = mutableSetOf("1st", "2nd", "3rd"))
        val updatedSubject = subject.copy(topics = mutableSetOf("new", "topics"))
        val falseSubject = Subject(id = 1111, subjectName = SubjectName.PHYSICS, classNumber = 17, topics = mutableSetOf("false", "data"))
        assertAll(
                {
                    assertEquals("SAVE METHOD SUCCESS", subject, subjectRepo.save(subject))
                    assertEquals("SAVE METHOD FAILURE", null, subjectRepo.save(subject))
                    assertEquals("FIND METHOD SUCCESS", subject, subjectRepo.findById(subject.id))
                    assertEquals("FIND METHOD FAILURE", null, subjectRepo.findById(falseSubject.id))
                    assertEquals("SPECIAL FIND METHOD SUCCESS", subject, subjectRepo.findBySubjectNameAndClassNumber(subject.subjectName, subject.classNumber))
                    assertEquals("SPECIAL FIND METHOD FAILURE", null, subjectRepo.findBySubjectNameAndClassNumber(falseSubject.subjectName, falseSubject.classNumber))
                    assertEquals("UPDATE METHOD SUCCESS", updatedSubject, subjectRepo.update(updatedSubject))
                    assertEquals("UPDATE METHOD FAILURE", null, subjectRepo.update(falseSubject))
                    assertEquals("DELETE METHOD SUCCESS", true, subjectRepo.deleteById(subject.id))
                    assertEquals("DELETE METHOD FAILURE", false, subjectRepo.deleteById(falseSubject.id))
                }
        )
    }

    @Test
    fun `Test For StudentRepo`() {

    }

}
