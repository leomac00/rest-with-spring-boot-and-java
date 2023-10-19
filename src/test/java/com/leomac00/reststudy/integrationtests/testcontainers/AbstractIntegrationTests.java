package com.leomac00.reststudy.integrationtests.testcontainers;

import com.leomac00.reststudy.Utils.MyMediaType;
import com.leomac00.reststudy.configs.TestConfigs;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTests.Initializer.class)
public class AbstractIntegrationTests {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, WebMvcConfigurer {

        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);
        }

        @Override
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            configurer.favorParameter(false)
                    .ignoreAcceptHeader(false)
                    .useRegisteredExtensionsOnly(false)
                    .defaultContentType(MediaType.valueOf(TestConfigs.CONTENT_TYPE_JSON))
                    .mediaType("json", MediaType.valueOf(TestConfigs.CONTENT_TYPE_JSON))
                    .mediaType("x-yaml", MediaType.valueOf(TestConfigs.CONTENT_TYPE_YAML))
                    .mediaType("xml", MediaType.valueOf(TestConfigs.CONTENT_TYPE_XML));
        }
    }
}
