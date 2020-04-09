package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.TutorQualification

interface TutorQualificationRepo {
    fun findById(id: Long): TutorQualification?
    fun save(tutorId: Long, qualification: TutorQualification): TutorQualification?
    fun update(qualification: TutorQualification): TutorQualification?
    fun deleteById(id: Long): Boolean
}