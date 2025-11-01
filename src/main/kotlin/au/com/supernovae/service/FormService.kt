package au.com.supernovae.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.OffsetDateTime
import java.util.*

@Service
class FormService @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate
) {
    private val mapper = jacksonObjectMapper()

    fun saveForm(formName: String, payload: Any) {
        val json = mapper.writeValueAsString(payload)
        val now = OffsetDateTime.now()
        val id = UUID.randomUUID().toString()
        val sql = "INSERT INTO form_submissions (id, form_name, payload_json, created_at) VALUES (?, ?, ?::jsonb, ?)"
        jdbcTemplate.update(sql, id, formName, json, now)
    }
}
