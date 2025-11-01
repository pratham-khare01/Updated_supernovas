package au.com.supernovae.config

import io.r2dbc.postgresql.codec.Json
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class PostgresJsonSerializer : JsonSerializer<Json>() {
	override fun serialize(value: Json, gen: JsonGenerator, serializers: SerializerProvider) {
		gen.writeRawValue(value.asString())
	}
}

@Configuration
class JacksonConfig {
	@Bean
	fun jacksonCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
		return Jackson2ObjectMapperBuilderCustomizer {
			it.serializerByType(Json::class.java, PostgresJsonSerializer())
		}
	}
}
