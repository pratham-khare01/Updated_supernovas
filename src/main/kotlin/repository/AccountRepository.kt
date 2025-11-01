package au.com.supernovae.repository

import au.com.supernovae.model.Account
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.UUID

interface AccountRepository : CoroutineCrudRepository<Account, UUID> {
	suspend fun findAccountByEmail(email: String): Account

	@Query("""
		insert into account(
			email_address,
			full_name,
			password_hash
		) values (
			:emailAddress,
			:full_name,
			:password_hash
		)
		returning *
	""")
	suspend fun createAccount(
		email: String,
		fullName: String,
		passwordHash: String
	): Account
}
