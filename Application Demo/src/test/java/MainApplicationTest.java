import application.context.test.ApplicationRunner;
import application.context.test.annotations.TestInstance;
import application.context.test.annotations.Launcher;
import application.context.test.annotations.Mock;
import main.application.MainApplication;
import main.application.service.NamedService;
import main.application.service.NoService;
import main.application.service.Service;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ApplicationRunner.class)
@Launcher(name = MainApplication.class)
public class MainApplicationTest {

    @Mock
    public Service service;

    @Mock
    public NoService noService;

    @Mock(alias = "namedService")
    public NamedService namedService;

    @TestInstance
    public MainApplication application;

    @Test
    public void testInjection() {
        Assert.assertNotNull(service);
        Assert.assertNotNull(noService);
        Assert.assertNotNull(namedService);
    }

    @Test
    public void testOnStart() {
        application.start();
    }

    @Test
    public void testOnStop() {
        application.stop();
    }
}

