package au.com.supernovae.repository

import au.com.supernovae.model.document.DocumentDraft
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PublishedDocumentRepository: CoroutineCrudRepository<DocumentDraft, UUID> {
	@Query("""
		insert into published_document (document_draft_id)
		values (:documentId)
	""")
	suspend fun publishDocument(documentId: UUID)
}
