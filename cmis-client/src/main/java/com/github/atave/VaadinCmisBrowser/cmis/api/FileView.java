package com.github.atave.VaadinCmisBrowser.cmis.api;

import com.github.atave.junderscore._map;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.enums.Action;

import java.util.Collection;
import java.util.GregorianCalendar;


/**
 * A simple read-only view of a {@link org.apache.chemistry.opencmis.client.api.FileableCmisObject}.
 */
public class FileView {

    protected final FileableCmisObject delegate;

    public FileView(FileableCmisObject delegate) {
        this.delegate = delegate;
    }

    /**
     * Casts itself as a {@link DocumentView}.
     */
    public DocumentView asDocument() {
        return (DocumentView) this;
    }

    /**
     * Casts itself as a {@link FolderView}.
     */
    public FolderView asFolder() {
        return (FolderView) this;
    }

    /**
     * Returns whether or not this object supports the specified {@code action}.
     */
    public boolean can(Action action) {
        return delegate.getAllowableActions().getAllowableActions().contains(action);
    }

    /**
     * Returns the object id.
     */
    public String getId() {
        return delegate.getId();
    }

    /**
     * Returns the name of this CMIS object (CMIS property
     * <code>cmis:name</code>).
     */
    public String getName() {
        return delegate.getName();
    }

    /**
     * Returns the description of this CMIS object (CMIS property
     * <code>cmis:description</code>).
     */
    public String getDescription() {
        return delegate.getDescription();
    }

    /**
     * Returns the user who created this CMIS object (CMIS property
     * <code>cmis:createdBy</code>).
     */
    public String getCreatedBy() {
        return delegate.getCreatedBy();
    }

    /**
     * Returns the timestamp when this CMIS object has been created (CMIS
     * property <code>cmis:creationDate</code>).
     */
    public GregorianCalendar getCreationDate() {
        return delegate.getCreationDate();
    }

    /**
     * Returns the user who modified this CMIS object (CMIS property
     * <code>cmis:lastModifiedBy</code>).
     */
    public String getLastModifiedBy() {
        return delegate.getLastModifiedBy();
    }

    /**
     * Returns the timestamp when this CMIS object has been modified (CMIS
     * property <code>cmis:lastModificationDate</code>).
     */
    public GregorianCalendar getLastModificationDate() {
        return delegate.getLastModificationDate();
    }

    /**
     * Returns the parents of this object.
     *
     * @return the list of parent folders of this object or an empty list if
     * this object is unfiled or if this object is the root folder
     */
    public Collection<FolderView> getParents() {
        return new _map<FolderView, Folder>() {
            @Override
            protected FolderView process(Folder object) {
                return new FolderView(object);
            }
        }.on(delegate.getParents());
    }

    /**
     * Returns the path of this object.
     */
    public String getPath() {
        return delegate.getPaths().get(0);
    }

    /**
     * Returns if the file is a document.
     */
    public boolean isDocument() {
        return delegate instanceof Document;
    }

    /**
     * Returns if the file is a folder.
     */
    public boolean isFolder() {
        return delegate instanceof Folder;
    }

}
