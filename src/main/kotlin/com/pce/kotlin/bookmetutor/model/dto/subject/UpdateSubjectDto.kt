package com.pce.kotlin.bookmetutor.model.dto.subject

data class UpdateSubjectDto(
        val classNumber: Int?,
        val subjectName: String?,
        val topics: List<String>?
)
