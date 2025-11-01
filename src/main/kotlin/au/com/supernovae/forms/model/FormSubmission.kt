package com.supernovae.forms.model

import jakarta.persistence.*

@Entity
@Table(name = "form_submissions")
data class FormSubmission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val fullname: String,
    val company: String,
    val role: String? = null,
    val email: String,
    val phone: String? = null,
    val locations: String? = null,
    val chargerTypes: String? = null,
    @Column(length = 2000)
    val description: String? = null,
    val consent: Boolean = false,

    @Column(name = "submitted_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val submittedAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)
