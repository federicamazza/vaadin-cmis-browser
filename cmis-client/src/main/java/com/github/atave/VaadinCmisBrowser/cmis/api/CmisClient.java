package com.github.atave.VaadinCmisBrowser.cmis.api;

import com.github.atave.junderscore._map;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.ContentStreamAllowed;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple CMIS client.
 */
public abstract class CmisClient implements DocumentFetcher {

    // CmisClient state
    private Session currentSession;
    private Folder currentFolder;
    private String versionableCmisType;

    // Helpers

    /**
     * Returns the {@link org.apache.chemistry.opencmis.client.api.SessionFactory} to use.
     */
    protected abstract SessionFactory getSessionFactory();

    /**
     * Returns the {@link SessionParametersFactory} used to configure new {@code Session}s.
     */
    protected abstract SessionParametersFactory getSessionParametersFactory();

    /**
     * Returns the versionable cmis type for the current session, since cmis:document
     * is actually not versionable.
     */
    private String getVersionableCmisType() {
        return getVersionableCmisType(currentSession.getTypeDescendants(null, -1, true));
    }

    private String getVersionableCmisType(List<Tree<ObjectType>> trees) {
        for (Tree<ObjectType> tree : trees) {
            ObjectType objectType = tree.getItem();
            if (objectType instanceof DocumentType) {
                DocumentType documentType = (DocumentType) objectType;
                if (documentType.getContentStreamAllowed() == ContentStreamAllowed.ALLOWED
                        && documentType.isVersionable()) {
                    return documentType.getId();
                }
            }

            String versionableCmisType = getVersionableCmisType(tree.getChildren());
            if (versionableCmisType != null) {
                return versionableCmisType;
            }
        }

        return null;
    }

    /**
     * Gets an object by path or object id.
     *
     * @param pathOrId the path or id of the object
     * @return the object
     */
    private CmisObject getObject(String pathOrId) {
        return FileUtils.getObject(pathOrId, currentSession);
    }

    /**
     * Determines an object existence.
     */
    private boolean exists(String pathOrId) {
        try {
            getObject(pathOrId);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Gets a folder by path or object id.
     *
     * @param pathOrId the path or id of the folder
     * @return the folder object
     */
    private Folder getBareFolder(String pathOrId) {
        return FileUtils.getFolder(pathOrId, currentSession);
    }

    /**
     * Gets a document by path or object id.
     *
     * @param pathOrId the path or id of the document
     * @return the document object
     */
    private Document getBareDocument(String pathOrId) {
        Document document;

        try {
            document = (Document) getObject(pathOrId);
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Object is not a document!");
        }

        return document;
    }

    /**
     * Returns a PWC of the specified document.
     */
    private Document checkout(String pathOrId) {
        return getBareDocument(getBareDocument(pathOrId).checkOut().getId());
    }

    /**
     * Joins the specified paths.
     */
    private String joinPath(String parent, String child) {
        if (!parent.endsWith("/")) {
            parent += "/";
        }
        return parent + child;
    }

    // API

    /**
     * Returns the available repositories.
     */
    public Collection<RepositoryView> getRepositories() {
        return new _map<RepositoryView, Repository>() {
            @Override
            protected RepositoryView process(Repository object) {
                return new RepositoryView(object);
            }
        }.on(getSessionFactory().getRepositories(getSessionParametersFactory().newInstance()));
    }

    /**
     * Connects the client to a repository.
     *
     * @param repositoryId see {@link RepositoryView#getId()}
     * @throws CmisBaseException if the connection could not be established
     */
    public void connect(String repositoryId) throws CmisBaseException {
        currentSession = getSessionFactory().createSession(getSessionParametersFactory().newInstance(repositoryId));
        currentFolder = currentSession.getRootFolder();
        versionableCmisType = getVersionableCmisType();
    }

    /**
     * Returns whether or not this client is already connected to a repository
     * that supports versionable documents.
     */
    public boolean isConnected() {
        return currentSession != null && currentFolder != null && versionableCmisType != null;
    }

    /**
     * Returns the current folder.
     */
    public FolderView getCurrentFolder() {
        return new FolderView(currentFolder);
    }

    /**
     * Changes the current working directory.
     *
     * @param path the path to navigate to.
     */
    public void navigateTo(String path) {
        if (path.equals("..") && !currentFolder.isRootFolder()) {
            currentFolder = currentFolder.getFolderParent();
        } else if (!path.equals(".")) {
            if (!path.startsWith("/")) {
                path = joinPath(currentFolder.getPath(), path);
            }
            currentFolder = getBareFolder(path);
        }
    }

    /**
     * Returns if {@code path} is a folder.
     */
    public boolean isFolder(String path) {
        return getObject(path) instanceof Folder;
    }

    /**
     * Returns if {@code path} is a document.
     */
    public boolean isDocument(String path) {
        return getObject(path) instanceof Document;
    }

    /**
     * Returns the file at the specified {@code path} or with the specified {@code objectId}.
     */
    public FileView getFile(String pathOrId) {
        return new FileView((FileableCmisObject) getObject(pathOrId));
    }

    /**
     * Returns the document at the specified {@code path} or with the specified {@code objectId}.
     */
    @Override
    public DocumentView getDocument(String pathOrId) {
        return new DocumentView(getBareDocument(pathOrId));
    }

    /**
     * Returns the folder at the specified {@code path}.
     */
    public FolderView getFolder(String pathOrId) {
        return new FolderView(getBareFolder(pathOrId));
    }

    /**
     * Creates a subfolder of the specified parent.
     *
     * @param parent the parent folder
     * @param name   the subfolder name
     * @return the created folder
     */
    public FolderView createFolder(String parent, String name) {
        Folder folder = FileUtils.createFolder(parent, name, null, currentSession);
        return new FolderView(folder);
    }

    /**
     * Creates a subfolder of the current folder.
     *
     * @param name the subfolder name
     * @return the created folder
     */
    public FolderView createFolder(String name) {
        return createFolder(currentFolder.getPath(), name);
    }

    /**
     * Uploads a document.
     *
     * @param parentIdOrPath       the id or path of the parent folder
     * @param fileName             the source file
     * @param mimeType             the MIME Type of the source file
     * @param inputStream          the input stream
     * @param length               the source file length
     * @param versioningState      the versioning state
     * @param additionalProperties additional properties for the uploaded files
     * @return the uploaded document
     */
    public DocumentView upload(String parentIdOrPath, String fileName, String mimeType,
                               InputStream inputStream, BigInteger length, VersioningState versioningState,
                               String checkInComment, Map<? extends String, ?> additionalProperties) {
        String cmisType = versionableCmisType;
        Folder parentFolder = getBareFolder(parentIdOrPath);

        if (mimeType == null) {
            mimeType = MimeTypes.getMIMEType(fileName);
        }

        // Setup basic properties
        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, cmisType);
        properties.put(PropertyIds.NAME, fileName);

        // Setup cool properties
        if (additionalProperties != null) {
            properties.putAll(additionalProperties);
        }

        ContentStream contentStream = new ContentStreamImpl(fileName, length, mimeType, inputStream);

        Document document = null;

        String documentPath = joinPath(parentFolder.getPath(), fileName);

        try {
            if (exists(documentPath)) {
                // Create new version of an existing document
                document = checkout(documentPath);
                document.checkIn(versioningState == VersioningState.MAJOR, null, contentStream, checkInComment);
                document = getBareDocument(documentPath);
            } else {
                // Create new document
                document = parentFolder.createDocument(properties, contentStream, versioningState);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new CmisRuntimeException("Cannot close source stream!", e);
                }
            }
        }

        return new DocumentView(document);
    }

