package io.baratine.mongodb.client;

import java.util.List;

import org.bson.Document;

import io.baratine.core.Result;

public interface BargoCollectionSync<T extends Document> extends BargoCollection<T>
{
  BargoFindIterableSync<T> findSync();

  void insertOne(T doc);

  void insertMany(List<? extends T> documents);
}
