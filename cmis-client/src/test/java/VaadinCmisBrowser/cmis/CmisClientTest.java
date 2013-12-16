package VaadinCmisBrowser.cmis;

import VaadinCmisBrowser.cmis.views.RepositoryView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CmisClientTest {

    private CmisClient client;

    @Before
    public void setUp() {
        client = new OpenCmisInMemoryClient();
    }

    @Test
    public void getRepositories() {
        Collection<RepositoryView> repositories = client.getRepositories();

        // There is at least one repository
        assertTrue(! repositories.isEmpty());
    }

    @Test
    public void connect() {
        // Connect to the first repository
        client.connect(client.getRepositories().iterator().next().getId());

        // Check that the current directory is the root directory
        assertEquals(client.getCurrentPath(), "/");
    }

    @Test
    public void navigateTo() {
        // TODO
        fail();
    }

    @Test
    public void listCurrentFolder() {
        // TODO
        fail();
    }

    @Test
    public void isFolder() {
        // TODO
        fail();
    }

    @Test
    public void isDocument() {
        // TODO
        fail();
    }

    @Test
    public void getFile() {
        // TODO
        fail();
    }

    @Test
    public void getDocument() {
        // TODO
        fail();
    }

    @Test
    public void getFolder() {
        // TODO
        fail();
    }

    @Test
    public void createFolder() {
        // TODO
        fail();
    }

    @Test
    public void upload() {
        // TODO
        fail();
    }

    @Test
    public void deleteDocument() {
        // TODO
        fail();
    }

    public void deleteFolder() {
        // TODO
        fail();
    }

    public void search() {
        // TODO
        fail();
    }
}
