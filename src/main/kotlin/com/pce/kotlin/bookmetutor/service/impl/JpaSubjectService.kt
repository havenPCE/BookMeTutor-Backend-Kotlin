package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import com.pce.kotlin.bookmetutor.repository.SubjectRepo
import com.pce.kotlin.bookmetutor.service.SubjectService
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class JpaSubjectService(val subjectRepo: SubjectRepo) : SubjectService {
    override fun retrieveAllSubjects(): List<Subject>? {
        return subjectRepo.findAll()
    }

    override fun retrieveSubject(classNumber: Int, subjectName: String): Subject? {
        return subjectRepo.findByClassNumberAndSubjectName(classNumber, SubjectName.valueOf(subjectName))
    }

    override fun createSubject(dto: CreateSubjectDto): Subject {
        return subjectRepo.save(Subject.fromDto(dto))
    }

    override fun removeSubject(id: Long) {
        subjectRepo.deleteById(id)
    }

    override fun updateSubject(id: Long, update: UpdateSubjectDto): Subject? {
        subjectRepo.findByIdOrNull(id)?.let {
            return subjectRepo.save(Subject.fromDto(update, it))
        }
        return null
    }
}