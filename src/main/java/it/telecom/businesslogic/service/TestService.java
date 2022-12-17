package it.telecom.businesslogic.service;

import java.io.File;

public interface TestService {

    String getHello();

    String getMessage(String s);

    String getHelloEnv();


    File createCheBancaDB(String fileString);
}
