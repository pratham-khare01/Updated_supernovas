package au.com.supernovae.repository

import au.com.supernovae.model.document.*
import io.r2dbc.postgresql.codec.Json
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

data class DocumentDraftUpdateRow(
	@Column("error_code")
	val errorCode: String
)

data class DocumentDraftDelete(
	@Column("error_code")
	val errorCode: String,
	@Column("document_id")
	val documentId: UUID
)

@Repository
interface DocumentDraftRepository : CoroutineCrudRepository<DocumentDraft, String> {
	@Query("""
			with is_published as (
					select
							case when exists (select 1 from published_document where document_draft_id = :documentId) then true
							else false
					end as v
			), updated_document_draft as (
					update document_draft
					set updated_date = :updatedDate
					where document_id = :documentId and (select v from is_published) = false
					returning document_id
			), updated_document as (
					update document
					set document_data = :documentData
					where document_id in (select document_id from document_draft)
					returning document_id
			), error_code as (
					select case
							when (select v from is_published) then 'is_published'
							when not exists (select 1 from updated_document_draft) then 'not_exists'
							else 'success'
					end as v
			)
			select
					error_code.v as error_code
			from
					error_code
		"""
	)
	suspend fun updateDocumentDraft(documentId: UUID, documentData: Json, updatedDate: LocalDateTime): DocumentDraftUpdateRow

	@Query("""
		select
				account.user_id,
				account.full_name,
				account.email_address,
				draft_for_route.document_id,
				draft_for_route.updated_date,
				draft_for_route.created_date
		from
				page
				join document_draft draft_for_route on page.document_id = draft_for_route.based_on_draft_id
				join account on draft_for_route.author_user_id = account.user_id
		where
				page.page_route = :pageRoute
		order by
				draft_for_route.updated_date desc,
				account.full_name
	""")
	fun listUsersEditingRoute(pageRoute: String): Flow<DraftEditingUser>

	@Query("""
		select
				account.user_id,
				account.full_name,
				account.email_address,
				draft_for_route.document_id,
				draft_for_route.updated_date,
				draft_for_route.created_date
		from
				simple_document
				join document_draft draft_for_route on simple_document.document_id = draft_for_route.based_on_draft_id
				join account on draft_for_route.author_user_id = account.user_id
		where
				simple_document.document_entity_name = :documentEntityName
		order by
				draft_for_route.updated_date desc,
				account.full_name
	""")
	fun listUsersEditingSimpleDocument(documentEntityName: String): Flow<DraftEditingUser>

	@Query("""
		with document_info as (
				select
						document_draft.document_id,
						case
								when published_document.document_draft_id is null then 'success'
								else 'is_published'
						end as error_code
				from document_draft
						left join published_document on document_draft.document_id = published_document.document_draft_id
				where document_id = :documentId
				union all
				select
						:documentId, 'not_exists'
				where
						not exists (select 1 from document_draft where document_id = :documentId)
		), deleted_document_draft as (
				delete from document_draft
				using document
				where
					document_draft.document_id in (select document_id from document_info where document_info.error_code = 'success') and
					document.document_id = document_draft.document_id
				returning document_draft.document_id
		)
		select *
		from document_info
	""")
	suspend fun deleteDocumentDraft(documentId: UUID): DocumentDraftDelete
}
