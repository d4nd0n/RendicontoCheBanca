package it.telecom.businesslogic.rest;

import it.telecom.businesslogic.data.CreateCheBancaDBInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface TestController {

    ResponseEntity<String> getHello();

    ResponseEntity<String> getMessage(String s);

    ResponseEntity<String> getHelloEnv();

    @PostMapping(value = "/createCheBancaDB/{fileString}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<File> createCheBancaDB(@RequestBody CreateCheBancaDBInput createCheBancaDBInput);
}
