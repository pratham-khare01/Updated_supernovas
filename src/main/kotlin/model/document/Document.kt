package au.com.supernovae.model.document

import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.*
import java.time.LocalDateTime
import java.util.UUID

@Table("document")
data class Document(
	@Id
	@Column("document_id")
	val documentId: UUID,
	@Column("document_data")
	val documentData: Json
)

@Table("document_draft")
data class DocumentDraft(
	@Id
	@Column("document_id")
	val documentId: UUID,

	@Column("created_date")
	val createdDate: LocalDateTime = LocalDateTime.now(),

	@Column("updated_date")
	val updatedDate: LocalDateTime = LocalDateTime.now(),

	@Column("author_user_id")
	val authorUserId: UUID?,

	@Column("based_on_draft_id")
	val basedOnDraftId: UUID?
)

data class DraftEditingUser(
	@Column("user_id")
	val authorUserId: UUID,
	@Column("full_name")
	val fullName: String,
	@Column("email_address")
	val email: String,

	@Column("document_id")
	val documentId: UUID,
	@Column("created_date")
	val createdDate: LocalDateTime,
	@Column("updated_date")
	val updatedDate: LocalDateTime
)

data class DocumentDraftInsertion(
	val documentId: UUID,
	val authorUserId: UUID?,
	val basedOnDraftId: UUID?,
	val draftName: String
)

data class FullDocumentDraftG<TDocumentDraft, TDocument>(
	val documentDraft: TDocumentDraft,
	val document: TDocument
)
