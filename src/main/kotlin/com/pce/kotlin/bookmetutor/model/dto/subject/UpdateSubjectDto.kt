package com.pce.kotlin.bookmetutor.model.dto.subject

data class UpdateSubjectDto(
    val classNumber: Int? = null,
    val subjectName: String? = null,
    val topics: List<String>? = null
)
