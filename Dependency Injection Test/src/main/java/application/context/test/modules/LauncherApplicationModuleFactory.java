package application.context.test.modules;

import java.lang.reflect.Field;
import java.util.Set;

public class LauncherApplicationModuleFactory {

    public static LauncherApplicationModule create(Set<Field> dependencies) {
        return new LauncherApplicationModule(dependencies);
    }
}
