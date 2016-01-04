package io.baratine.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.async.client.MongoIterable;

import io.baratine.core.Result;
import io.baratine.core.ServiceRef;
import io.baratine.mongodb.client.BargoCollection;
import io.baratine.mongodb.client.BargoCollectionSync;
import io.baratine.mongodb.client.BargoDatabase;

public class BargoDatabaseService implements BargoDatabase
{
  private BargoClientService _client;
  private MongoDatabase _db;

  private BargoCursorServiceImpl _cursorService;

  private HashMap<String, ServiceRef> _collectionMap
    = new HashMap<>();

  public BargoDatabaseService(BargoClientService client, MongoDatabase db)
  {
    _client = client;

    _db = db;

    _cursorService = new BargoCursorServiceImpl();
    _cursorService.init(ServiceRef.current().pin(_cursorService));
  }

  //public void createCollection(String name, CreateCollectionOptions, Result<Void>);

  public void createCollection(String name, Result<Void> result)
  {
    _db.createCollection(name, (value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(value);
      }
    });
  }

  public void drop(Result<Void> result)
  {
    _db.drop((value, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        for (ServiceRef ref : _collectionMap.values()) {
          ref.close();
        }

        _collectionMap.clear();

        _client.remove(_db.getName());

        result.complete(value);
      }
    });
  }

  public void getCodecRegistry(Result<CodecRegistry> result)
  {
    result.complete(_db.getCodecRegistry());
  }

  public void getCollection(String name, Result<BargoCollection<Document>> result)
  {
    ServiceRef ref = _collectionMap.get(name);

    if (ref == null) {
      MongoCollection<Document> col = _db.getCollection(name);

      BargoCollectionService<Document> serviceCollection = new BargoCollectionService<>(col);

      ref = ServiceRef.current().pin(serviceCollection);

      _collectionMap.put(name, ref);
    }

    result.complete(ref.as(BargoCollection.class));
  }

  public BargoCollectionSync<Document> getCollectionSync(String name)
  {
    ServiceRef ref = _collectionMap.get(name);

    if (ref == null) {
      MongoCollection<Document> col = _db.getCollection(name);

      BargoCollectionService<Document> serviceCollection = new BargoCollectionService<>(col);

      ref = ServiceRef.current().pin(serviceCollection);

      _collectionMap.put(name, ref);
    }

    return ref.as(BargoCollectionSync.class);
  }

  public void getName(Result<String> result)
  {
    result.complete(_db.getName());
  }

  public void getReadPreference(Result<ReadPreference> result)
  {
    result.complete(_db.getReadPreference());
  }

  public void getWriteConcern(Result<WriteConcern> result)
  {
    result.complete(_db.getWriteConcern());
  }

  public void listCollectionNames(Result<List<String>> result)
  {
    MongoIterable<String> iter = _db.listCollectionNames();

    iter.into(new ArrayList<String>(), (list, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(list);
      }
    });
  }

  //public void listCollections(Class<TResult> cls, Result<List> result);

  //public void runCommand(...);

  public void withCodecRegistry(CodecRegistry codecRegistry, Result<Void> result)
  {
    _db = _db.withCodecRegistry(codecRegistry);

    result.complete(null);
  }

  public void withReadPreference(ReadPreference readPreference, Result<Void> result)
  {
    _db = _db.withReadPreference(readPreference);

    result.complete(null);
  }

  public void withWriteConcern(WriteConcern writeConcern, Result<Void> result)
  {
    _db = _db.withWriteConcern(writeConcern);

    result.complete(null);
  }
}
