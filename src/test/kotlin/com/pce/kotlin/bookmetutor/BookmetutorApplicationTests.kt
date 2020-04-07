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
    fun `The admin repo should save the admin and return same entity not null`() {
        val admin = Admin(id = 5000, email = "def@gmail.com", password = "password")
        val updatedAdmin = admin.copy(password = "new-pass")
        assertAll(
                {
                    assertEquals("SAVE METHOD", admin, adminRepo.save(admin))
                    assertEquals("FIND METHOD", admin, adminRepo.findByEmail(admin.email))
                    assertEquals("UPDATE METHOD", updatedAdmin, adminRepo.update(updatedAdmin))
                    assertEquals("FIND_ALL METHOD", true, adminRepo.findAll()?.contains(updatedAdmin))
                    assertEquals("DELETE METHOD 404", false, adminRepo.deleteByEmail("xyz"))
                    assertEquals("DELETE METHOD", true, adminRepo.deleteByEmail(admin.email))
                }
        )
    }

}
