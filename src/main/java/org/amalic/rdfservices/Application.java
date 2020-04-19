package org.amalic.rdfservices;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
		, title = "RDF OpenAPI services" 
		, description = 
				"<p>Easy to extend webservices frontend for RDF endpoints</p>" +
				"<p>Max limit of entries per page is 100.000. Default limit is set to 100.</p>"
		, contact = @Contact(name = "Alexander Malic", email = "alexander.malic@gmail.com")
		, license = @License(name = "MIT License", url = "https://github.com/amalic/rdfservices/blob/master/LICENSE")
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
    	} catch (IOException e) {
    		logger.severe("Error: " + ExceptionUtils.getStackTrace(e));
    	}
    }
    
}