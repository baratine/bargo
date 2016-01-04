package io.baratine.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.async.client.MongoIterable;

import io.baratine.core.OnDestroy;
import io.baratine.core.OnInit;
import io.baratine.core.Result;
import io.baratine.core.ServiceRef;
import io.baratine.mongodb.client.BargoClient;
import io.baratine.mongodb.client.BargoDatabase;
import io.baratine.mongodb.client.BargoDatabaseSync;

public class BargoClientService implements BargoClient
{
  private static final Logger _logger
    = Logger.getLogger(BargoClientService.class.getName());

  private String _uri;
  private MongoClient _client;

  private HashMap<String, ServiceRef> _databaseMap
    = new HashMap<>();

  public BargoClientService()
  {
  }

  public BargoClientService(String uri)
  {
    _uri = uri;
  }

  public void getUri(Result<String> result)
  {
    result.complete(_uri);
  }

  @OnInit
  public void onInit()
  {
    _client = MongoClients.create(_uri);
  }

  public BargoDatabaseSync getDatabaseSync(String name)
  {
    ServiceRef ref = _databaseMap.get(name);

    if (ref == null) {
      MongoDatabase db = _client.getDatabase(name);

      BargoDatabaseService serviceDatabase = new BargoDatabaseService(this, db);

      ref = ServiceRef.current().pin(serviceDatabase);

      _databaseMap.put(name, ref);
    }

    return ref.as(BargoDatabaseSync.class);
  }

  public void getDatabase(String name, Result<BargoDatabase> result)
  {
    ServiceRef ref = _databaseMap.get(name);

    if (ref == null) {
      MongoDatabase db = _client.getDatabase(name);

      BargoDatabaseService serviceDatabase = new BargoDatabaseService(this, db);

      ref = ServiceRef.current().pin(serviceDatabase);

      _databaseMap.put(name, ref);
    }

    result.complete(ref.as(BargoDatabase.class));
  }

  public void getSettings(Result<MongoClientSettings> result)
  {
    result.complete(_client.getSettings());
  }

  public void listDatabaseNames(Result<List<String>> result)
  {
    MongoIterable<String> iter = _client.listDatabaseNames();

    iter.into(new ArrayList<String>(), (list, t) -> {
      if (t != null) {
        result.fail(t);
      }
      else {
        result.complete(list);
      }
    });
  }

  protected void remove(String name)
  {
    _databaseMap.remove(name);
  }

  @OnDestroy
  public void onDestroy()
  {
    close(Result.ignore());
  }

  public void close(Result<Void> result)
  {
    _client.close();

    result.complete(null);
  }
}
