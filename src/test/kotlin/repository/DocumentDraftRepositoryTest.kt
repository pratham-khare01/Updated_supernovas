package au.com.supernovae.repository

import io.r2dbc.postgresql.codec.Json
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DocumentDraftRepositoryTest(
	private val documentDraftRepository: DocumentDraftRepository
): AbstractRepositoryTest() {
	@Test
	fun `updateDocumentDraft update non-existant draft returns 'not_exists'`() = runTestTx {
		val result = documentDraftRepository.updateDocumentDraft(
			UUID.fromString("00000000-0000-0000-0000-000000000000"),
			Json.of("{}"),
			LocalDateTime.MIN
		)

		assertEquals("not_exists", result.errorCode)
	}

	@Test
	fun `updateDocumentDraft update published draft returns 'is_published'`() = runTestTx {
		val accountUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
		val documentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)

		databaseClient.sql("""
			with
			i1 as (insert into account values (:accountUuid, 'johnsmith@example.com', 'John Smith', '1', :time)),
			i2 as (insert into document values (:documentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid, 'Draft 1', :time, :time, :accountUuid, null)),
			i4 as (insert into simple_document_draft values (:documentUuid, 'front_page_1'))
			insert into published_document values (:documentUuid)
		""".trimIndent())
			.bind("documentUuid", documentUuid)
			.bind("accountUuid", accountUuid)
			.bind("time", time)
			.await()

		val result = documentDraftRepository.updateDocumentDraft(
			documentUuid,
			Json.of("{}"),
			time
		)

		assertEquals("is_published", result.errorCode)
	}

	@Test
	fun `updateDocumentDraft updating existing draft returns 'success'`() = runTestTx {
		val accountUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
		val documentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)

		databaseClient.sql("""
			with
			i1 as (insert into account values (:accountUuid, 'johnsmith@example.com', 'John Smith', '1', :time)),
			i2 as (insert into document values (:documentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid, 'Draft 1', :time, :time, :accountUuid, null))
			insert into simple_document_draft values (:documentUuid, 'front_page_1')
		""".trimIndent())
			.bind("documentUuid", documentUuid)
			.bind("accountUuid", accountUuid)
			.bind("time", time)
			.await()

		val result = documentDraftRepository.updateDocumentDraft(
			documentUuid,
			Json.of("{}"),
			time
		)

		assertEquals("success", result.errorCode)
	}

	@Test
	fun `listUsersEditingRoute returns correct user IDs`() = runTestTx {
		val accountUuids = arrayOf(
			UUID.fromString("00000000-0000-0000-0000-000000000001"),
			UUID.fromString("00000000-0000-0000-0000-000000000002"),
			UUID.fromString("00000000-0000-0000-0000-000000000003"),
		)
		val documentUuid0 = UUID.fromString("00000000-0000-0000-0000-000000000004")
		val documentUuid1 = UUID.fromString("00000000-0000-0000-0000-000000000005")
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000006")
		val expectedAccountUuids = listOf(accountUuids[2], accountUuids[0])
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)

		databaseClient.sql("""
			with
			i1 as (insert into account values
				(:accountUuid0, 'kasane.teto@example.com', 'Kasane Teto', '1', :time),
				(:accountUuid1, 'hatsune.miku@example.com', 'Hatsune Miku', '1', :time),
				(:accountUuid2, 'kagamine.rin@example.com', 'Kagamine Rin', '1', :time)
			),

			i7 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i8 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, null, null)),
			i9 as (insert into page_draft values (:baseDocumentUuid, 'login')),
			i10 as (insert into published_document values (:baseDocumentUuid)),

			i2 as (insert into document values (:documentUuid0, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid0, 'Draft 1', :time, :time, :accountUuid0, :baseDocumentUuid)),
			i4 as (insert into page_draft values (:documentUuid0, 'login')),

			i5 as (insert into document values (:documentUuid1, '{}'::jsonb)),
			i6 as (insert into document_draft values (:documentUuid1, 'Draft 1', :time, :time, :accountUuid2, :baseDocumentUuid))
			insert into page_draft values (:documentUuid1, 'login')
		""".trimIndent())
			.bind("accountUuid0", accountUuids[0])
			.bind("accountUuid1", accountUuids[1])
			.bind("accountUuid2", accountUuids[2])
			.bind("documentUuid0", documentUuid0)
			.bind("documentUuid1", documentUuid1)
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("time", time)
			.await()

		val result = documentDraftRepository
			.listUsersEditingRoute("login")
			.map { user -> user.authorUserId }
			.toList()

		assertEquals(expectedAccountUuids, result)
	}

	@Test
	fun `listUsersEditingSimpleDocument returns correct user IDs`() = runTestTx {
		val accountUuids = arrayOf(
			UUID.fromString("00000000-0000-0000-0000-000000000001"),
			UUID.fromString("00000000-0000-0000-0000-000000000002"),
			UUID.fromString("00000000-0000-0000-0000-000000000003"),
		)
		val documentUuid0 = UUID.fromString("00000000-0000-0000-0000-000000000004")
		val documentUuid1 = UUID.fromString("00000000-0000-0000-0000-000000000005")
		val baseDocumentUuid = UUID.fromString("00000000-0000-0000-0000-000000000006")
		val expectedAccountUuids = listOf(accountUuids[2], accountUuids[0])
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)

		databaseClient.sql("""
			with
			i1 as (insert into account values
				(:accountUuid0, 'kasane.teto@example.com', 'Kasane Teto', '1', :time),
				(:accountUuid1, 'hatsune.miku@example.com', 'Hatsune Miku', '1', :time),
				(:accountUuid2, 'kagamine.rin@example.com', 'Kagamine Rin', '1', :time)
			),

			i7 as (insert into document values (:baseDocumentUuid, '{}'::jsonb)),
			i8 as (insert into document_draft values (:baseDocumentUuid, 'Draft 1', :time, :time, null, null)),
			i9 as (insert into simple_document_draft values (:baseDocumentUuid, 'front')),
			i10 as (insert into published_document values (:baseDocumentUuid)),

			i2 as (insert into document values (:documentUuid0, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid0, 'Draft 1', :time, :time, :accountUuid0, :baseDocumentUuid)),
			i4 as (insert into simple_document_draft values (:documentUuid0, 'front')),

			i5 as (insert into document values (:documentUuid1, '{}'::jsonb)),
			i6 as (insert into document_draft values (:documentUuid1, 'Draft 1', :time, :time, :accountUuid2, :baseDocumentUuid))
			insert into simple_document_draft values (:documentUuid1, 'front')
		""".trimIndent())
			.bind("accountUuid0", accountUuids[0])
			.bind("accountUuid1", accountUuids[1])
			.bind("accountUuid2", accountUuids[2])
			.bind("documentUuid0", documentUuid0)
			.bind("documentUuid1", documentUuid1)
			.bind("baseDocumentUuid", baseDocumentUuid)
			.bind("time", time)
			.await()

		val result = documentDraftRepository
			.listUsersEditingSimpleDocument("front")
			.map { user -> user.authorUserId }
			.toList()

		assertEquals(expectedAccountUuids, result)
	}

	@Test
	fun `deleteDocumentDraft deleting non-existant draft returns 'not_exists'`() = runTestTx {
		val result = documentDraftRepository.deleteDocumentDraft(UUID.fromString("00000000-0000-0000-0000-0000000000"))

		assertEquals("not_exists", result.errorCode)
	}

	@Test
	fun `deleteDocumentDraft deleting published draft returns 'is_published'`() = runTestTx {
		val documentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)

		databaseClient.sql("""
			with
			i2 as (insert into document values (:documentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid, 'Draft 1', :time, :time, null, null)),
			i4 as (insert into simple_document_draft values (:documentUuid, 'front_page_1'))
			insert into published_document values (:documentUuid)
		""".trimIndent())
			.bind("documentUuid", documentUuid)
			.bind("time", time)
			.await()

		val result = documentDraftRepository.deleteDocumentDraft(documentUuid)

		assertEquals("is_published", result.errorCode)
	}

	@Test
	fun `deleteDocumentDraft deleting non-published draft returns 'success'`() = runTestTx {
		val documentUuid = UUID.fromString("00000000-0000-0000-0000-000000000002")
		val time = LocalDateTime.of(2025, 9, 12, 0, 0)

		databaseClient.sql("""
			with
			i2 as (insert into document values (:documentUuid, '{}'::jsonb)),
			i3 as (insert into document_draft values (:documentUuid, 'Draft 1', :time, :time, null, null))
			insert into simple_document_draft values (:documentUuid, 'front_page_1')
		""".trimIndent())
			.bind("documentUuid", documentUuid)
			.bind("time", time)
			.await()

		val beforeRowsCount = databaseClient.sql("select 1 from document_draft where document_id = :documentUuid")
			.bind("documentUuid", documentUuid)
			.fetch()
			.awaitRowsUpdated()
		val result = documentDraftRepository.deleteDocumentDraft(documentUuid)
		val afterRowsCount = databaseClient.sql("select 1 from document_draft where document_id = :documentUuid")
			.bind("documentUuid", documentUuid)
			.fetch()
			.awaitRowsUpdated()

		assertEquals("success", result.errorCode)
		assertEquals(1, beforeRowsCount)
		assertEquals(0, afterRowsCount)
	}
}
