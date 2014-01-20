package com.github.atave.VaadinCmisBrowser.cmis.impl;


import com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient;
import com.github.atave.VaadinCmisBrowser.cmis.api.RepositoryView;
import com.github.atave.VaadinCmisBrowser.cmis.api.SessionParametersFactory;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link com.github.atave.VaadinCmisBrowser.cmis.api.CmisClient} that authenticates
 * with a username and password.
 */
public abstract class HttpAuthCmisClient extends CmisClient {

    private final String user;
    private final String password;
    private final String atomPubUrl;

    /**
     * Constructs an {@code HttpAuthCmisClient} with the provided credentials.
     */
    protected HttpAuthCmisClient(String user, String password, String atomPubUrl) {
        this.user = user;
        this.password = password;
        this.atomPubUrl = atomPubUrl;

        // Smart autoconnect
        Collection<RepositoryView> repositories = getRepositories();
        if (repositories.size() == 1) {
            connect(repositories.iterator().next().getId());
        }
    }

    /**
     * Returns the client's user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the client's password.
     */
    protected String getPassword() {
        return password;
    }

    /**
     * Returns the AtomPub binding URL for this client.
     */
    protected String getAtomPubUrl() {
        return atomPubUrl;
    }

    @Override
    protected SessionParametersFactory getSessionParametersFactory() {
        return new HttpAuthSessionParametersFactory();
    }

    /**
     * A {@link com.github.atave.VaadinCmisBrowser.cmis.api.SessionParametersFactory} for Basic HTTP authentication.
     */
    private class HttpAuthSessionParametersFactory extends SessionParametersFactory {
        @Override
        public Map<String, String> newInstance() {
            Map<String, String> parameters = new HashMap<>();

            // Credentials
            parameters.put(SessionParameter.USER, user);
            parameters.put(SessionParameter.PASSWORD, password);

            // Connection settings
            parameters.put(SessionParameter.ATOMPUB_URL, getAtomPubUrl());
            parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

            return parameters;
        }
    }
}
