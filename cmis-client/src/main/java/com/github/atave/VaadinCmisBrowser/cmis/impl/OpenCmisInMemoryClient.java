package com.github.atave.VaadinCmisBrowser.cmis.impl;

import com.github.atave.VaadinCmisBrowser.cmis.api.Config;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;

public class OpenCmisInMemoryClient extends HttpAuthCmisClient {

    public static final String ATOMPUB_URL = "opencmis.inmemory.atompub.url";

    /**
     * Creates a new {@code OpenCmisInMemoryClient}.
     *
     * @param user       the client's user
     * @param password   the client's password
     * @param atomPubUrl the client's AtomPub binding URL
     */
    public OpenCmisInMemoryClient(String user, String password, String atomPubUrl) {
        super(user, password, atomPubUrl);
    }

    /**
     * Creates a new {@code OpenCmisInMemoryClient} with the default AtomPub binding URL.
     *
     * @param user     the client's user
     * @param password the client's password
     */
    public OpenCmisInMemoryClient(String user, String password) {
        this(user, password, Config.get(ATOMPUB_URL));
    }

    /**
     * Returns the {@link org.apache.chemistry.opencmis.client.api.SessionFactory} to use.
     */
    @Override
    protected SessionFactory getSessionFactory() {
        return SessionFactoryImpl.newInstance();
    }
}
