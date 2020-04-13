package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import com.pce.kotlin.bookmetutor.repository.SubjectRepo
import com.pce.kotlin.bookmetutor.service.SubjectService
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcSubjectService(val subjectRepo: SubjectRepo) : SubjectService {
    override fun retrieveAllSubjects(): List<Subject> {
        return subjectRepo.findAll()
    }

    override fun retrieveSubject(classNumber: Int, subjectName: String): Subject? {
        return subjectRepo.findBySubjectNameAndClassNumber(SubjectName.valueOf(subjectName), classNumber)
    }

    override fun createSubject(dto: CreateSubjectDto): Subject? {
        return subjectRepo.save(Subject.fromDto(dto))
    }

    override fun removeSubject(id: Long): Boolean {
        return subjectRepo.deleteById(id)
    }

    override fun updateSubject(id: Long, update: UpdateSubjectDto): Subject? {
        val subject: Subject? = subjectRepo.findById(id)
        subject?.let {
            return subjectRepo.update(Subject.fromDto(update, subject))
        }
        return null
    }
}