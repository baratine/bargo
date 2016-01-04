package io.baratine.mongodb;

import java.util.ArrayList;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.caucho.junit.ConfigurationBaratine;
import com.caucho.junit.RunnerBaratine;

import io.baratine.core.Lookup;
import io.baratine.mongodb.client.BargoClientSync;
import io.baratine.mongodb.client.BargoCollectionSync;
import io.baratine.mongodb.client.BargoDatabaseSync;
import io.baratine.mongodb.client.BargoFindIterableSync;

@RunWith(RunnerBaratine.class)
@ConfigurationBaratine(services = {BargoManager.class}, pod = "pod",
  logs = {@ConfigurationBaratine.Log(name = "com.caucho", level = "WARNING"),
          @ConfigurationBaratine.Log(name = "io.baratine.mongodb", level = "FINER")})

public class BargoCollectionTest
{
  @Inject @Lookup("public:///mongodb/")
  private BargoClientSync _client;

  @Before
  @After
  public void before()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    db.drop();
  }

  @Test
  public void testFind()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    BargoCollectionSync<Document> col = db.getCollectionSync("testCollection");
    BargoFindIterableSync<Document> iter = col.findSync();

    Document first = iter.first();
    Assert.assertEquals(true, first == null);
  }

  @Test
  public void testInsertOne()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    BargoCollectionSync<Document> col = db.getCollectionSync("testCollection");
    BargoFindIterableSync<Document> iter = col.findSync();

    Document first = iter.first();
    Assert.assertEquals(true, first == null);

    col.insertOne(new Document("name", "testDoc0"));

    first = iter.first();
    Assert.assertEquals(true, first != null);
    Assert.assertEquals("testDoc0", first.getString("name"));
  }

  @Test
  public void testInsertMany()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    BargoCollectionSync<Document> col = db.getCollectionSync("testCollection");
    BargoFindIterableSync<Document> iter = col.findSync();

    Document first = iter.first();
    Assert.assertEquals(true, first == null);

    ArrayList<Document> list = new ArrayList<>();
    list.add(new Document("name", "testDoc0"));
    list.add(new Document("name", "testDoc1"));
    list.add(new Document("name", "testDoc2"));

    col.insertMany(list);
    iter = col.findSync();

    ArrayList<Document> resultList = iter.into(new ArrayList<Document>());

    Assert.assertEquals(3, resultList.size());
    Assert.assertEquals("testDoc0", resultList.get(0).getString("name"));
    Assert.assertEquals("testDoc1", resultList.get(1).getString("name"));
    Assert.assertEquals("testDoc2", resultList.get(2).getString("name"));
  }
}
