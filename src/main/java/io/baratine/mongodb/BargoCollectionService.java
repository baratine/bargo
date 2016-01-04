package io.baratine.mongodb;

import java.util.List;

import org.bson.Document;

import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoCollection;

import io.baratine.core.Result;
import io.baratine.core.ServiceRef;
import io.baratine.mongodb.client.BargoCollection;
import io.baratine.mongodb.client.BargoFindIterable;
import io.baratine.mongodb.client.BargoFindIterable.BargoFindIterableImpl;
import io.baratine.mongodb.client.BargoFindIterableSync;
import io.baratine.mongodb.client.BargoFindIterableSync.BargoFindIterableSyncImpl;
import io.baratine.mongodb.client.BargoIterableService;
import io.baratine.mongodb.client.BargoIterableServiceSync;

public class BargoCollectionService<T extends Document> implements BargoCollection<T>
{
  private MongoCollection<T> _col;

  private BargoCursorServiceImpl _cursorService;
  private ServiceRef _cursorServiceRef;

  public BargoCollectionService(MongoCollection<T> col)
  {
    _col = col;

    _cursorService = new BargoCursorServiceImpl();
    _cursorServiceRef = ServiceRef.current().pin(_cursorService);

    _cursorService.init(_cursorServiceRef);
  }

  public void find(Result<BargoFindIterable<T>> result)
  {
    FindIterable<T> iter = _col.find();

    long id = _cursorService.add((FindIterable) iter);

    result.complete(new BargoFindIterableImpl<T>(id, _cursorServiceRef.as(BargoIterableService.class)));
  }

  public BargoFindIterableSync<T> findSync()
  {
    FindIterable<T> iter = _col.find();

    long id = _cursorService.add((FindIterable) iter);

    return new BargoFindIterableSyncImpl<T>(id, _cursorServiceRef.as(BargoIterableServiceSync.class));
  }

  public void insertOne(T doc, Result<Void> result)
  {
    _col.insertOne(doc, (value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }

  public void insertMany(List<? extends T> documents, Result<Void> result)
  {
    _col.insertMany(documents, (value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }
}
