package com.supernovae.forms.repository

import com.supernovae.forms.model.FormSubmission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FormRepository : JpaRepository<FormSubmission, Long>
