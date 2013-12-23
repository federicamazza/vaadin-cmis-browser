package com.github.atave.VaadinCmisBrowser.cmis.api;

public interface DocumentFetcher {

    /**
     * Returns the document at the specified {@code path} or with the specified {@code objectId}.
     */
    DocumentView getDocument(String pathOrId);
}
