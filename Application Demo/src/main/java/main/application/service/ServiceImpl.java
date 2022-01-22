package main.application.service;

import application.context.annotations.Component;
import main.application.repository.Repository;

import javax.inject.Inject;

@Component
public class ServiceImpl implements Service {

    @Inject
    Repository repository;

    @Override
    public String hello() {
        return this.repository.hello();
    }
}
