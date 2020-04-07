package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import com.pce.kotlin.bookmetutor.util.SubjectName
import kotlin.random.Random

data class Subject(
        val id: Long = Random.nextLong(Long.MAX_VALUE),
        val subjectName: SubjectName,
        val classNumber: Int,
        val topics: Set<String>
) {
    fun toDto() = SubjectDto(
            id = this.id,
            subjectName = this.subjectName.name,
            classNumber = this.classNumber,
            topics = this.topics.toList()
    )

    companion object {
        fun fromDto(dto: CreateSubjectDto) = Subject(
                subjectName = SubjectName.valueOf(dto.subjectName),
                classNumber = dto.classNumber,
                topics = dto.topics.toSet()
        )

        fun fromDto(dto: UpdateSubjectDto, default: Subject) = Subject(
                id = default.id,
                subjectName = dto.subjectName?.let { SubjectName.valueOf(it) } ?: default.subjectName,
                classNumber = dto.classNumber ?: default.classNumber,
                topics = dto.topics?.toSet() ?: default.topics
        )
    }
}