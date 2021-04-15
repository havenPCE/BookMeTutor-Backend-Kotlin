package com.pce.kotlin.bookmetutor.model.dto.subject

data class CreateSubjectDto(
    val classNumber: Int,
    val subjectName: String,
    val topics: List<String>
)