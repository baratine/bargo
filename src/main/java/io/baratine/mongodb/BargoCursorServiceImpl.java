package io.baratine.mongodb;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.CursorType;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoIterable;

import io.baratine.core.Result;
import io.baratine.core.ServiceRef;
import io.baratine.mongodb.client.BargoCursor;
import io.baratine.mongodb.client.BargoCursor.BargoCursorImpl;
import io.baratine.mongodb.client.BargoCursorService;
import io.baratine.mongodb.client.BargoIterableService;

public class BargoCursorServiceImpl
  implements BargoCursorService, BargoIterableService
{
  private ServiceRef _me;

  private long _count;

  private HashMap<Long,MongoIterable<Object>> _iterMap
    = new HashMap<Long,MongoIterable<Object>>();

  private HashMap<Long,AsyncBatchCursor<Object>> _cursorMap
    = new HashMap<Long,AsyncBatchCursor<Object>>();

  public BargoCursorServiceImpl()
  {
  }

  public void init(ServiceRef me)
  {
    _me = me;
  }

  protected long add(MongoIterable<Object> iter)
  {
    long id = _count++;

    _iterMap.put(id, iter);

    return id;
  }

  //
  // AsyncBatchCursor
  //

  public void close(long id, Result<Void> result)
  {
    AsyncBatchCursor<Object> cursor = getCursor(id);

    cursor.close();

    _cursorMap.remove(id);
    _iterMap.remove(id);

    result.complete(null);
  }

  public void getBatchSize(long id, Result<Integer> result)
  {
    AsyncBatchCursor<Object> cursor = getCursor(id);

    result.complete(cursor.getBatchSize());
  }

  public void isClosed(long id, Result<Boolean> result)
  {
    AsyncBatchCursor<Object> cursor = getCursor(id);

    result.complete(cursor.isClosed());
  }

  public void next(long id, Result<List<Object>> result)
  {
    AsyncBatchCursor<Object> cursor = getCursor(id);

    cursor.next((value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }

  public void setBatchSize(long id, int batchSize, Result<Void> result)
  {
    AsyncBatchCursor<Object> cursor = getCursor(id);

    cursor.setBatchSize(batchSize);

    result.complete(null);
  }

  //
  // MongoIterable
  //

  public <T> void batchCursor(long id, Result<BargoCursor<T>> result)
  {
    AsyncBatchCursor<T> cursor = (AsyncBatchCursor) _cursorMap.get(id);

    if (cursor != null) {
      result.complete(new BargoCursorImpl<T>(id, _me.as(BargoCursorService.class)));
    }
    else {
      MongoIterable<Object> iter = getIterable(id);

      iter.batchCursor((value, t) -> {
        if (t != null) {
          result.fail(t);
        }
        else {
          _cursorMap.put(id, value);

          result.complete(new BargoCursorImpl<T>(id, _me.as(BargoCursorService.class)));
        }
      });
    }
  }

  public void batchSize(long id, int batchSize, Result<Void> result)
  {
    setBatchSize(id, batchSize, result);
  }

  public <T> void first(long id, Result<T> result)
  {
    MongoIterable<T> iter = (MongoIterable) getIterable(id);

    iter.first((value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }

  public <T> void forEach(long id, Block<? super T> block, Result<Void> result)
  {
    MongoIterable<T> iter = (MongoIterable) getIterable(id);

    iter.forEach(block, (value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }

  public <T,A extends Collection<? super T>> void into(long id, A target, Result<A> result)
  {
    MongoIterable<T> iter = (MongoIterable) getIterable(id);

    iter.into(target, (value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }

  //public void map(...);

  //
  // FindIterable
  //

  public void cursorType(long id, CursorType cursorType, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.cursorType(cursorType);

    result.complete(null);
  }

  public void filter(long id, Bson filter, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.filter(filter);

    result.complete(null);
  }

  public void limit(long id, int limit, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.limit(limit);

    result.complete(null);
  }

  public void maxTime(long id, long maxTime, TimeUnit timeUnit, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.maxTime(maxTime, timeUnit);

    result.complete(null);
  }

  public void modifiers(long id, Bson modifiers, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.modifiers(modifiers);

    result.complete(null);
  }

  public void noCursorTimeout(long id, boolean noCursorTimeout, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.noCursorTimeout(noCursorTimeout);

    result.complete(null);
  }

  public void partial(long id, boolean partial, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.partial(partial);

    result.complete(null);
  }

  public void projection(long id, Bson projection, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.projection(projection);

    result.complete(null);
  }

  public void skip(long id, int skip, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.skip(skip);

    result.complete(null);
  }

  public void sort(long id, Bson sort, Result<Void> result)
  {
    FindIterable iter = (FindIterable) getIterable(id);

    iter.sort(sort);

    result.complete(null);
  }

  //
  //
  //

  private MongoIterable<Object> getIterable(long id)
  {
    MongoIterable<Object> iter = _iterMap.get(id);

    if (iter != null) {
      return iter;
    }
    else {
      throw new RuntimeException("iterator not found for id: " + id);
    }
  }

  private AsyncBatchCursor<Object> getCursor(long id)
  {
    AsyncBatchCursor<Object> cursor = _cursorMap.get(id);

    if (cursor != null) {
      return cursor;
    }
    else {
      throw new RuntimeException("cursor not found for id: " + id);
    }
  }
}
