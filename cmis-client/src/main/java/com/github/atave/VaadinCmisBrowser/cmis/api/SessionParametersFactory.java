package com.github.atave.VaadinCmisBrowser.cmis.api;

import org.apache.chemistry.opencmis.commons.SessionParameter;

import java.util.Map;


/**
 * A producer of {@link org.apache.chemistry.opencmis.commons.SessionParameter} bundles.
 */
public abstract class SessionParametersFactory {

    public abstract Map<String, String> newInstance();

    public Map<String, String> newInstance(String repositoryId) {
        Map<String, String> sessionParameters = newInstance();
        sessionParameters.put(SessionParameter.REPOSITORY_ID, repositoryId);

        return sessionParameters;
    }
}
