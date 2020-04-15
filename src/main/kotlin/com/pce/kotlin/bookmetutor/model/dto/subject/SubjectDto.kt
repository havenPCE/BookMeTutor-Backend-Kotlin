package com.pce.kotlin.bookmetutor.model.dto.subject

data class SubjectDto(
        val id: Long? = null,
        val classNumber: Int,
        val subjectName: String,
        val topics: List<String>
)