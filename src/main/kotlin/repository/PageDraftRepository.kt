package au.com.supernovae.repository

import au.com.supernovae.model.document.*
import au.com.supernovae.model.page.*

import io.r2dbc.postgresql.codec.Json
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PageDraftRepository : CoroutineCrudRepository<Document, String> {
	@Query(
		"""
		with new_document as (
			insert into document(document_data)
			values (:documentData)
			returning *
		), new_document_draft as (
			insert into document_draft(document_id, author_user_id, based_on_draft_id, draft_name)
			select new_document.document_id, :authorUserId, :basedOnDraftId, :draftName
    	from new_document
			returning *
		), new_page_draft as (
			insert into page_draft(document_id, page_route)
			select new_document.document_id, :pageRoute
			from new_document
			returning *
		) select
			new_document.document_id,
			new_document.document_data,
			new_document_draft.author_user_id,
			new_document_draft.based_on_draft_id,
			new_document_draft.draft_name,
			new_document_draft.created_date,
			new_document_draft.updated_date,
			new_page_draft.page_route
			from new_document, new_document_draft, new_page_draft
	"""
	)
	suspend fun createFullPageDraft(
		documentData: Json,
		authorUserId: UUID?,
		basedOnDraftId: UUID?,
		draftName: String,
		pageRoute: String
	): FullPageDraftRow

	@Query("""
		select
			page.*,
			document.document_data
		from
			page join document on page.document_id = document.document_id
		where
			page.page_route = :pageRoute
	""")
	suspend fun getPage(pageRoute: String): FullPageDraftRow

	@Query("""
		select true_page_draft.*, document.document_data
		from true_page_draft
			join document on true_page_draft.document_id = document.document_id
		where
			true_page_draft.author_user_id = :userId
		order by
			true_page_draft.page_route,
			true_page_draft.updated_date desc
	""")
	fun listPageDraftsByUser(userId: UUID): Flow<FullPageDraftRow>
	@Query("""
		select true_page_draft.*, document.document_data
		from true_page_draft
			join document on true_page_draft.document_id = document.document_id
		where
			true_page_draft.author_user_id = :userId
			and true_page_draft.page_route = :pageRoute
		order by
			true_page_draft.updated_date desc
	""")
	fun listPageDraftsByUserAndPageRoute(userId: UUID, pageRoute: String): Flow<FullPageDraftRow>

	@Query("""
		select page.*, document.document_data
		from
				page join document on page.document_id = document.document_id,
				(select based_on_draft_id from document_draft where document_draft.document_id = :documentId) as parent_page_draft
		where
				page.based_on_draft_id is not distinct from parent_page_draft.based_on_draft_id
		order by page.page_route
	""")
	fun listPublishedPagesAheadOfDraft(documentId: UUID): Flow<FullPageDraftRow>

	@Query("""
		select page.*, document.document_data
		from page join document on page.document_id = document.document_id
		where page.author_user_id = :userId
		order by page.page_route
	""")
	fun listPublishedPagesByAuthor(userId: UUID): Flow<FullPageDraftRow>
}
