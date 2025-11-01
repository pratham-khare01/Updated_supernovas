package au.com.supernovae

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SupernovaeApplication

fun main(args: Array<String>) {
	runApplication<SupernovaeApplication>(*args)
}
