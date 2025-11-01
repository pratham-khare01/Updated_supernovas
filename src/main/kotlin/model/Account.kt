package au.com.supernovae.model

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.*
import java.time.LocalDateTime
import java.util.*

@Table("account")
class Account(
	@Id @Column("user_id")
	val userId: UUID = UUID.randomUUID(),
	@Column("email_address")
	var email: String,
	@Column("full_name")
	var fullName: String,
	@Column("password_hash")
	var passwordHash: String,
	@Column("registered_date")
	var registeredDate: LocalDateTime = LocalDateTime.now()
)

@Table("auth2fa")
class Auth2FA(
	@Id @Column("user_id")
	val userId: UUID,
	@Column("secret")
	var secret: String,
	@Column("backup_codes")
	var backupCodes: String
)

@Table("session")
class Session(
	@Id @Column("session_code")
	val sessionCode: UUID = UUID.randomUUID(),
	@Column("user_id")
	var userId: UUID,
	@Column("expiration_date")
	var expirationDate: LocalDateTime? = null,
	@Column("created_date")
	var createdDate: LocalDateTime = LocalDateTime.now()
)
