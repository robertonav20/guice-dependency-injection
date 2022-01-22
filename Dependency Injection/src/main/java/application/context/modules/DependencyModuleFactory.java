package application.context.modules;

public class DependencyModuleFactory {

    public static DependencyModule create(String path) {
        return new DependencyModule(path);
    }
}
