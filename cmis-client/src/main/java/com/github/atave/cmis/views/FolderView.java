package com.github.atave.cmis.views;

import com.github.atave.junderscore._map;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import java.util.Collection;


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
                if(object instanceof Document) {
                    return new DocumentView((Document) object);
                } else if(object instanceof Folder) {
                    return new FolderView((Folder) object);
                } else {
                    return null;
                }
            }
        }.on(getDelegate().getChildren());
    }

    /**
     * Gets the parent folder object
     *
     * @return the parent folder object
     * @throws CmisObjectNotFoundException if this folder is already the root folder.
     */
    public FolderView getParent() throws CmisObjectNotFoundException{
        Folder parent = getDelegate().getFolderParent();
        if(parent != null) {
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
