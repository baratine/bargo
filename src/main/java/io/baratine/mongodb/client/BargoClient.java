package io.baratine.mongodb.client;

import java.util.List;

import com.mongodb.async.client.MongoClientSettings;

import io.baratine.core.Result;

public interface BargoClient
{
  void getUri(Result<String> result);

  BargoDatabaseSync getDatabaseSync(String name);

  void getDatabase(String name, Result<BargoDatabase> result);

  void getSettings(Result<MongoClientSettings> result);

  void listDatabaseNames(Result<List<String>> result);

  void close(Result<Void> result);
}
