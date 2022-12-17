package it.telecom.businesslogic.rest;

import it.telecom.businesslogic.data.CreateCheBancaDBInput;
import it.telecom.businesslogic.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class TestControllerImpl implements TestController{

    private final TestService testService;

    @GetMapping(value = "/getHello/", produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<String> getHello() {
        String out = testService.getHello();
        return ResponseEntity.ok(out);
    }

    @GetMapping(value = "/getMessage/{s}/", produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<String> getMessage(@PathVariable String s) {
        String out = testService.getMessage(s);
        return ResponseEntity.ok(out);
    }

    @GetMapping(value = "/getHelloEnv/", produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<String> getHelloEnv() {
        String out = testService.getHelloEnv();
        return ResponseEntity.ok(out);
    }

    @PostMapping(value = "/createCheBancaDB/", produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<File> createCheBancaDB(@RequestBody CreateCheBancaDBInput createCheBancaDBInput) {
        File file = testService.createCheBancaDB(createCheBancaDBInput.getPath());
        return ResponseEntity.ok(file);
    }
}
