package io.baratine.mongodb.client;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.mongodb.Block;
import com.mongodb.Function;

import io.baratine.core.Result;
import io.baratine.mongodb.client.BargoCursor.BargoCursorWrapper;

public interface BargoIterable<T>
{
  void batchCursor(Result<BargoCursor<T>> result);

  void batchSize(int batchSize, Result<Void> result);

  void first(Result<T> result);

  void forEach(Block<? super T> block, Result<Void> result);

  <A extends Collection<? super T>> void into(A target, Result<A> result);

  <U> BargoIterable<U> map(Function<T,U> mapper);

  public static class BargoIterableImpl<T> implements BargoIterable<T> {
    protected long _id;
    protected BargoIterableService _service;

    public BargoIterableImpl(long id, BargoIterableService service)
    {
      _id = id;
      _service = service;
    }

    public void batchCursor(Result<BargoCursor<T>> result)
    {
      _service.batchCursor(_id, result);
    }

    public void batchSize(int batchSize, Result<Void> result)
    {
      _service.batchSize(_id, batchSize, result);
    }

    public void first(Result<T> result)
    {
      _service.first(_id, result);
    }

    public void forEach(Block<? super T> block, Result<Void> result)
    {
      _service.forEach(_id, block, result);
    }

    public <A extends Collection<? super T>> void into(A target, Result<A> result)
    {
      _service.into(_id, target, result);
    }

    public <U> BargoIterable<U> map(Function<T,U> mapper)
    {
      return new BargoIterableMapper<T,U>(this, mapper);
    }
  }

  public static class BargoIterableMapper<T,U> implements BargoIterable<U> {
    private BargoIterable<T> _iter;
    private Function<T,U> _mapper;

    public BargoIterableMapper(BargoIterable<T> iter, Function<T,U> mapper)
    {
      _iter = iter;
      _mapper = mapper;
    }

    public void batchCursor(Result<BargoCursor<U>> result)
    {
      _iter.batchCursor(Result.from(cursor -> {
        result.complete(new BargoCursorWrapper<T,U>(cursor, _mapper));
      }, t -> {
        result.fail(t);
      }));
    }

    public void batchSize(int batchSize, Result<Void> result)
    {
      _iter.batchSize(batchSize, result);
    }

    public void first(Result<U> result)
    {
      _iter.first(Result.from(value -> {
        result.complete(_mapper.apply(value));
      }, t -> {
        result.fail(t);
      }));
    }

    public void forEach(Block<? super U> block, Result<Void> result)
    {
      _iter.forEach(value -> {
        block.apply(_mapper.apply(value));
      }, result);
    }

    public <A extends Collection<? super U>> void into(A target, Result<A> result)
    {
      _iter.into(new CollectionWrapper(target, _mapper), Result.from(value -> {
        result.complete(target);
      }, t -> {
        result.fail(t);
      }));
    }

    public <V> BargoIterable<V> map(Function<U,V> mapper)
    {
      return new BargoIterableMapper<U,V>(this, mapper);
    }
  }

  static class CollectionWrapper<T,U> extends AbstractCollection<T> {
    private Collection<U> _col;
    private Function<T,U> _mapper;

    public CollectionWrapper(Collection<U> col, Function<T,U> mapper)
    {
      _col = col;
      _mapper = mapper;
    }

    public boolean add(T value)
    {
      U u = _mapper.apply(value);

      return _col.add(u);
    }

    public boolean addAll(Collection<? extends T> c)
    {
      for (T value : c) {
        U u = _mapper.apply(value);

        _col.add(u);
      }

      return true;
    }

    public int size()
    {
      return _col.size();
    }

    public Iterator<T> iterator()
    {
      throw new UnsupportedOperationException();
    }
  }
}
