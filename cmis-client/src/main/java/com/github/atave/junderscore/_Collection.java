package com.github.atave.junderscore;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class _Collection<T> implements Collection<T> {

    private final Collection<T> delegate;

    public _Collection(Collection<T> delegate) {
        this.delegate = delegate;
    }

    public Collection<T> value() {
        return delegate;
    }

    public <K> Map<K, Integer> countBy(final Lambda1<K, T> lambdaFunction) {
        return new _countBy<K, T>() {
            @Override
            protected K process(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public void each(final Lambda1<Void, T> lambdaFunction) {
        new _each<T>() {
            @Override
            protected void process(T object) {
                lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public boolean every(final Lambda1<Boolean, T> lambdaFunction) {
        return new _every<T>() {
            @Override
            protected boolean test(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public _Collection<T> filter(final Lambda1<Boolean, T> lambdaFunction) {
        return new _Collection<>(new _filter<T>() {
            @Override
            protected boolean test(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate));
    }

    public T find(final Lambda1<Boolean, T> lambdaFunction) {
        return new _find<T>() {
            @Override
            protected boolean test(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public <K> Map<K, Collection<T>> groupBy(final Lambda1<K, T> lambdaFunction) {
        return new _groupBy<K, T>() {
            @Override
            protected K process(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public <K> Map<K, T> indexBy(final Lambda1<K, T> lambdaFunction) {
        return new _indexBy<K, T>() {
            @Override
            protected K process(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public void invoke(final String methodName, final Object... args) throws InvocationTargetException, IllegalAccessException {
        _invoke.on(delegate, methodName, args);
    }

    public <R> _Collection<R> map(final Lambda1<R, T> lambdaFunction) {
        return new _Collection<>(new _map<R, T>() {
            @Override
            protected R process(T object) {
                return lambdaFunction.call((object));
            }
        }.on(delegate));
    }

    public T max(final Comparator<T> comparator) {
        return new _max<T>().on(delegate, comparator);
    }

    public T min(final Comparator<T> comparator) {
        return new _min<T>().on(delegate, comparator);
    }

    public <R> R reduce(final Lambda2<R, R, T> lambdaFunction, R memo) {
        return new _reduce<R, T>() {
            @Override
            protected R process(R memo, T object) {
                return lambdaFunction.call(memo, object);
            }
        }.on(delegate, memo);
    }

    public <R> R reduceRight(final Lambda2<R, R, T> lambdaFunction, R memo) {
        return new _reduceRight<R, T>() {
            @Override
            protected R process(R memo, T object) {
                return lambdaFunction.call(memo, object);
            }
        }.on(delegate, memo);
    }

    public _Collection<T> reject(final Lambda1<Boolean, T> lambdaFunction) {
        return new _Collection<>(new _reject<T>() {
            @Override
            protected boolean test(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate));
    }

    public _Collection<T> sample(int n) {
        return new _Collection<>(_sample.on(delegate, n));
    }

    public _Collection<T> shuffle() {
        return new _Collection<>(_shuffle.on(delegate));
    }

    public boolean some(final Lambda1<Boolean, T> lambdaFunction) {
        return new _some<T>() {
            @Override
            protected boolean test(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate);
    }

    public <R extends Comparable<R>> _Collection<R> sortBy(final Lambda1<R, T> lambdaFunction) {
        return new _Collection<>(new _sortBy<R, T>() {
            @Override
            protected R process(T object) {
                return lambdaFunction.call(object);
            }
        }.on(delegate));
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return delegate.toArray(a);
    }

    public boolean add(T t) {
        return delegate.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return delegate.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
