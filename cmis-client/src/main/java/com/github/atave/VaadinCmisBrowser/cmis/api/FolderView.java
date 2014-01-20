package com.github.atave.VaadinCmisBrowser.cmis.api;

import com.github.atave.junderscore.Lambda1;
import com.github.atave.junderscore._map;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import java.util.Collection;

import static com.github.atave.junderscore.JUnderscore._;


/**
 * A simple read-only view of a {@link org.apache.chemistry.opencmis.client.api.Folder}.
 */
public class FolderView extends FileView {

    public FolderView(Folder delegate) {
        super(delegate);
    }

    private Folder getDelegate() {
        return (Folder) delegate;
    }

    /**
     * Returns if the folder is the root folder.
     */
    public boolean isRootFolder() {
        return getDelegate().isRootFolder();
    }

    /**
     * Returns the children of this folder.
     */
    public Collection<FileView> getChildren() {
        return new _map<FileView, CmisObject>() {
            @Override
            protected FileView process(CmisObject object) {
                if (object instanceof Document) {
                    return new DocumentView((Document) object);
                } else if (object instanceof Folder) {
                    return new FolderView((Folder) object);
                } else {
                    return null;
                }
            }
        }.on(getDelegate().getChildren());
    }

    /**
     * Returns the documents whose first parent is this folder.
     */
    public Collection<DocumentView> getDocuments() {
        return _(getChildren()).filter(new Lambda1<Boolean, FileView>() {
            @Override
            public Boolean call(FileView o) {
                return o.isDocument();
            }
        }).map(new Lambda1<DocumentView, FileView>() {
            @Override
            public DocumentView call(FileView o) {
                return o.asDocument();
            }
        }).value();
    }

    /**
     * Returns the direct subfolders.
     */
    public Collection<FolderView> getFolders() {
        return _(getChildren()).filter(new Lambda1<Boolean, FileView>() {
            @Override
            public Boolean call(FileView o) {
                return o.isFolder();
            }
        }).map(new Lambda1<FolderView, FileView>() {
            @Override
            public FolderView call(FileView o) {
                return o.asFolder();
            }
        }).value();
    }

    /**
     * Gets the parent folder object
     *
     * @return the parent folder object
     * @throws CmisObjectNotFoundException if this folder is already the root folder.
     */
    public FolderView getParent() throws CmisObjectNotFoundException {
        Folder parent = getDelegate().getFolderParent();
        if (parent != null) {
            return new FolderView(parent);
        } else {
            throw new CmisObjectNotFoundException("Folder #" + getId() + " is already the root folder");
        }
    }

    /**
     * Returns the path of the folder.
     */
    @Override
    public String getPath() {
        return getDelegate().getPath();
    }

}
