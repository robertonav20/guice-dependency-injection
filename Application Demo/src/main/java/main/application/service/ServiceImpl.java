package main.application.service;

import application.context.annotations.ApplicationComponent;
import main.application.repository.Repository;

import javax.inject.Inject;

@ApplicationComponent
public class ServiceImpl implements Service {

    @Inject
    Repository repository;

    @Override
    public String hello() {
        return this.repository.hello();
    }
}
