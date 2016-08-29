package test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSpringBoot {

	
	@RequestMapping("/hello")
    public String greeting() {
        return "Hello World!";
    }
}
