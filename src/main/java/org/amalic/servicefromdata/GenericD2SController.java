package org.amalic.servicefromdata;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericD2SController {
	static final String PATTERN = "/d2s/**";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(PATTERN)
    public Object genericHandler(HttpServletRequest request) {
    	System.out.println(request.getRequestURI() + "?" + request.getQueryString());
    	System.out.println(" - " + request.getRequestURI().substring(PATTERN.length()-2));
    	System.out.println(" - " + request.getQueryString());
        return "Count: " + counter.incrementAndGet();
    }
}