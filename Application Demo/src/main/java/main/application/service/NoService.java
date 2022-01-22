package main.application.service;

import application.context.annotations.ApplicationComponent;

@ApplicationComponent
public class NoService {
    public String hello() {
        return this.getClass().getSimpleName();
    }
}
