package com.github.atave.VaadinCmisBrowser.cmis.impl;

import com.github.atave.VaadinCmisBrowser.cmis.api.Config;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;


/**
 * A {@link com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient} implementation for Alfresco.
 */
public class AlfrescoClient extends HttpAuthCmisClient {

    private static final String ATOMPUB_URL = "alfresco.cmis.atompub.url";

    /**
     * Creates a new {@code AlfrescoClient}.
     *
     * @param user       the client's user
     * @param password   the client's password
     * @param atomPubUrl the client's AtomPub binding URL
     */
    public AlfrescoClient(String user, String password, String atomPubUrl) {
        super(user, password, atomPubUrl);
    }

    /**
     * Creates a new {@code AlfrescoClient} with the default AtomPub binding URL.
     *
     * @param user     the client's user
     * @param password the client's password
     */
    public AlfrescoClient(String user, String password) {
        this(user, password, Config.get(ATOMPUB_URL));
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return SessionFactoryImpl.newInstance();
    }
}
