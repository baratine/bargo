package io.baratine.mongodb;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.caucho.junit.ConfigurationBaratine;
import com.caucho.junit.RunnerBaratine;
import com.mongodb.async.client.MongoClientSettings;

import io.baratine.core.Lookup;
import io.baratine.core.ResultFuture;
import io.baratine.core.ServiceRef;
import io.baratine.mongodb.client.BargoClientSync;
import io.baratine.mongodb.client.BargoDatabaseSync;

@RunWith(RunnerBaratine.class)
@ConfigurationBaratine(services = {BargoManager.class}, pod = "pod",
  logs = {@ConfigurationBaratine.Log(name = "com.caucho", level = "WARNING"),
          @ConfigurationBaratine.Log(name = "io.baratine.mongodb", level = "FINER")})
public class BargoClientTest
{
  @Inject @Lookup("public:///mongodb/")
  private ServiceRef _ref;

  @Inject @Lookup("public:///mongodb/")
  private BargoClientSync _client;

  @Inject
  private RunnerBaratine _testContext;

  @Test
  public void testGetUri()
  {
    Assert.assertEquals("mongodb:///", _client.getUri());
  }

  @Test
  public void testGetDatabase()
  {
    BargoDatabaseSync db = _client.getDatabaseSync("testDb");

    Assert.assertEquals("testDb", db.getName());
  }

  @Test
  public void testGetSettings()
  {
    MongoClientSettings settings = _client.getSettings();
    Assert.assertEquals(true, settings != null);
  }

  @Test
  public void testListDatabaseNames()
  {
    List<String> list = _client.listDatabaseNames();

    Assert.assertEquals(1, list.size());
  }

  @Test
  public void testClose()
  {
    _client.close();
  }
}
