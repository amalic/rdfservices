package org.amalic.rdfservices;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@OpenAPIDefinition(
	info = @Info(
		version = "2020.04"
		, title = "Some generic RDF services" 
		, description = "Easy to extend webservices frontend for RDF endpoints"
		, license = @License(name = "MIT License", url = "https://github.com/amalic/rdfservices/blob/master/LICENSE")
		, contact = @Contact(name = "Alexander Malic", email = "alexander.malic@gmail.com")
	)
)


@SpringBootApplication
@RestController
public class Application {
	private static final Logger logger = Logger.getLogger(Application.class.getName());
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @RequestMapping("/")
    public void getDefault(HttpServletResponse response) {
    	try {
    		response.sendRedirect("/swagger-ui.html");
    	} catch (Exception e) {
    		logger.severe("Error: " + ExceptionUtils.getFullStackTrace(e));
    	}
    }
    
}