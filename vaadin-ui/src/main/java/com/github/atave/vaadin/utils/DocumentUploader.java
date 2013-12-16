package com.github.atave.vaadin.utils;

import com.github.atave.cmis.CmisClient;
import com.github.atave.cmis.views.DocumentView;
import com.vaadin.ui.Upload;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A bridge between Vaadin uploads and CMIS uploads.
 */
public abstract class DocumentUploader implements Upload.Receiver, Upload.SucceededListener {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final CmisClient client;
    private final String parent;
    private String fileName;
    private VersioningState versioningState;
    private String checkInComment;
    private final Map<String, Object> metadata = new HashMap<String, Object>();

    private DocumentView documentView;

    /**
     * Creates a new {@link DocumentUploader}.
     *
     * @param client the CMIS client
     * @param parent the parent the uploaded file will have
     */
    public DocumentUploader(CmisClient client, String parent) {
        this.client = client;
        this.parent = parent;
    }

    /**
     * Invoked when a new upload arrives.
     *
     * @param filename the desired filename of the upload, usually as specified
     *                 by the client.
     * @param mimeType the MIME type of the uploaded file.
     * @return Stream to which the uploaded file should be written.
     */
    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        return outputStream;
    }

    /**
     * Invoked when the upload reached the Web Server (not the CMIS server).
     *
     * @param event the Upload successful event.
     */
    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        if (fileName == null) {
            fileName = event.getFilename();
        }

        String mimeType = event.getMIMEType();

        long length = event.getLength();

        InputStream stream = new ByteArrayInputStream(outputStream.toByteArray());

        documentView = client.upload(parent, fileName, mimeType, stream,
                BigInteger.valueOf(length), versioningState, checkInComment, metadata);

        onCmisUploadReceived(documentView);
    }

    /**
     * Invoked when the document has been uploaded on the CMIS server.
     *
     * @param documentView the uploaded document
     */
    protected abstract void onCmisUploadReceived(DocumentView documentView);

    /**
     * Returns the check-in comment.
     */
    public String getCheckInComment() {
        return checkInComment;
    }

    /**
     * Returns the CMIS Client.
     */
    public CmisClient getClient() {
        return client;
    }

    /**
     * Returns the filename.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the parent of the uploaded file.
     */
    public String getParent() {
        return parent;
    }

    /**
     * Returns an immutable copy of the uploaded file's additional properties.
     */
    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    /**
     * Returns the uploaded document.
     */
    public DocumentView getDocumentView() {
        return documentView;
    }

    /**
     * Returns the versioning state.
     */
    public VersioningState getVersioningState() {
        return versioningState;
    }

    /**
     * Sets the check-in comment.
     */
    public void setCheckInComment(String checkInComment) {
        this.checkInComment = checkInComment;
    }

    /**
     * Sets the filename.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Sets the versioning state.
     */
    public void setVersioningState(VersioningState versioningState) {
        this.versioningState = versioningState;
    }

    /**
     * Get an additional property of the uploaded file.
     */
    public Object getProperty(String propertyId) {
        return metadata.get(propertyId);
    }

    /**
     * Set an additional property on the uploaded file.
     */
    public void setProperty(String propertyId, Object value) {
        metadata.put(propertyId, value);
    }

}
