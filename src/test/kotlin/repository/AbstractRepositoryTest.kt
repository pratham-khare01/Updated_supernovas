package au.com.supernovae.repository

import kotlinx.coroutines.test.runTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

@SpringBootTest
abstract class AbstractRepositoryTest {
	@Autowired
	private lateinit var txManager: ReactiveTransactionManager
	@Autowired
	lateinit var databaseClient: DatabaseClient
	private val txOperator: TransactionalOperator by lazy { TransactionalOperator.create(txManager) }

	fun runTestTx(f: suspend () -> Unit): Unit = runTest {
		txOperator.executeAndAwait { transaction ->
			transaction.setRollbackOnly()
			f()
		}
	}
}
