package io.baratine.mongodb;

import java.util.List;

import javax.inject.Inject;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.caucho.junit.ConfigurationBaratine;
import com.caucho.junit.RunnerBaratine;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

import io.baratine.core.Lookup;
import io.baratine.mongodb.client.BargoClientSync;
import io.baratine.mongodb.client.BargoCollectionSync;
import io.baratine.mongodb.client.BargoDatabaseSync;

@RunWith(RunnerBaratine.class)
@ConfigurationBaratine(services = {BargoManager.class}, pod = "pod",
  logs = {@ConfigurationBaratine.Log(name = "com.caucho", level = "WARNING"),
          @ConfigurationBaratine.Log(name = "io.baratine.mongodb", level = "FINER")})
public class BargoDatabaseTest
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
  public void testCreateCollection()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    try {
      List<String> list = db.listCollectionNames();

      Assert.assertEquals(0, list.size());

      db.createCollection("testCollection");

      list = db.listCollectionNames();
      Assert.assertEquals(1, list.size());
      Assert.assertEquals("testCollection", list.get(0));
    }
    finally {
      db.drop();
    }
  }

  @Test
  public void testDrop()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    db.createCollection("testCollection");

    List<String> list = db.listCollectionNames();
    Assert.assertEquals(1, list.size());

    db.drop();

    list = db.listCollectionNames();
    Assert.assertEquals(0, list.size());
  }

  @Test
  public void testGetCodecRegistry()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    Assert.assertEquals(true, db.getCodecRegistry() != null);
  }

  @Test
  public void testGetCollection()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    BargoCollectionSync<Document> col = db.getCollectionSync("testCollection");

    Assert.assertEquals(true, col != null);
  }

  @Test
  public void testGetName()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    Assert.assertEquals("testDb", db.getName());
  }

  @Test
  public void testGetReadPreference()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    Assert.assertEquals(true, db.getReadPreference() != null);
  }

  @Test
  public void testGetWriteConcern()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    Assert.assertEquals(true, db.getWriteConcern() != null);
  }

  @Test
  public void testListCollectionNames()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    List<String> list = db.listCollectionNames();

    Assert.assertEquals(0, list.size());
  }

  @Test
  public void testWithCodecRegistry()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    CodecRegistry codec = db.getCodecRegistry();

    MyCodecRegistry myCodec = new MyCodecRegistry();

    try {
      db.withCodecRegistry(myCodec);

      Assert.assertEquals(true, db.getCodecRegistry() instanceof MyCodecRegistry);
    }
    finally {
      db.withCodecRegistry(codec);
    }

    Assert.assertEquals(false, db.getCodecRegistry() instanceof MyCodecRegistry);
  }

  @Test
  public void testWithReadPreference()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    ReadPreference pref = db.getReadPreference();

    try {
      db.withReadPreference(ReadPreference.secondaryPreferred());
      Assert.assertEquals(ReadPreference.secondaryPreferred(), db.getReadPreference());

      db.withReadPreference(ReadPreference.primary());
      Assert.assertEquals(ReadPreference.primary(), db.getReadPreference());
    }
    finally {
      db.withReadPreference(pref);
    }
  }

  @Test
  public void testWithWriteConcern()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    WriteConcern concern = db.getWriteConcern();

    try {
      db.withWriteConcern(WriteConcern.REPLICA_ACKNOWLEDGED);
      Assert.assertEquals(WriteConcern.REPLICA_ACKNOWLEDGED, db.getWriteConcern());

      db.withWriteConcern(WriteConcern.JOURNALED);
      Assert.assertEquals(WriteConcern.JOURNALED, db.getWriteConcern());
    }
    finally {
      db.withWriteConcern(concern);
    }
  }

  static class MyCodecRegistry implements CodecRegistry {
    public <T> Codec<T> get(Class<T> cls)
    {
      return null;
    }
  }
}
