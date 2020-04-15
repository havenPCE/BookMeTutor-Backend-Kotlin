package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import com.pce.kotlin.bookmetutor.repository.SubjectRepo
import com.pce.kotlin.bookmetutor.service.SubjectService
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcSubjectService(val subjectRepo: SubjectRepo) : SubjectService {
    override fun retrieveAllSubjects(): List<SubjectDto> {
        return subjectRepo.findAll().map { it.toDto() }
    }

    override fun retrieveSubject(classNumber: Int, subjectName: String): SubjectDto? {
        return subjectRepo.findBySubjectNameAndClassNumber(SubjectName.valueOf(subjectName), classNumber)?.toDto()
    }

    override fun createSubject(dto: CreateSubjectDto): SubjectDto? {
        return subjectRepo.save(Subject.fromDto(dto))?.toDto()
    }

    override fun removeSubject(id: Long): Boolean {
        return subjectRepo.deleteById(id)
    }

    override fun updateSubject(id: Long, dto: UpdateSubjectDto): SubjectDto? {
        return subjectRepo.findById(id)?.let {
            subjectRepo.update(Subject.fromDto(dto, it))?.toDto()
        }
    }

}