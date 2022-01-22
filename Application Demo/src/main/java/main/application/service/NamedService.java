package main.application.service;

import application.context.annotations.ApplicationComponent;

@ApplicationComponent(primary = false, alias = "namedService")
public class NamedService {
    public String hello() {
        return this.getClass().getSimpleName();
    }
}
