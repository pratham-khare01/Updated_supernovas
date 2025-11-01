package au.com.supernovae.model.simple_document

import au.com.supernovae.model.document.*
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import java.time.LocalDateTime
import java.util.UUID

data class FullSimpleDocumentDraftRow(
	@Column("document_id")
	val documentId: UUID,
	@Column("document_data")
	val documentData: Json,
	@Column("author_user_id")
	val authorUserId: UUID?,
	@Column("based_on_draft_id")
	val basedOnDraftId: UUID?,
	@Column("draft_name")
	val draftName: String,
	@Column("created_date")
	val createdDate: LocalDateTime,
	@Column("updated_date")
	val updatedDate: LocalDateTime,
	@Column("document_entity_name")
	val documentEntityName: String
)

@Table("simple_document_draft")
data class SimpleDocumentDraft(
	@Id
	@Column("document_id")
	val documentId: UUID,
	@Column("document_entity_name")
	val documentEntityName: String
)

data class SimpleDocumentDraftInsertion(
	val documentEntityName: String
)

data class FullSimpleDocumentDraftG<TFullDocumentDraft, TSimpleDocumentDraft>(
	val simpleDocumentDraft: TSimpleDocumentDraft,
	val fullDocumentDraft: TFullDocumentDraft
)
