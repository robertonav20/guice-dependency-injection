package application.context.test.modules;

import application.context.test.annotations.Mock;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Set;

final class LauncherApplicationModule extends AbstractModule {

    private final Set<Field> dependencies;

    LauncherApplicationModule(Set<Field> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public void configure() {
        this.dependencies.forEach(dependency -> {
            Mock annotation = dependency.getAnnotation(Mock.class);
            Class dependencyClass = dependency.getType();
            Object dependencyInstance = Mockito.mock(dependency.getType());
            if (StringUtils.isNotEmpty(annotation.alias())) {
                bind(dependencyClass)
                        .annotatedWith(Names.named(annotation.alias()))
                        .toInstance(dependencyInstance);
            } else {
                bind(dependencyClass).toInstance(dependencyInstance);
            }
        });
    }
}
