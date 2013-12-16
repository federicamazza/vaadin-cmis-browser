package com.github.atave.cmis.alfresco;

import com.github.atave.cmis.Config;
import com.github.atave.cmis.HttpAuthSessionParametersFactory;

public class AlfrescoSessionParametersFactory extends HttpAuthSessionParametersFactory {

    private static final String ALFRESCO_ATOMPUB_URL = "alfresco.cmis.atompub.url";

    public AlfrescoSessionParametersFactory(String user, String password) {
        super(user, password);
    }

    @Override
    protected String getAtomPubUrl() {
        return Config.get(ALFRESCO_ATOMPUB_URL);
    }
}
