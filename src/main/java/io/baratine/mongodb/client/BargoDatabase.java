package io.baratine.mongodb.client;

import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

import io.baratine.core.Result;

public interface BargoDatabase
{
  void createCollection(String name, Result<Void> result);

  void drop(Result<Void> result);

  void getCodecRegistry(Result<CodecRegistry> result);

  void getCollection(String name, Result<BargoCollection<Document>> result);

  BargoCollectionSync<Document> getCollectionSync(String name);

  void getName(Result<String> result);

  void getReadPreference(Result<ReadPreference> result);

  void getWriteConcern(Result<WriteConcern> result);

  void listCollectionNames(Result<List<String>> result);

  void withCodecRegistry(CodecRegistry codecRegistry, Result<Void> result);

  void withReadPreference(ReadPreference readPreference, Result<Void> result);

  void withWriteConcern(WriteConcern writeConcern, Result<Void> result);
}
