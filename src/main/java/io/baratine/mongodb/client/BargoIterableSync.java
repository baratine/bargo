package io.baratine.mongodb.client;

import java.util.Collection;

import com.mongodb.Block;
import com.mongodb.Function;

public interface BargoIterableSync<T>
{
  BargoCursorSync<T> batchCursor();

  void batchSize(int batchSize);

  T first();

  void forEach(Block<? super T> block);

  <A extends Collection<? super T>> A into(A target);

  <U> BargoIterableSync<U> map(Function<T,U> mapper);

  public static class BargoIterableSyncImpl<T> implements BargoIterableSync<T> {
    protected long _id;
    protected BargoIterableServiceSync _service;

    public BargoIterableSyncImpl(long id, BargoIterableServiceSync service)
    {
      _id = id;
      _service = service;
    }

    public BargoCursorSync<T> batchCursor()
    {
      return _service.batchCursor(_id);
    }

    public void batchSize(int batchSize)
    {
      _service.batchSize(_id, batchSize);
    }

    public T first()
    {
      return _service.first(_id);
    }

    public void forEach(Block<? super T> block)
    {
      _service.forEach(_id, block);
    }

    public <A extends Collection<? super T>> A into(A target)
    {
      return _service.into(_id, target);
    }

    public <U> BargoIterableSync<U> map(Function<T,U> mapper)
    {
      throw new UnsupportedOperationException();
    }
  }
}
