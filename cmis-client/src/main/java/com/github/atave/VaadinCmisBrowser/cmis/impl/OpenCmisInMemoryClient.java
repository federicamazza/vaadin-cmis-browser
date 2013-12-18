package com.github.atave.VaadinCmisBrowser.cmis.impl;

import com.github.atave.VaadinCmisBrowser.cmis.api.Config;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;

public class OpenCmisInMemoryClient extends HttpAuthCmisClient {

    private static final String ATOMPUB_URL = "opencmis.inmemory.atompub.url";

    public OpenCmisInMemoryClient(String user, String password) {
        super(user, password);
    }

    /**
     * Constructs an {@code OpenCmisInMemoryClient} with testing credentials.
     */
    OpenCmisInMemoryClient() {
        this("test_user", "test_password");
    }

    /**
     * Returns the AtomPub binding URL for this client.
     */
    @Override
    protected String getAtomPubUrl() {
        return Config.get(ATOMPUB_URL);
    }

    /**
     * Returns the {@link org.apache.chemistry.opencmis.client.api.SessionFactory} to use.
     */
    @Override
    protected SessionFactory getSessionFactory() {
        return SessionFactoryImpl.newInstance();
    }
}
