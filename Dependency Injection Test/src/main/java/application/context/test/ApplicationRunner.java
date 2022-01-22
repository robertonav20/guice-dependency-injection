package application.context.test;

import application.context.test.annotations.TestInstance;
import application.context.test.annotations.Launcher;
import application.context.test.annotations.Mock;
import application.context.test.modules.LauncherApplicationModuleFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationRunner extends BlockJUnit4ClassRunner {

    private static final Logger logger = LogManager.getLogger(ApplicationRunner.class);

    private Class testClazz;
    private Set<Field> mockedDependenciesFields;
    private Injector injector;

    public ApplicationRunner(Class<?> testClass) throws Exception {
        super(testClass);

        testClazz = Class.forName(testClass.getName());
        mockedDependenciesFields = getFieldsByAnnotation(testClazz, Mock.class);
        injector = Guice.createInjector(LauncherApplicationModuleFactory.create(mockedDependenciesFields));
    }

    protected ApplicationRunner(TestClass testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Object createTest() throws Exception {
        Object testClassInstance = testClazz.getDeclaredConstructor().newInstance();
        mockedDependenciesFields.forEach(field -> {
            try {
                field.set(testClassInstance, injector.getInstance(field.getType()));
            } catch (Exception e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        });

        Launcher launcher = (Launcher) testClazz.getAnnotation(Launcher.class);
        Class applicationClazz = launcher.name();
        Object applicationInstance = injector.getInstance(applicationClazz);
        Field field = getFieldByAnnotation(testClazz, TestInstance.class);
        field.set(testClassInstance, applicationInstance);
        return testClassInstance;
    }

    private Field getFieldByAnnotation(Class clazz, Class annotation) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(annotation) != null)
                .findFirst().get();
    }

    private Set<Field> getFieldsByAnnotation(Class clazz, Class annotation) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(annotation) != null)
                .collect(Collectors.toSet());
    }
}
