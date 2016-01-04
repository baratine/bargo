package io.baratine.mongodb.client;

import java.util.concurrent.TimeUnit;

import org.bson.conversions.Bson;

import com.mongodb.CursorType;

public interface BargoFindIterableSync<T> extends BargoIterableSync<T>
{
  void cursorType(CursorType cursorType);

  void filter(Bson filter);

  void limit(int limit);

  void maxTime(long maxTime, TimeUnit timeUnit);

  void modifiers(Bson modifiers);

  void noCursorTimeout(boolean noCursorTimeout);

  //void oplogReplay(boolean opLogReplay);

  void partial(boolean partial);

  void projection(Bson projection);

  void skip(int skip);

  void sort(Bson sort);

  public static class BargoFindIterableSyncImpl<T> extends BargoIterableSyncImpl<T> implements BargoFindIterableSync<T> {
    public BargoFindIterableSyncImpl(long id, BargoIterableServiceSync service)
    {
      super(id, service);
    }

    public void cursorType(CursorType cursorType)
    {
      _service.cursorType(_id, cursorType);
    }

    public void filter(Bson filter)
    {
      _service.filter(_id, filter);
    }

    public void limit(int limit)
    {
      _service.limit(_id, limit);
    }

    public void maxTime(long maxTime, TimeUnit timeUnit)
    {
      _service.maxTime(_id, maxTime, timeUnit);
    }

    public void modifiers(Bson modifiers)
    {
      _service.modifiers(_id, modifiers);
    }

    public void noCursorTimeout(boolean noCursorTimeout)
    {
      _service.noCursorTimeout(_id, noCursorTimeout);
    }

    //public void oplogReplay(boolean opLogReplay);

    public void partial(boolean partial)
    {
      _service.partial(_id, partial);
    }

    public void projection(Bson projection)
    {
      _service.projection(_id, projection);
    }

    public void skip(int skip)
    {
      _service.skip(_id, skip);
    }

    public void sort(Bson sort)
    {
      _service.sort(_id, sort);
    }
  }
}
