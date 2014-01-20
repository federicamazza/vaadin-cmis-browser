package com.github.atave.VaadinCmisBrowser.cmis.api;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A wrapper for an {@link org.apache.chemistry.opencmis.client.api.ItemIterable}
 * of {@link org.apache.chemistry.opencmis.client.api.QueryResult}s.
 */
public class QueryResults implements ItemIterable<DocumentView> {

    private final DocumentFetcher documentFetcher;
    private final ItemIterable<QueryResult> delegate;

    QueryResults(DocumentFetcher documentFetcher, ItemIterable<QueryResult> delegate) {
        this.documentFetcher = documentFetcher;
        this.delegate = delegate;
    }

    /**
     * Skip to position within CMIS collection
     *
     * @param position items to skip
     * @return iterable whose starting point is the specified skip to position.
     * This iterable <em>may</em> be the same as {@code this}
     */
    @Override
    public ItemIterable<DocumentView> skipTo(long position) {
        return new QueryResults(documentFetcher, delegate.skipTo(position));
    }

    /**
     * Gets an iterable for the current sub collection within the CMIS collection using
     * default maximum number of items
     *
     * @return iterable for current page
     */
    @Override
    public ItemIterable<DocumentView> getPage() {
        return new QueryResults(documentFetcher, delegate.getPage());
    }

    /**
     * Gets an iterable for the current sub collection within the CMIS collection
     *
     * @param maxNumItems maximum number of items the sub collection will contain
     * @return iterable for current page
     */
    @Override
    public ItemIterable<DocumentView> getPage(int maxNumItems) {
        return new QueryResults(documentFetcher, delegate.getPage(maxNumItems));
    }

    /**
     * A wrapper iterator that converts a {@link org.apache.chemistry.opencmis.client.api.QueryResult}
     * into a {@link DocumentView} using the {@code objectId}.
     */
    private class _Iterator implements Iterator<DocumentView> {

        private final Iterator<QueryResult> delegate;

        private _Iterator(Iterator<QueryResult> delegate) {
            this.delegate = delegate;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public DocumentView next() {
            QueryResult queryResult = delegate.next();
            String objectId = (String) queryResult.getPropertyByQueryName(PropertyIds.OBJECT_ID).getFirstValue();
            return documentFetcher.getDocument(objectId);
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         */
        @Override
        public void remove() {
            delegate.remove();
        }
    }

    @Override
    public Iterator<DocumentView> iterator() {
        return new _Iterator(delegate.iterator());
    }

    /**
     * Returns the number of items fetched for the current page.
     *
     * @return number of items for currently fetched collection
     */
    @Override
    public long getPageNumItems() {
        return delegate.getPageNumItems();
    }

    /**
     * Returns whether the repository contains additional items beyond the page of
     * items already fetched.
     *
     * @return true => further page requests will be made to the repository
     */
    @Override
    public boolean getHasMoreItems() {
        return delegate.getHasMoreItems();
    }

    /**
     * Returns the total number of items. If the repository knows the total
     * number of items in a result set, the repository SHOULD include the number
     * here. If the repository does not know the number of items in a result
     * set, this parameter SHOULD not be set. The value in the parameter MAY NOT
     * be accurate the next time the client retrieves the result set or the next
     * page in the result set.
     *
     * @return total number of items or (-1)
     */
    @Override
    public long getTotalNumItems() {
        return delegate.getTotalNumItems();
    }
}
