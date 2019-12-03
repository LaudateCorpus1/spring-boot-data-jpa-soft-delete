package org.yuequan.jpa.soft.delete.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.yuequan.jpa.soft.delete.repository.EnableJpaSoftDeleteRepositories;
import org.yuequan.jpa.soft.delete.repository.support.JpaSoftDeleteRepository;

@SpringBootApplication
@EnableHypermediaSupport(type = HypermediaType.HAL)
@EnableJpaSoftDeleteRepositories
public class JpaSoftDeleteSpringBootStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaSoftDeleteSpringBootStarterApplication.class, args);
	}
	
	@Bean
	public SpelAwareProxyProjectionFactory projectionFactory() {
		return new SpelAwareProxyProjectionFactory();
	}
	
}
