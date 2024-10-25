package aero.sita.compreface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import aero.sita.compreface.utils.MiscUtil;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan(basePackages = "aero.sita.compreface")
@ComponentScan(basePackages = "aero.sita.compreface")
@EnableScheduling
@EnableSwagger2
@SpringBootApplication(scanBasePackages = "aero.sita.compreface")
public class Application {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${app.name}")
	String name;

	@Value("${app.version}")
	String version;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.info("\n\n─ •✧✧•  {}  {}  •✧✧• ─\n", name, version);
		};
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		return MiscUtil.getOrCreateSerializingObjectMapper();
	}

	@Bean
	@Primary
	public Gson gson() {
		return MiscUtil.getOrCreateSerializingGsonBuilder();
	}

}