package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import javax.persistence.*

@Entity
@Table(name = "subject")
data class Subject(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "subject_id", unique = true, nullable = false)
        var id: Long? = null,

        @Column(name = "class", nullable = false)
        var classNumber: Int,

        @Column(name = "subject_name", nullable = false)
        var subjectName: String,

        @ElementCollection
        var topics: Set<String> = emptySet()

) {
    fun toDto(): SubjectDto? = SubjectDto(
            id = this.id ?: -1,
            classNumber = this.classNumber,
            subjectName = this.subjectName,
            topics = this.topics.map { it }
    )

    companion object Util {
        fun fromDto(dto: CreateSubjectDto): Subject = Subject(
                classNumber = dto.classNumber,
                subjectName = dto.subjectName,
                topics = dto.topics.toSet()
        )

        fun fromDto(dto: UpdateSubjectDto, subject: Subject): Subject = subject.copy(
                classNumber = dto.classNumber ?: subject.classNumber,
                subjectName = dto.subjectName ?: subject.subjectName,
                topics = dto.topics?.toSet() ?: subject.topics
        )
    }

}