package com.github.atave.VaadinCmisBrowser.cmis.api;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;

import java.util.HashSet;
import java.util.Set;

/**
 * A builder class for CMIS queries.
 */
public class QueryBuilder {

    private final DocumentFetcher documentFetcher;
    private final Session session;

    private final Set<String> selectFragments = new HashSet<>();
    private final Set<String> fromFragments = new HashSet<>();
    private final Set<String> whereFragments = new HashSet<>();
    private final Set<String> orderByFragments = new HashSet<>();

    /**
     * Creates a new {@code QueryBuilder}.
     *
     * @param session the session to use
     */
    public QueryBuilder(DocumentFetcher documentFetcher, Session session) {
        this.documentFetcher = documentFetcher;
        this.session = session;
    }

    private QueryStatement getQueryStatement(String stmt) {
        return session.createQueryStatement(stmt);
    }

    /**
     * Adds a SELECT clause. <br></br>
     * Additionally, it adds the related FROM clause if necessary.
     *
     * @param type     the type of the property
     * @param property the name of the property
     * @return a {@code QueryBuilder} object
     */
    public QueryBuilder select(String type, String property) {
        QueryStatement stmt = getQueryStatement("?");
        stmt.setProperty(1, type, property);
        selectFragments.add(stmt.toQueryString());
        return from(type);
    }

    /**
     * Adds a FROM clause.
     *
     * @param type the type of the property
     * @return a {@code QueryBuilder} object
     */
    public QueryBuilder from(String type) {
        fromFragments.add(type);
        return this;
    }

    /**
     * Adds a WHERE clause.
     *
     * @param propertyMatcher the bundle of information used to build the clause
     * @return a {@code QueryBuilder} object
     */
    public QueryBuilder where(PropertyMatcher propertyMatcher) {
        whereFragments.add(propertyMatcher.getFragment(session));
        return this;
    }

    /**
     * Adds multiple WHERE clauses.
     *
     * @param propertyMatchers the bundles of information used to build the clauses
     * @return a {@code QueryBuilder} object
     */
    public QueryBuilder where(Iterable<PropertyMatcher> propertyMatchers) {
        QueryBuilder queryBuilder = this;

        if (propertyMatchers != null) {
            for (PropertyMatcher propertyMatcher : propertyMatchers) {
                queryBuilder = where(propertyMatcher);
            }
        }

        return queryBuilder;
    }

    /**
     * Adds a 'CONTAINS' WHERE clause.
     *
     * @param text the text to search for, unescaped
     * @return a {@code QueryBuilder} object
     */
    public QueryBuilder whereContains(String text) {
        return where(new PropertyMatcher(null, null, QueryOperator.CONTAINS, PropertyType.STRING, text));
    }

    /**
     * Adds an ORDER BY clause.
     *
     * @param type     the type of the property
     * @param property the name of the property
     * @return a {@code QueryBuilder} object
     */
    public QueryBuilder orderBy(String type, String property) {
        QueryStatement stmt = getQueryStatement("?");
        stmt.setProperty(1, type, property);
        orderByFragments.add(stmt.toQueryString());
        return this;
    }

    private void concatenate(StringBuilder sb, Set<String> strings, String separator) {
        int i = strings.size();
        for (String selectFragment : strings) {
            sb.append(" ").append(selectFragment);
            if (--i > 0) {
                sb.append(separator);
            }
        }
    }

    /**
     * Returns the query as a {@code String}.
     */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder("SELECT");

        // SELECT
        if (selectFragments.isEmpty()) {
            sb.append(" *");
        } else {
            concatenate(sb, selectFragments, ",");
        }

        // FROM
        if (!fromFragments.isEmpty()) {
            sb.append(" FROM");
            concatenate(sb, fromFragments, ",");

            // WHERE
            if (!whereFragments.isEmpty()) {
                sb.append(" WHERE");
                concatenate(sb, whereFragments, " AND");
            }

            // ORDER BY
            if (!orderByFragments.isEmpty()) {
                sb.append(" ORDER BY");
                concatenate(sb, orderByFragments, ",");
            }
        } else {
            throw new CmisRuntimeException("No FROM clauses have been specified!");
        }

        return sb.toString();
    }

    /**
     * Executes the query.
     *
     * @param allVersions whether to query all document versions
     * @return an {@link org.apache.chemistry.opencmis.client.api.ItemIterable} of documents matching the query
     */
    public ItemIterable<DocumentView> executeQuery(boolean allVersions) {
        String query = getQueryString();
        return new QueryResults(documentFetcher, session.query(query, allVersions));
    }

    /**
     * Executes the query on all document versions.
     *
     * @return an {@link org.apache.chemistry.opencmis.client.api.ItemIterable} of documents matching the query
     */
    public ItemIterable<DocumentView> executeQuery() {
        return executeQuery(true);
    }
}
