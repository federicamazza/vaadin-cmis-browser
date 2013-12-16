package VaadinCmisBrowser.vaadin.utils;

import VaadinCmisBrowser.cmis.views.DocumentView;
import com.vaadin.server.StreamResource;

import java.io.InputStream;


/**
 * A {@link com.vaadin.server.StreamResource.StreamSource} for {@code Document}s.
 */
public class DocumentDownloader implements StreamResource.StreamSource {

    private DocumentView document;

    /**
     * Constructs a {@code DocumentDownloader}.
     *
     * @param document the document to download
     */
    public DocumentDownloader(DocumentView document) {
        this.document = document;
    }

    /**
     * Returns the document to download.
     */
    public DocumentView getDocument() {
        return document;
    }

    /**
     * Sets the document to download.
     */
    public void setDocument(DocumentView document) {
        this.document = document;
    }

    /**
     * Returns new input stream that is used for reading the resource.
     */
    @Override
    public InputStream getStream() {
        return document.download();
    }
}
