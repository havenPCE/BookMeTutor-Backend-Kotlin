package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import javax.persistence.*

@Entity
@Table(name = "subject")
data class Subject(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "subject_id", unique = true, nullable = false)
        var id: Long?,

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

}