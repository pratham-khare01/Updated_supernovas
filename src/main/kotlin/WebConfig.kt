package au.com.supernovae

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.resource.PathResourceResolver
import reactor.core.publisher.Mono

@Configuration
@Profile("prod")
class WebConfig : WebFluxConfigurer {
	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		registry.addResourceHandler("/{segment:^(?!api).*}/**", "/")
			.addResourceLocations("classpath:/static/svelte/")
			.resourceChain(true)
			.addResolver(object : PathResourceResolver() {
				override fun getResource(resourcePath: String, location: Resource): Mono<Resource> {
					val requested = location.createRelative(resourcePath)
					return if (requested.exists() && requested.isReadable) {
						Mono.just(requested)
					} else {
						Mono.just(ClassPathResource("/static/svelte/404.html"))
					}
				}
			})
	}
}
