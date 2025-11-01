package au.com.supernovae.repository

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PublishedDocumentRepositoryTest(
	private val publishedDocumentRepository: PublishedDocumentRepository
): AbstractRepositoryTest()
{
	@Test
	fun `publishDocument function creates a page`() = runTestTx {
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
		val documentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)
		val nextTime = time.plusSeconds(1)

		databaseClient.sql("""
			with
			i0 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i1 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, null, null)),
			i4 as (insert into page_draft values (:baseDocumentUuid, 'login')),
			i5 as (insert into published_document values (:baseDocumentUuid)),

			i2 as (insert into document values (:documentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid, 'Draft 1', :nextTime, :nextTime, null, :baseDocumentUuid))
			insert into page_draft values (:documentUuid, 'login')
		""".trimIndent())
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("documentUuid", documentUuid)
			.bind("time", time)
			.bind("nextTime", nextTime)
			.await()

		val beforePublish = databaseClient.sql("select document_id from page where page_route = 'login'")
			.fetch()
			.awaitSingle()["document_id"] as UUID
		publishedDocumentRepository.publishDocument(documentUuid)
		val afterPublish = databaseClient.sql("select document_id from page where page_route = 'login'")
			.fetch()
			.awaitSingle()["document_id"] as UUID

		assertEquals(baseDocumentUuid, beforePublish)
		assertEquals(documentUuid, afterPublish)
	}

	@Test
	fun `publishDocument function creates a simple document`() = runTestTx {
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
		val documentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)
		val nextTime = time.plusSeconds(1)

		databaseClient.sql("""
			with
			i0 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i1 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, null, null)),
			i4 as (insert into simple_document_draft values (:baseDocumentUuid, '/:description_1')),
			i5 as (insert into published_document values (:baseDocumentUuid)),

			i2 as (insert into document values (:documentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid, 'Draft 1', :nextTime, :nextTime, null, :baseDocumentUuid))
			insert into simple_document_draft values (:documentUuid, '/:description_1')
		""".trimIndent())
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("documentUuid", documentUuid)
			.bind("time", time)
			.bind("nextTime", nextTime)
			.await()

		val beforePublish = databaseClient.sql("select document_id from simple_document where document_entity_name = '/:description_1'")
			.fetch()
			.awaitSingle()["document_id"] as UUID
		publishedDocumentRepository.publishDocument(documentUuid)
		val afterPublish = databaseClient.sql("select document_id from simple_document where document_entity_name = '/:description_1'")
			.fetch()
			.awaitSingle()["document_id"] as UUID

		assertEquals(baseDocumentUuid, beforePublish)
		assertEquals(documentUuid, afterPublish)
	}
}
