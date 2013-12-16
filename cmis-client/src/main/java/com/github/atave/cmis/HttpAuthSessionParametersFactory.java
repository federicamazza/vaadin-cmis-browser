package com.github.atave.cmis;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.Map;


/**
 * A {@link SessionParametersFactory} for Basic HTTP authentication.
 */
public abstract class HttpAuthSessionParametersFactory extends SessionParametersFactory {

    private String user;
    private String password;

    public HttpAuthSessionParametersFactory(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    abstract protected String getAtomPubUrl();

    @Override
    public Map<String, String> newInstance() {
        Map<String, String> parameters = new HashMap<String, String>();

        // Credentials
        parameters.put(SessionParameter.USER, user);
        parameters.put(SessionParameter.PASSWORD, password);

        // Connection settings
        parameters.put(SessionParameter.ATOMPUB_URL, getAtomPubUrl());
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        return parameters;
    }


}
