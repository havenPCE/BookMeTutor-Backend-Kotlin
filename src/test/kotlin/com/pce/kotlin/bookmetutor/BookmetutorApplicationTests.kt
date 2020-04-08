package com.pce.kotlin.bookmetutor

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertEquals

@SpringBootTest
class BookmetutorApplicationTests {

    @Autowired
    lateinit var adminRepo: AdminRepo


    @Test
    fun `Test for AdminRepo Methods`() {
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

}
