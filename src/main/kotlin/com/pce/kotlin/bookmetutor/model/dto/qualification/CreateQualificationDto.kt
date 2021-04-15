package com.pce.kotlin.bookmetutor.model.dto.qualification

data class CreateQualificationDto(
    val degree: String,
    val university: String,
    val percentile: Double
)