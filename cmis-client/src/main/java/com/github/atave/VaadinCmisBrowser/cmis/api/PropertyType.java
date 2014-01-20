package com.github.atave.VaadinCmisBrowser.cmis.api;


import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Session;

import java.util.Date;

import static com.github.atave.VaadinCmisBrowser.cmis.api.QueryOperator.*;

/**
 * An enumeration of property types.
 */
public enum PropertyType {

    STRING(EQUALS, NOT_EQUALS, LIKE, NOT_LIKE, CONTAINS),
    STRING_SET(IN, NOT_IN, ANY_IN),

    NUMBER(EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS),
    NUMBER_SET(IN, NOT_IN, ANY_IN),

    BOOLEAN(EQUALS),

    DATETIME(EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS),
    DATETIME_SET(IN, NOT_IN, ANY_IN),

    ID(EQUALS, NOT_EQUALS),
    ID_SET(IN, NOT_IN, ANY_IN),

    URI(EQUALS, NOT_EQUALS, LIKE, NOT_LIKE),
    URI_SET(IN, NOT_IN, ANY_IN);

    private final QueryOperator[] supportedOperators;

    PropertyType(QueryOperator... supportedOperators) {
        this.supportedOperators = supportedOperators;
    }

    /**
     * Returns the operators supported by this property type.
     */
    public QueryOperator[] getSupportedOperators() {
        return supportedOperators;
    }

    /**
     * Returns whether the specified operator is supported.
     */
    public boolean supports(QueryOperator operator) {
        for (QueryOperator queryOperator : supportedOperators) {
            if (queryOperator == operator) {
                return true;
            }
        }
        return false;
    }

    /**
     * Formats the values of a query fragment.
     */
    String format(String fragment, Object[] values, Session session) {
        QueryStatement stmt = session.createQueryStatement(fragment);

        switch (this) {
            case STRING:
            case URI:
            case ID:
                stmt.setString(1, (String[]) values[0]);
                break;

            case STRING_SET:
            case URI_SET:
            case ID_SET:
                for (int i = 1; i < values.length; ++i) {
                    stmt.setString(i, (String[]) values[i]);
                }
                break;

            case NUMBER:
                stmt.setNumber(1, (Number[]) values[0]);
                break;

            case NUMBER_SET:
                for (int i = 1; i < values.length; ++i) {
                    stmt.setNumber(i, (Number[]) values[i]);
                }
                break;

            case BOOLEAN:
                stmt.setBoolean(1, (boolean[]) values[0]);
                break;

            case DATETIME:
                stmt.setDateTime(1, (Date[]) values[0]);
                break;

            case DATETIME_SET:
                for (int i = 1; i < values.length; ++i) {
                    stmt.setDateTime(i, (Date[]) values[i]);
                }
                break;
        }

        return stmt.toQueryString();
    }
}
