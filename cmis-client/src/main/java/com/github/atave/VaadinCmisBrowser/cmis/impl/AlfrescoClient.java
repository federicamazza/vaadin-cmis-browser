package com.github.atave.VaadinCmisBrowser.cmis.impl;

import com.github.atave.VaadinCmisBrowser.cmis.api.Config;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;


/**
 * A {@link com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient} implementation for Alfresco.
 */
public class AlfrescoClient extends HttpAuthCmisClient {

    private static final String ATOMPUB_URL = "alfresco.cmis.atompub.url";

    public AlfrescoClient(String user, String password) {
        super(user, password);

        // Alfresco only supports one repository
        connect(getRepositories().iterator().next().getId());
    }

    @Override
    protected String getAtomPubUrl() {
        return Config.get(ATOMPUB_URL);
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return SessionFactoryImpl.newInstance();
    }
}
