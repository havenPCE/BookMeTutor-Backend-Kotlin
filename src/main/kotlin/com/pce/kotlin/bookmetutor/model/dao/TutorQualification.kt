package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.qualification.CreateQualificationDto
import com.pce.kotlin.bookmetutor.model.dto.qualification.QualificationDto
import kotlin.random.Random

data class TutorQualification(
    val id: Long = Random.nextLong(Long.MAX_VALUE),
    val degree: String,
    val university: String,
    val percentile: Double,
    val tutorId: Long? = null
) {
    fun toDto() = QualificationDto(
        id = this.id,
        degree = this.degree,
        university = this.university,
        percentile = this.percentile
    )

    companion object {
        fun fromDto(dto: CreateQualificationDto) = TutorQualification(
            degree = dto.degree,
            university = dto.university,
            percentile = dto.percentile
        )
    }
}
