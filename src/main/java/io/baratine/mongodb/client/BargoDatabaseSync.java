package io.baratine.mongodb.client;

import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

public interface BargoDatabaseSync extends BargoDatabase
{
  void createCollection(String name);

  void drop();

  CodecRegistry getCodecRegistry();

  BargoCollection<Document> getCollection(String name);

  String getName();

  ReadPreference getReadPreference();

  WriteConcern getWriteConcern();

  List<String> listCollectionNames();

  void withCodecRegistry(CodecRegistry codecRegistry);

  void withReadPreference(ReadPreference readPreference);

  void withWriteConcern(WriteConcern writeConcern);
}
