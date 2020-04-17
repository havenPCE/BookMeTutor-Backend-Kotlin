package com.pce.kotlin.bookmetutor.model.dto.qualification

data class QualificationDto(
        val id: Long,
        val degree: String,
        val university: String,
        val percentile: Double
)