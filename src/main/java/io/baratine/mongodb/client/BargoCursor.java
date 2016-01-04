package io.baratine.mongodb.client;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.Function;

import io.baratine.core.Result;

public interface BargoCursor<T> extends Closeable
{
  void close();

  void close(Result<Void> result);

  void getBatchSize(Result<Integer> result);

  void isClosed(Result<Boolean> result);

  void next(Result<List<T>> result);

  public static class BargoCursorImpl<T> implements BargoCursor<T> {
    private long _id;
    private BargoCursorService _service;

    private boolean _isClosed;

    public BargoCursorImpl(long id, BargoCursorService service)
    {
      _id = id;
      _service = service;
    }

    public void close()
    {
      _service.close(_id, Result.ignore());
    }

    public void close(Result<Void> result)
    {
      _service.close(_id, Result.from(value -> {
        _isClosed = true;
      }, t -> {
        result.fail(t);
      }));
    }

    public void getBatchSize(Result<Integer> result)
    {
      _service.getBatchSize(_id, result);
    }

    public void isClosed(Result<Boolean> result)
    {
      if (_isClosed) {
        result.complete(_isClosed);
      }
      else {
        _service.isClosed(_id, result);
      }
    }

    public void next(Result<List<T>> result)
    {
      _service.next(_id, (Result) result);
    }

    @Override
    public String toString()
    {
      return getClass().getSimpleName() + "[" + _id + ",isClosed=" + _isClosed + "]";
    }
  }

  public static class BargoCursorWrapper<T,U> implements BargoCursor<U> {
    private BargoCursor<T> _cursor;
    private Function<T,U> _mapper;

    public BargoCursorWrapper(BargoCursor<T> cursor, Function<T,U> mapper)
    {
      _cursor = cursor;
      _mapper = mapper;
    }

    public void close()
    {
      _cursor.close();
    }

    public void close(Result<Void> result)
    {
      _cursor.close(result);
    }

    public void getBatchSize(Result<Integer> result)
    {
      _cursor.getBatchSize(result);
    }

    public void isClosed(Result<Boolean> result)
    {
      _cursor.isClosed(result);
    }

    public void next(Result<List<U>> result)
    {
      _cursor.next(Result.from(list -> {
        ArrayList<U> newList = new ArrayList<>();

        for (T value : list) {
          newList.add(_mapper.apply(value));
        }

        result.complete(newList);
      }, t -> {
        result.fail(t);
      }));
    }
  }
}
