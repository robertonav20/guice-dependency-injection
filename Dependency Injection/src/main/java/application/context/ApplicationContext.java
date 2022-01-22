package application.context;

import application.context.annotations.App;
import application.context.annotations.OnStart;
import application.context.annotations.OnStop;
import application.context.exceptions.ApplicationException;
import application.context.modules.DependencyModuleFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final public class ApplicationContext {

    private static final Logger logger = LogManager.getLogger(ApplicationContext.class);

    private static Injector injector;
    private static Class applicationClazz;
    private static Object application;
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("context-%d").build();
    private static final ExecutorService executor = Executors.newFixedThreadPool(1, threadFactory);

    public static void start(Class clazz) throws ApplicationException {
        executor.execute(() -> {
            String packageName = "";
            if (clazz.getAnnotation(App.class) != null) {
                packageName = ((App) clazz.getAnnotation(App.class)).folder();
            } else {
                packageName = clazz.getPackageName();
            }
            injector = Guice.createInjector(DependencyModuleFactory.create(packageName));
            application = injector.getInstance(clazz);
            applicationClazz = clazz;
            try {
                Optional<Method> onStartMethod = Arrays.stream(applicationClazz.getDeclaredMethods())
                    .filter(m -> m.getAnnotation(OnStart.class) != null)
                    .findFirst();
                if (onStartMethod.isPresent()) {
                    onStartMethod.get().invoke(application);
                }
            } catch (Exception e) {
                logger.error(e);
                throw new ApplicationException(e);
            }
        });
        Runtime.getRuntime().addShutdownHook(threadFactory.newThread(() -> stop()));
    }

    public static void stop() {
        try {
            Optional<Method> onStopMethod = Arrays.stream(applicationClazz.getDeclaredMethods())
                .filter(m -> m.getAnnotation(OnStop.class) != null)
                .findFirst();
            if (onStopMethod.isPresent()) {
                onStopMethod.get().invoke(application);
            }
            executor.shutdown();
            System.exit(0);
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException(e);
        }
    }
}
