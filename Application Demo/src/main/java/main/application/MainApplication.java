package main.application;

import application.context.ApplicationContext;
import application.context.annotations.App;
import application.context.annotations.OnStart;
import application.context.annotations.OnStop;
import main.application.service.NamedService;
import main.application.service.NoService;
import main.application.service.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;

@App(folder = "main")
public class MainApplication {

    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    @Inject
    private Service service;
    @Inject
    private NoService noService;
    @Inject
    @Named("namedService")
    private NamedService namedService;

    public static void main(String[] args) {
        ApplicationContext.start(MainApplication.class);
    }

    @OnStart
    public void start() {
        logger.info("Application is starting!");
        logger.info(service.hello());
        logger.info(noService.hello());
        logger.info(namedService.hello());
    }

    @OnStop
    public void stop() {
        logger.info("Application is stopping!");
    }
}
