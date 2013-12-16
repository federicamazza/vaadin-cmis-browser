package com.github.atave.cmis;

import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;

public class OpenCmisInMemoryClient extends CmisClient {

    private static final String ATOMPUB_URL = "opencmis.inmemory.atompub.url";

    /**
     * Returns the {@link org.apache.chemistry.opencmis.client.api.SessionFactory} to use.
     */
    @Override
    protected SessionFactory getSessionFactory() {
        return SessionFactoryImpl.newInstance();
    }

    /**
     * Returns the {@link com.github.atave.cmis.SessionParametersFactory} used to configure new {@code Session}s.
     */
    @Override
    protected SessionParametersFactory getSessionParametersFactory() {
        return new HttpAuthSessionParametersFactory("test", "test") {
            @Override
            protected String getAtomPubUrl() {
                return Config.get(ATOMPUB_URL);
            }
        };
    }
}
