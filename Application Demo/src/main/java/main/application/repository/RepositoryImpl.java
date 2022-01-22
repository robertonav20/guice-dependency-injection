package main.application.repository;

import application.context.annotations.Component;

@Component
public class RepositoryImpl implements Repository {
    @Override
    public String hello() {
        return this.getClass().getSimpleName();
    }
}
