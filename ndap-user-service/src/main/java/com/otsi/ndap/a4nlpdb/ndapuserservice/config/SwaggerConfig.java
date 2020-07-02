package com.otsi.ndap.a4nlpdb.ndapuserservice.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
* @author Chandrakanth
*/

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() throws FileNotFoundException, IOException, XmlPullParserException {

		return new Docket(DocumentationType.SWAGGER_2)
				.select()//
				.apis(RequestHandlerSelectors.basePackage("com.otsi.ndap.a4nlpdb.ndapuserservice"))
				.paths(Predicates.not(PathSelectors.regex("/error")))
				.paths(regex("/api.*"))
				.build()
				.apiInfo(metadata())
				.useDefaultResponseMessages(false)
				.securitySchemes(new ArrayList<>(Arrays.asList(new ApiKey("Bearer %token", "Authorization", "Header"))))//
				.tags(new Tag("users", "Operations about users"))
				.tags(new Tag("ping", "Just a ping"))
				.genericModelSubstitutes(Optional.class);

	}

	/*
	 * @Bean public LinkDiscoverers discoverers() { List<LinkDiscoverer> plugins =
	 * new ArrayList<>(); plugins.add(new CollectionJsonLinkDiscoverer()); return
	 * new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	 * 
	 * }
	 */

	private ApiInfo metadata() throws FileNotFoundException, IOException, XmlPullParserException {
		
		return new ApiInfoBuilder()//
				.title("JSON Web Token Authentication API")//
				.description("This is a JWT authentication service.")//
				.version("2.0")//
				.build();
	}
	
	

}
