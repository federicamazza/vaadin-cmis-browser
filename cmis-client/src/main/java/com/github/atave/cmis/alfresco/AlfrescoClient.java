package com.github.atave.cmis.alfresco;

import com.github.atave.cmis.CmisClient;
import com.github.atave.cmis.SessionParametersFactory;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;


/**
 * A {@link com.github.atave.cmis.CmisClient} implementation for Alfresco.
 */
public class AlfrescoClient extends CmisClient {

    private final String user;
    private final String password;

    public AlfrescoClient(String user, String password) {
        this.user = user;
        this.password = password;

        // Alfresco only supports one repository
        connect(getRepositories().iterator().next().getId());
    }

    @Override
    protected SessionFactory getSessionFactory() {
        return SessionFactoryImpl.newInstance();
    }

    @Override
    protected SessionParametersFactory getSessionParametersFactory() {
        return new AlfrescoSessionParametersFactory(user, password);
    }
}
