package com.github.atave.VaadinCmisBrowser.cmis.api;

import com.github.atave.junderscore.Lambda1;
import org.apache.chemistry.opencmis.client.api.Document;

import java.io.InputStream;
import java.util.Collection;

import static com.github.atave.junderscore.JUnderscore._;


/**
 * A simple read-only view of a {@link org.apache.chemistry.opencmis.client.api.Document}.
 */
public class DocumentView extends FileView {

    public DocumentView(Document delegate) {
        super(delegate);
    }

    private Document getDelegate() {
        return (Document) delegate;
    }

    /**
     * Downloads the document.
     *
     * @return the document's content as an {@link InputStream} that must be closed.
     */
    public InputStream download() {
        return getDelegate().getContentStream().getStream();
    }

    /**
     * Fetches all versions of this document.
     */
    public Collection<String> getAllVersions() {
        return _(getDelegate().getAllVersions()).map(new Lambda1<String, Document>() {
            @Override
            public String call(Document o) {
                return o.getVersionLabel();
            }
        });
    }

    /**
     * Returns the checkin comment (CMIS property
     * <code>cmis:checkinComment</code>).
     */
    public String getCheckinComment() {
        return getDelegate().getCheckinComment();
    }

    /**
     * Returns the document's MIME type or <code>null</code> if the document
     * has no content (CMIS property <code>cmis:contentStreamMimeType</code>).
     */
    public String getMimeType() {
        return getDelegate().getContentStreamMimeType();
    }

    /**
     * Fetches the latest major or minor version of this document.
     *
     * @param major if <code>true</code> the latest major version will be
     *              returned, otherwise the very last version will be returned
     * @return the latest document object
     */
    public DocumentView getObjectOfLatestVersion(boolean major) {
        return new DocumentView(getDelegate().getObjectOfLatestVersion(major));
    }

    /**
     * Returns the specified version of the document.
     *
     * @see #getVersionLabel()
     */
    public DocumentView getObjectOfVersion(final String versionLabel) {
        return new DocumentView(_(getDelegate().getAllVersions()).find(new Lambda1<Boolean, Document>() {
            @Override
            public Boolean call(Document o) {
                return o.getVersionLabel().equals(versionLabel);
            }
        }));
    }

    /**
     * Returns the size of the document in bytes or -1 if the document has no content
     * (CMIS property <code>cmis:contentStreamLength</code>).
     */
    public long getSize() {
        return getDelegate().getContentStreamLength();
    }

    /**
     * Returns the version label (CMIS property <code>cmis:versionLabel</code>).
     */
    public String getVersionLabel() {
        return getDelegate().getVersionLabel();
    }

    /**
     * Returns if this CMIS object is the latest version (CMIS property
     * <code>cmis:isLatestVersion</code>).
     */
    public Boolean isLatestVersion() {
        return getDelegate().isLatestVersion();
    }

    /**
     * Returns <code>true</code> if this CMIS object is the latest version (CMIS
     * property <code>cmis:isMajorVersion</code>).
     */
    public Boolean isMajorVersion() {
        return getDelegate().isMajorVersion();
    }

    /**
     * Returns <code>true</code> if this CMIS object is the latest major version
     * (CMIS property <code>cmis:isLatestMajorVersion</code>).
     */
    public Boolean isLatestMajorVersion() {
        return getDelegate().isLatestMajorVersion();
    }

}
