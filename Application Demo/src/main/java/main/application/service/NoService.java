package main.application.service;

import application.context.annotations.Component;

@Component
public class NoService {
    public String hello() {
        return this.getClass().getSimpleName();
    }
}
