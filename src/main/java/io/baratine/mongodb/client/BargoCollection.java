package io.baratine.mongodb.client;

import java.util.List;

import org.bson.Document;

import io.baratine.core.Result;

public interface BargoCollection<T extends Document>
{
  void find(Result<BargoFindIterable<T>> result);

  void insertOne(T doc, Result<Void> result);

  void insertMany(List<? extends T> documents, Result<Void> result);
}
