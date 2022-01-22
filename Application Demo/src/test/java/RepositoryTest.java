import application.context.test.ApplicationRunner;
import application.context.test.annotations.TestInstance;
import application.context.test.annotations.Launcher;
import application.context.test.annotations.Mock;
import main.application.repository.Repository;
import main.application.service.Service;
import main.application.service.ServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(ApplicationRunner.class)
@Launcher(name = ServiceImpl.class)
public class RepositoryTest {

    @Mock
    public Repository repository;

    @TestInstance
    public Service service;

    @Before
    public void setup() {
        Mockito.doReturn("TEST").when(this.repository).hello();
    }

    @Test
    public void testInjection() {
        Assert.assertNotNull(repository);
        Assert.assertNotNull(service);
    }

    @Test
    public void testHello() {
        service.hello();
    }
}

