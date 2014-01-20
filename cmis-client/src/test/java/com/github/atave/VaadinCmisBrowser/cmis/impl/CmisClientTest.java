package com.github.atave.VaadinCmisBrowser.cmis.impl;

import com.github.atave.VaadinCmisBrowser.cmis.api.*;
import com.github.atave.junderscore.Lambda1;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Scanner;

import static com.github.atave.junderscore.JUnderscore._;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CmisClientTest {

    // Constants
    private static final String WAR = "../lib/chemistry-opencmis-server-inmemory-0.10.0.war";

    private static final String SERVER_PREFIX = "InMemoryServer";
    private static final String FILLER_PREFIX = "RepositoryFiller";

    private static String cat(String prefix, String suffix) {
        return prefix + "." + suffix;
    }

    // Server properties
    protected static final String REPO_ID = cat(SERVER_PREFIX, "RepositoryId");
    private static final String USER = cat(SERVER_PREFIX, "User");
    private static final String PASSWORD = cat(SERVER_PREFIX, "Password");

    // Repository filler properties
    private static final String DOCS_PER_FOLDER = cat(FILLER_PREFIX, "DocsPerFolder");
    private static final String FOLDER_PER_FOLDER = cat(FILLER_PREFIX, "FolderPerFolder");
    private static final String DEPTH = cat(FILLER_PREFIX, "Depth");
    protected static final String CONTENT_SIZE_IN_KB = cat(FILLER_PREFIX, "ContentSizeInKB");

    // Variables
    private Server server;
    private CmisClient client;

    @Before
    public void setUp() throws Exception {
        // Load the OpenCMIS InMemory Server webapp
        WebAppContext webApp = new WebAppContext();
        webApp.setWar(WAR);

        // Set the context path
        String[] splitAtomPubUrl = Config.get(OpenCmisInMemoryClient.ATOMPUB_URL).split("/");
        String contextPath = "/" + splitAtomPubUrl[splitAtomPubUrl.length - 2];
        webApp.setContextPath(contextPath);

        // Start server
        server = new Server(0);
        server.setHandler(webApp);
        server.start();

        // Create the client
        int port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
        String atomPubUrl = "http://localhost:" + port + contextPath + "/atom11";
        client = new OpenCmisInMemoryClient(Config.get(USER), Config.get(PASSWORD), atomPubUrl);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void getRepositories() {
        Collection<RepositoryView> repositories = client.getRepositories();

        // There is exactly one repository
        assertEquals(repositories.size(), 1);
    }

    @Test
    public void listCurrentFolder() {
        FolderView folder = client.getCurrentFolder();
        assertEquals(folder.getDocuments().size(), Integer.parseInt(Config.get(DOCS_PER_FOLDER)));
        assertEquals(folder.getFolders().size(), Integer.parseInt(Config.get(FOLDER_PER_FOLDER)));
    }

    @Test
    public void navigateTo() {
        FolderView currentFolder = client.getCurrentFolder();
        for (FolderView folder : currentFolder.getFolders()) {
            client.navigateTo(folder.getPath());
            assertEquals(folder.getPath(), client.getCurrentFolder().getPath());
            client.navigateTo("..");
            assertEquals(folder.getParent().getPath(), currentFolder.getPath());
        }
    }

    void testCreationProperties(FileView fileView) {
        assertEquals(fileView.getCreatedBy(), Config.get(USER));
        assertNotNull(fileView.getCreationDate());
    }

    @Test
    public void createFolder() {
        final String name = "CreatedFolder";
        for (FolderView folder : client.getCurrentFolder().getFolders()) {
            FolderView createdFolder = client.createFolder(folder.getPath(), name);

            testCreationProperties(createdFolder);

            boolean exists = _(folder.getFolders()).some(new Lambda1<Boolean, FolderView>() {
                @Override
                public Boolean call(FolderView o) {
                    return o.getName().equals(name);
                }
            });
            assertTrue(exists);
        }
    }

    private InputStream asInputStream(String string) {
        try {
            return new ByteArrayInputStream(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String asString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private void testModificationProperties(FileView fileView) {
        assertEquals(fileView.getLastModifiedBy(), Config.get(USER));
        assertNotNull(fileView.getLastModificationDate());
    }

    private void checkContents(DocumentView document, String content) {
        try (InputStream inputStream = document.download()) {
            String downloadedContent = asString(inputStream);
            assertEquals(downloadedContent, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void upload() {
        String name = "UploadedFile.txt";

        // Create a new file
        String content = "ehi";
        DocumentView uploadedDocument = client.upload("/", name, null,
                asInputStream(content), BigInteger.valueOf(content.length()),
                VersioningState.MAJOR, null, null);

        assertEquals(name, uploadedDocument.getName());
        testCreationProperties(uploadedDocument);
        checkContents(uploadedDocument, content);

        String firstVersion = uploadedDocument.getVersionLabel();
        assertNotNull(firstVersion);

        // Create new minor version
        String content2 = "ohi";
        String checkInComment = "new minor version";
        VersioningState versioningState = VersioningState.MINOR;
        uploadedDocument = client.upload("/", name, null, asInputStream(content2),
                BigInteger.valueOf(content2.length()), versioningState,
                checkInComment, null);

        testCreationProperties(uploadedDocument);
        testModificationProperties(uploadedDocument);
        checkContents(uploadedDocument, content2);

        assertEquals(versioningState == VersioningState.MAJOR,
                uploadedDocument.isMajorVersion());

        assertTrue(uploadedDocument.isLatestVersion());

        assertEquals(uploadedDocument.getCheckinComment(), checkInComment);

        // Check all versions are available
        assertEquals(uploadedDocument.getAllVersions().size(), 2);

        // Check again the first version
        uploadedDocument = uploadedDocument.getObjectOfVersion(firstVersion);
        testCreationProperties(uploadedDocument);
        checkContents(uploadedDocument, content);
    }

    private DocumentView createVersionedDocument(String fileName, String[] contents) {
        DocumentView document = null;

        for (String content : contents) {
            document = client.upload("/", fileName, null, asInputStream(content),
                    BigInteger.valueOf(content.length()), VersioningState.MAJOR,
                    null, null);
        }

        return document;
    }

    @Test
    public void deleteDocument() {
        String fileName = "DELETE_ME.txt";
        String[] contents = {"useless content", "more useless content"};

        // Delete all versions of a document
        DocumentView versionedDocument = createVersionedDocument(fileName, contents);
        final String documentPath = versionedDocument.getPath();
        client.deleteDocument(documentPath);

        // Check total deletion
        boolean found = _(client.getCurrentFolder().getDocuments()).some(new Lambda1<Boolean, DocumentView>() {
            @Override
            public Boolean call(DocumentView o) {
                return o.getPath().equals(documentPath);
            }
        });
        assertFalse(found);

        // Delete only a version of a document
        versionedDocument = createVersionedDocument(fileName, contents);
        if (!versionedDocument.isLatestVersion()) {
            versionedDocument = versionedDocument.getObjectOfLatestVersion(false);
        }
        String latestVersion = versionedDocument.getVersionLabel();
        client.deleteDocument(documentPath, versionedDocument.getVersionLabel());

        // Check that exactly one version remains of that document
        versionedDocument = client.getDocument(documentPath);
        assertEquals(versionedDocument.getAllVersions().size(), 1);
        assertNotEquals(versionedDocument.getVersionLabel(), latestVersion);
        found = _(client.getCurrentFolder().getDocuments()).some(new Lambda1<Boolean, DocumentView>() {
            @Override
            public Boolean call(DocumentView o) {
                return o.getPath().equals(documentPath);
            }
        });
        assertTrue(found);
    }

    @Test
    public void deleteFolder() {
        FolderView folder = client.getCurrentFolder().getFolders().iterator().next();
        final String folderPath = folder.getPath();
        client.deleteFolder(folderPath);

        // Check deletion
        boolean found = _(client.getCurrentFolder().getFolders()).some(new Lambda1<Boolean, FolderView>() {
            @Override
            public Boolean call(FolderView o) {
                return o.getPath().equals(folderPath);
            }
        });
        assertFalse(found);
    }

    @Test
    public void search() {
        // Search generated documents
        int docsPerFolder = Integer.parseInt(Config.get(DOCS_PER_FOLDER));
        int depth = Integer.parseInt(Config.get(DEPTH));
        int folderPerFolder = Integer.parseInt(Config.get(FOLDER_PER_FOLDER));
        int numFolders = (int) (Math.pow(folderPerFolder, depth) - 1);
        int generatedDocuments = docsPerFolder * numFolders;

        ItemIterable<DocumentView> results = client.search(null, "Lorem");

        assertEquals(results.getTotalNumItems(), generatedDocuments);

        // Search versioned documents
        String fileName = "FIND_ME.txt";
        String[] contents = {"ax", "by", "x'y"};
        createVersionedDocument(fileName, contents);

        results = client.search(fileName, "a");
        assertEquals(results.getTotalNumItems(), 1);
        assertEquals(contents[0], asString(results.iterator().next().download()));

        results = client.search(fileName, "b");
        assertEquals(results.getTotalNumItems(), 1);
        assertEquals(contents[1], asString(results.iterator().next().download()));

        results = client.search(fileName, "x");
        assertEquals(results.getTotalNumItems(), 2);

        results = client.search(fileName, "y");
        assertEquals(results.getTotalNumItems(), 2);

        results = client.search(fileName, "'");
        assertEquals(results.getTotalNumItems(), 1);
        assertEquals(contents[2], asString(results.iterator().next().download()));
    }
}
