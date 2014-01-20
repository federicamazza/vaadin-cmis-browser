package com.github.atave.VaadinCmisBrowser.cmis.api;


import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

/**
 * A bundle of information used to build WHERE clauses in queries.
 *
 * @see com.github.atave.VaadinCmisBrowser.cmis.api.QueryBuilder
 */
public class PropertyMatcher {

    private final String propertyType;
    private final String property;
    private final QueryOperator operator;
    private final PropertyType valueType;
    private final Object[] values;

    /**
     * Creates a new bundle.
     *
     * @param propertyType the type of the property
     * @param property     the name of the property
     * @param operator     an operator supported by the type of the property
     * @param valueType    the type of the property
     * @param values       the value(s) of the property
     */
    public PropertyMatcher(String propertyType, String property, QueryOperator operator, PropertyType valueType, Object... values) {
        this.property = property;
        this.operator = operator;
        this.valueType = valueType;
        this.values = values;
        this.propertyType = propertyType;
    }

    /**
     * Creates a new bundle for matching documents.
     *
     * @param property  the name of the property
     * @param operator  an operator supported by the type of the property
     * @param valueType the type of the property
     * @param values    the value(s) of the property
     */
    public PropertyMatcher(String property, QueryOperator operator, PropertyType valueType, Object... values) {
        this(BaseTypeId.CMIS_DOCUMENT.value(), property, operator, valueType, values);
    }

    /**
     * Returns the type of the property.
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Returns the the name of the property.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Returns an operator supported by the type of the property.
     */
    public QueryOperator getOperator() {
        return operator;
    }

    /**
     * Returns the type of the property.
     */
    public PropertyType getValueType() {
        return valueType;
    }

    /**
     * Returns the value(s) of the property.
     */
    public Object[] getValues() {
        return values;
    }

    /**
     * Translates this bundle in a WHERE clause.
     *
     * @param session the session to use
     * @return a WHERE clause as a {@code String}
     */
    String getFragment(Session session) {
        return operator.getFragment(this, session);
    }
}
