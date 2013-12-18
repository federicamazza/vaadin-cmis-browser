package com.github.atave.VaadinCmisBrowser.cmis.api;

import org.apache.chemistry.opencmis.client.api.Repository;

/**
 * A simple read-only view of a {@link org.apache.chemistry.opencmis.client.api.Repository}.
 */
public class RepositoryView {

    private final org.apache.chemistry.opencmis.client.api.Repository delegate;

    public RepositoryView(Repository delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the repository id.
     */
    public String getId() {
        return delegate.getId();
    }

    /**
     * Returns the repository name.
     */
    public String getName() {
        return delegate.getName();
    }

    /**
     * Returns the repository description.
     */
    public String getDescription() {
        return delegate.getDescription();
    }

}
