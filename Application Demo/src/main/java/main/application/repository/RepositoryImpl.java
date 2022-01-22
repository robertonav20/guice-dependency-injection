package main.application.repository;

import application.context.annotations.ApplicationComponent;

@ApplicationComponent
public class RepositoryImpl implements Repository {
    @Override
    public String hello() {
        return this.getClass().getSimpleName();
    }
}
