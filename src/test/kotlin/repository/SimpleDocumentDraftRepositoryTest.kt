package au.com.supernovae.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.await
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SimpleDocumentDraftRepositoryTest(
	private val simpleDocumentDraftRepository: SimpleDocumentDraftRepository
): AbstractRepositoryTest() {
	@Test
	fun `listSimpleDocumentsAheadOfDraft lists page drafts ahead of current draft`() = runTestTx {
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000003")
		val behindDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val aheadDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)
		val nextTime = time.plusSeconds(1)

		databaseClient.sql("""
			with
			i0 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i1 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, null, null)),
			i4 as (insert into simple_document_draft values (:baseDocumentUuid, '/:description_1')),
			i5 as (insert into published_document values (:baseDocumentUuid)),

			i6 as (insert into document values (:aheadDocumentUuid, '{}'::jsonb)),
			i7 as (insert into document_draft values (:aheadDocumentUuid, 'Draft 1', :nextTime, :nextTime, null, :baseDocumentUuid)),
			i8 as (insert into simple_document_draft values (:aheadDocumentUuid, '/:description_1')),
			i9 as (insert into published_document values (:aheadDocumentUuid)),

			i2 as (insert into document values (:behindDocumentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:behindDocumentUuid, 'Draft 1', :nextTime, :nextTime, null, :baseDocumentUuid))
			insert into simple_document_draft values (:behindDocumentUuid, '/:description_1')
		""".trimIndent())
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("behindDocumentUuid", behindDocumentUuid)
			.bind("aheadDocumentUuid", aheadDocumentUuid)
			.bind("time", time)
			.bind("nextTime", nextTime)
			.await()

		val result = simpleDocumentDraftRepository
			.listSimpleDocumentsAheadOfDraft(behindDocumentUuid)
			.map { row -> row.documentId }
			.toList()

		assertEquals(listOf(aheadDocumentUuid), result)
	}

	@Test
	fun `getSimpleDocument gets latest simple document for entity name`() = runTestTx {
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000003")
		val aheadDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
		val documentEntityName = "/:description_1"
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)
		val nextTime = time.plusSeconds(1)

		databaseClient.sql("""
			with
			i0 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i1 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, null, null)),
			i2 as (insert into simple_document_draft values (:baseDocumentUuid, :documentEntityName)),
			i3 as (insert into published_document values (:baseDocumentUuid)),

			i4 as (insert into document values (:aheadDocumentUuid, '{}'::jsonb)),
			i5 as (insert into document_draft values (:aheadDocumentUuid, 'Draft 1', :nextTime, :nextTime, null, :baseDocumentUuid)),
			i6 as (insert into simple_document_draft values (:aheadDocumentUuid, :documentEntityName))
			insert into published_document values (:aheadDocumentUuid)
		""".trimIndent())
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("aheadDocumentUuid", aheadDocumentUuid)
			.bind("documentEntityName", documentEntityName)
			.bind("time", time)
			.bind("nextTime", nextTime)
			.await()

		val result = simpleDocumentDraftRepository.getSimpleDocument(documentEntityName).documentId

		assertEquals(aheadDocumentUuid, result)
	}

	@Test
	fun `listPublishedSimpleDocumentsByAuthor gets published simple documents for author`() = runTestTx {
		val accountUuid0 = UUID.fromString("00000000-0000-0000-0000-000000000006")
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000003")
		val expectedDocumentUuids = listOf(UUID.fromString("00000000-0000-0000-0000-000000000001"))
		val unownedDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000004")
		val documentEntityName = "/:description_1"
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)
		val nextTime = time.plusSeconds(1)

		databaseClient.sql("""
			with
			i7 as (insert into account values
				(:accountUuid0, 'kasane.teto@example.com', 'Kasane Teto', '1', :time)
			),
			i0 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i1 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, :accountUuid0, null)),
			i2 as (insert into simple_document_draft values (:baseDocumentUuid, :documentEntityName)),
			i3 as (insert into published_document values (:baseDocumentUuid)),

			i4 as (insert into document values (:aheadDocumentUuid, '{}'::jsonb)),
			i5 as (insert into document_draft values (:aheadDocumentUuid, 'Draft 1', :nextTime, :nextTime, :accountUuid0, :baseDocumentUuid)),
			i6 as (insert into simple_document_draft values (:aheadDocumentUuid, :documentEntityName)),
			i8 as (insert into published_document values (:aheadDocumentUuid)),

			i9 as (insert into document values (:unownedDocumentUuid, '{}'::jsonb)),
			i10 as (insert into document_draft values (:unownedDocumentUuid, 'Draft 1', :nextTime, :nextTime, null, :baseDocumentUuid)),
			i11 as (insert into simple_document_draft values (:unownedDocumentUuid, :documentEntityName))
			insert into published_document values (:unownedDocumentUuid)
		""".trimIndent())
			.bind("accountUuid0", accountUuid0)
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("aheadDocumentUuid", expectedDocumentUuids[0])
			.bind("unownedDocumentUuid", unownedDocumentUuid)
			.bind("documentEntityName", documentEntityName)
			.bind("time", time)
			.bind("nextTime", nextTime)
			.await()

		val result = simpleDocumentDraftRepository.listPublishedSimpleDocumentsByAuthor(accountUuid0).map{ row -> row.documentId }.toList()

		assertEquals(expectedDocumentUuids, result)
	}
}
