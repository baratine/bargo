package io.baratine.mongodb.client;

import java.util.List;

import com.mongodb.async.client.MongoClientSettings;

public interface BargoClientSync extends BargoClient
{
  String getUri();

  BargoDatabaseSync getDatabaseSync(String name);

  MongoClientSettings getSettings();

  List<String> listDatabaseNames();

  void close();
}
