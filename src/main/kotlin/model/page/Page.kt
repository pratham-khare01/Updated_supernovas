package au.com.supernovae.model.page

import au.com.supernovae.model.document.*
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import java.time.LocalDateTime
import java.util.UUID

data class FullPageDraftRow(
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
	@Column("page_route")
	val pageRoute: String
)

@Table("page_draft")
data class PageDraft(
	@Id
	@Column("document_id")
	val documentId: UUID,
	@Column("page_route")
	val pageRoute: String
)

data class PageDraftInsertion(
	val pageRoute: String
)

data class FullPageDraftG<TFullDocumentDraft, TPageDraft>(
	val pageDraft: TPageDraft,
	val fullDocumentDraft: TFullDocumentDraft
)
