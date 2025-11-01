package au.com.supernovae.repository

import au.com.supernovae.model.simple_document.*

import io.r2dbc.postgresql.codec.Json
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SimpleDocumentDraftRepository: CoroutineCrudRepository<SimpleDocumentDraft, UUID> {
	@Query("""
		with new_document as (
			insert into document(document_data)
			values (:documentData)
			returning *
		), new_document_draft as (
			insert into document_draft(document_id, draft_name, author_user_id, based_on_draft_id)
			select new_document.document_id, :draftName, :authorUserId, :basedOnDraftId
			from new_document
			returning *
		), new_simple_document_draft as (
			insert into simple_document_draft(document_id, document_entity_name)
			select new_document.document_id, :documentEntityName
			from new_document
			returning *
		)
		select
			new_document.document_data,
			new_simple_document_draft.document_entity_name,
			new_document_draft.*
		from
			new_document
			join new_document_draft using (document_id)
			join new_simple_document_draft using (document_id)
	""")
	suspend fun createFullDocumentDraft(documentData: Json, authorUserId: UUID?, basedOnDraftId: UUID?, documentEntityName: String, draftName: String): FullSimpleDocumentDraftRow

	@Query("""
		select
			simple_document.*,
			document.document_data
		from
			simple_document join document on simple_document.document_id = document.document_id
		where
			simple_document.document_entity_name = :documentEntityName
	""")
	suspend fun getSimpleDocument(documentEntityName: String): FullSimpleDocumentDraftRow

	@Query("""
		select true_simple_document_draft.*, document.document_data
		from true_simple_document_draft
			join document on true_simple_document_draft.document_id = document.document_id
		where
			true_simple_document_draft.author_user_id = :userId
		order by
			true_simple_document_draft.document_entity_name,
			true_simple_document_draft.updated_date desc
	""")
	fun listSimpleDocumentDraftsByUser(userId: UUID): Flow<FullSimpleDocumentDraftRow>
	@Query("""
		select true_simple_document_draft.*, document.document_data
		from true_simple_document_draft
			join document on true_simple_document_draft.document_id = document.document_id
		where
			true_simple_document_draft.author_user_id = :userId
			and true_simple_document_draft.document_entity_name = :documentEntityName
		order by
			true_simple_document_draft.updated_date desc
	""")
	fun listSimpleDocumentDraftsByUserAndDocumentEntityName(userId: UUID, documentEntityName: String): Flow<FullSimpleDocumentDraftRow>

	@Query("""
		select simple_document.*, document.document_data
		from
				simple_document join document on simple_document.document_id = document.document_id,
				(select based_on_draft_id from document_draft where document_draft.document_id = :documentId) as parent_simple_document
		where
				simple_document.based_on_draft_id is not distinct from parent_simple_document.based_on_draft_id
		order by simple_document.document_entity_name
	""")
	fun listSimpleDocumentsAheadOfDraft(documentId: UUID): Flow<FullSimpleDocumentDraftRow>

	@Query("""
		select simple_document.*, document.document_data
		from simple_document join document on simple_document.document_id = document.document_id
		where simple_document.author_user_id = :userId
		order by simple_document.document_entity_name
	""")
	fun listPublishedSimpleDocumentsByAuthor(userId: UUID): Flow<FullSimpleDocumentDraftRow>
}
