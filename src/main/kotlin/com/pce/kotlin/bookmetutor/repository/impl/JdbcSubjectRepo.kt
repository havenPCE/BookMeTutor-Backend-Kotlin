package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.repository.SubjectRepo
import com.pce.kotlin.bookmetutor.util.SubjectName
import com.pce.kotlin.bookmetutor.util.SubjectQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcSubjectRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : SubjectRepo {

    val subjectRowMapper: (ResultSet, Int) -> Subject = { rs, _ ->
        Subject(
                id = rs.getLong("subject_id"),
                subjectName = SubjectName.valueOf(rs.getString("subject_name")),
                classNumber = rs.getInt("class_number"),
                topics = emptySet()
        )
    }
    val topicRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("topic") }

    override fun findById(id: Long): Subject? {
        val (selectFromSubjectQuery, selectFromSubjectParams) = SubjectQuery.selectByIdFromSubject(id)
        val (selectFromTopicQuery, selectFromTopicParams) = SubjectQuery.selectByIdFromTopic(id)

        var subject: Subject? = jdbcTemplate.query(selectFromSubjectQuery, selectFromSubjectParams, subjectRowMapper).firstOrNull()
        subject = subject?.copy(
                topics = jdbcTemplate.query(selectFromTopicQuery, selectFromTopicParams, topicRowMapper).toSet()
        )
        return subject
    }

    override fun findBySubjectNameAndClassNumber(subjectName: SubjectName, classNumber: Int): Subject? {
        val (selectFromSubjectQuery, selectFromSubjectParams) = SubjectQuery.selectBySubjectNameAndClassNumberFromSubject(subjectName, classNumber)

        var subject: Subject? = jdbcTemplate.query(selectFromSubjectQuery, selectFromSubjectParams, subjectRowMapper).firstOrNull()
        subject = subject?.let {
            val (selectFromTopicQuery, selectFromTopicParams) = SubjectQuery.selectByIdFromTopic(it.id)
            it.copy(
                    topics = jdbcTemplate.query(selectFromTopicQuery, selectFromTopicParams, topicRowMapper).toSet()
            )
        }
        return subject
    }

    override fun save(subject: Subject): Subject? {
        val (insertIntoSubjectQuery, insertIntoSubjectParams) = SubjectQuery.insertIntoSubject(subject)
        val insertIntoTopicQuery = SubjectQuery.insertIntoTopic(subject.id)
        val insertIntoTopicParams = createTopicParams(subject.topics)

        return try {
            jdbcTemplate.update(insertIntoSubjectQuery, insertIntoSubjectParams)
            jdbcTemplate.batchUpdate(insertIntoTopicQuery, insertIntoTopicParams)
            findById(subject.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(subject: Subject): Subject? {
        val (updateToSubjectQuery, updateToSubjectParams) = SubjectQuery.updateToSubject(subject)
        val insertIntoTopicQuery = SubjectQuery.insertIntoTopic(subjectId = subject.id)
        val (deleteFromTopicQuery, deleteFromTopicParams) = SubjectQuery.deleteByIdFromTopic(subject.id)
        val insertIntoTopicParams = createTopicParams(subject.topics)

        return try {
            jdbcTemplate.update(updateToSubjectQuery, updateToSubjectParams)
            jdbcTemplate.update(deleteFromTopicQuery, deleteFromTopicParams)
            jdbcTemplate.batchUpdate(insertIntoTopicQuery, insertIntoTopicParams)
            findById(subject.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = SubjectQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findAll(): List<Subject> {
        val selectQuery = "SELECT subject_id, subject_name, class_number, topics FROM public.subject;"
        return jdbcTemplate.query(selectQuery, subjectRowMapper)
    }

    fun createTopicParams(topics: Collection<String>): Array<SqlParameterSource> = SqlParameterSourceUtils.createBatch(topics.map { mutableMapOf("topic" to it) }.toTypedArray())
}