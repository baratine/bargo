package io.baratine.mongodb.client;

import java.util.concurrent.TimeUnit;

import org.bson.conversions.Bson;

import com.mongodb.CursorType;

import io.baratine.core.Result;

public interface BargoFindIterable<T> extends BargoIterable<T>
{
  void cursorType(CursorType cursorType, Result<Void> result);

  void filter(Bson filter, Result<Void> result);

  void limit(int limit, Result<Void> result);

  void maxTime(long maxTime, TimeUnit timeUnit, Result<Void> result);

  void modifiers(Bson modifiers, Result<Void> result);

  void noCursorTimeout(boolean noCursorTimeout, Result<Void> result);

  //void oplogReplay(boolean opLogReplay, Result<Void> result);

  void partial(boolean partial, Result<Void> result);

  void projection(Bson projection, Result<Void> result);

  void skip(int skip, Result<Void> result);

  void sort(Bson sort, Result<Void> result);

  public static class BargoFindIterableImpl<T> extends BargoIterableImpl<T> implements BargoFindIterable<T> {
    public BargoFindIterableImpl(long id, BargoIterableService service)
    {
      super(id, service);
    }

    public void cursorType(CursorType cursorType, Result<Void> result)
    {
      _service.cursorType(_id, cursorType, result);
    }

    public void filter(Bson filter, Result<Void> result)
    {
      _service.filter(_id, filter, result);
    }

    public void limit(int limit, Result<Void> result)
    {
      _service.limit(_id, limit, result);
    }

    public void maxTime(long maxTime, TimeUnit timeUnit, Result<Void> result)
    {
      _service.maxTime(_id, maxTime, timeUnit, result);
    }

    public void modifiers(Bson modifiers, Result<Void> result)
    {
      _service.modifiers(_id, modifiers, result);
    }

    public void noCursorTimeout(boolean noCursorTimeout, Result<Void> result)
    {
      _service.noCursorTimeout(_id, noCursorTimeout, result);
    }

    //public void oplogReplay(boolean opLogReplay, Result<Void> result);

    public void partial(boolean partial, Result<Void> result)
    {
      _service.partial(_id, partial, result);
    }

    public void projection(Bson projection, Result<Void> result)
    {
      _service.projection(_id, projection, result);
    }

    public void skip(int skip, Result<Void> result)
    {
      _service.skip(_id, skip, result);
    }

    public void sort(Bson sort, Result<Void> result)
    {
      _service.sort(_id, sort, result);
    }
  }
}
