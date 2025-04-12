package org.prograIII.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prograIII.thread.CovidThread;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // Iniciar el hilo con un retraso de 15 segundos
        CovidThread.startThreadWithDelay();
    }
}
