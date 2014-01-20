package com.github.atave.VaadinCmisBrowser.cmis.api;

import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Session;

/**
 * An enumeration of operators allowed in CMIS queries.
 */
public enum QueryOperator {
    CONTAINS("CONTAINS"),
    EQUALS("="),
    NOT_EQUALS("<>"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    ANY_IN("IN"),
    IN("IN"),
    NOT_IN("NOT IN"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN(">="),
    GREATER_THAN_OR_EQUALS(">=");

    private final String value;

    QueryOperator(String value) {
        this.value = value;
    }

    private String getTemplate() {
        if (this == CONTAINS) {
            return value + "(?)";
        } else if (this == ANY_IN) {
            return "ANY ? IN (?)";
        } else if (this == IN || this == NOT_IN) {
            return "? " + value + " (?)";
        } else {
            return "? " + value + " ?";
        }
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Generates a formatted query fragment.
     *
     * @param matcher the matcher for this query fragment
     * @param session the session to use
     * @return a formatted query fragment
     */
    String getFragment(PropertyMatcher matcher, Session session) {
        QueryStatement stmt = session.createQueryStatement(getTemplate());

        if (this == CONTAINS) {
            // Obvious
            stmt.setStringContains(1, (String) matcher.getValues()[0]);
            return stmt.toQueryString();
        } else {
            stmt.setProperty(1, matcher.getPropertyType(), matcher.getProperty());

            if (this == LIKE || this == NOT_LIKE) {
                // Also obvious
                stmt.setStringLike(2, (String) matcher.getValues()[0]);
                return stmt.toQueryString();
            } else {
                // Delegate
                return matcher.getValueType().format(
                        stmt.toQueryString(),
                        matcher.getValues(),
                        session);
            }
        }
    }
}
