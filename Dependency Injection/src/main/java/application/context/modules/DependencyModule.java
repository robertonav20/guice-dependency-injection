package application.context.modules;

import application.context.annotations.Component;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

final class DependencyModule extends AbstractModule {

	private static final Logger logger = LogManager.getLogger(DependencyModule.class);
	private final String path;

	DependencyModule(String path) {
		this.path = path;
	}

	@Override
	public void configure() {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			Set<Class> classes = ClassPath.from(loader).getTopLevelClassesRecursive(path).stream()
				.map(c -> c.load())
				.filter(c -> c.getAnnotation(Component.class) != null)
				.map(c -> {
					try {
						return Class.forName(c.getName());
					} catch (ClassNotFoundException e) {
						logger.error(e);
					}
					return null;
				})
				.collect(Collectors.toSet());

			for (Class clazz : classes) {
				logger.debug("Found {} dependency", clazz.getName());
				Component annotation = (Component) clazz.getAnnotation(Component.class);
				if (clazz.getInterfaces().length > 0) {
					Arrays.stream(clazz.getInterfaces()).forEach(i -> {
						if (annotation.primary()) {
							bindClass(i, clazz);
						} else {
							bindClass(i, clazz, annotation.alias());
						}
					});
				} else if (Modifier.isAbstract(clazz.getSuperclass().getModifiers())) {
					if (annotation.primary()) {
						bindClass(clazz.getSuperclass(), clazz);
					} else {
						bindClass(clazz.getSuperclass(), clazz, annotation.alias());
					}
				} else {
					if (annotation.primary()) {
						bindClass(clazz);
					} else {
						bindClass(clazz, annotation.alias());
					}
				}
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

	private void bindClass(Class clazz) {
		bind(clazz).asEagerSingleton();
	}

	private void bindClass(Class clazz, String alias) {
		bind(clazz).annotatedWith(Names.named(alias)).to(clazz).asEagerSingleton();
	}

	private void bindClass(Class interfaze, Class clazz) {
		bind(interfaze).to(clazz).asEagerSingleton();
	}

	private void bindClass(Class interfaze, Class clazz, String alis) {
		bind(interfaze).annotatedWith(Names.named(alis)).to(clazz).asEagerSingleton();
	}
}