    /**
     * Deletes all versions of a document.
     *
     * @param documentPath the absolute path of the document to delete
     */
    public void deleteDocument(String documentPath) {
        getBareDocument(documentPath).deleteAllVersions();
    }

    /**
     * Deletes all versions of a document.
     *
     * @param document the document to delete
     */
    public void deleteDocument(DocumentView document) {
        deleteDocument(document.getId());
    }

    /**
     * Deletes a single version of a document.
     *
     * @param documentPath the absolute path of the document
     * @param versionLabel the version of the document to delete
     * @see DocumentView#getVersionLabel()
     */
    public void deleteDocument(String documentPath, String versionLabel) {
        DocumentView documentView = new DocumentView(getBareDocument(documentPath)).getObjectOfVersion(versionLabel);
        getBareDocument(documentView.getId()).delete(false);
    }

    /**
     * Deletes a single version of a document.
     *
     * @param document     the document to delete
     * @param versionLabel the version of the document to delete
     * @see DocumentView#getVersionLabel()
     */
    public void deleteDocument(DocumentView document, String versionLabel) {
        deleteDocument(document.getId(), versionLabel);
    }

    /**
     * Recursively delete a folder.
     *
     * @param folderPath path of the folder to delete
     * @return the paths of folder and documents that could not be deleted
     */
    public Collection<String> deleteFolder(String folderPath) {
        List<String> failed = getBareFolder(folderPath).deleteTree(true, UnfileObject.DELETE, true);
        return new _map<String, String>() {
            @Override
            protected String process(String objectId) {
                if (isFolder(objectId)) {
                    return getBareFolder(objectId).getPath();
                } else {
                    // Ignore multiple paths
                    return getBareDocument(objectId).getPaths().get(0);
                }
            }
        }.on(failed);
    }

    /**
     * Recursively delete a folder.
     *
     * @param folder the folder to delete
     * @return the paths of folder and documents that could not be deleted
     */
    public Collection<String> deleteFolder(FolderView folder) {
        return deleteFolder(folder.getPath());
    }

    /**
     * Searches all versions of every document in the current CMIS repository.
     *
     * @param name       a string that has to be contained in the name of the document
     * @param text       a string that has to be contained in the text of the document
     * @param properties the additional properties the document must match
     * @return an {@link ItemIterable} of documents matching the query
     */
    public ItemIterable<DocumentView> search(String name, String text, Collection<PropertyMatcher> properties) {
        // Build query
        String type = BaseTypeId.CMIS_DOCUMENT.value();
        QueryBuilder queryBuilder = new QueryBuilder(this, currentSession)
                .select(type, PropertyIds.OBJECT_ID)
                .from(BaseTypeId.CMIS_DOCUMENT.value());

        if (name != null) {
            queryBuilder = queryBuilder.where(new PropertyMatcher(PropertyIds.NAME,
                    QueryOperator.LIKE, PropertyType.STRING, name));
        }

        if (text != null) {
            queryBuilder = queryBuilder.whereContains(text);
        }

        if (properties != null) {
            queryBuilder = queryBuilder.where(properties);
        }

        // Execute query
        return queryBuilder.executeQuery(true);
    }

    /**
     * Searches all versions of every document in the current CMIS repository.
     *
     * @param name a string that has to be contained in the name of the document
     * @param text a string that has to be contained in the text of the document
     * @return an {@link ItemIterable} of documents matching the query
     */
    public ItemIterable<DocumentView> search(String name, String text) {
        return search(name, text, null);
    }

    /**
     * Returns a {@link com.github.atave.VaadinCmisBrowser.cmis.api.QueryBuilder}
     * to interactively build queries.
     */
    public QueryBuilder getQueryBuilder() {
        return new QueryBuilder(this, currentSession);
    }
}
