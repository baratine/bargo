package io.baratine.mongodb;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.baratine.core.Lookup;
import io.baratine.core.ServiceRef;
import io.baratine.mongodb.client.BargoClientSync;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.caucho.junit.ConfigurationBaratine;
import com.caucho.junit.RunnerBaratine;

@RunWith(RunnerBaratine.class)
@ConfigurationBaratine(services = {BargoManager.class}, pod = "pod",
  logs = {@ConfigurationBaratine.Log(name = "com.caucho", level = "WARNING"),
          @ConfigurationBaratine.Log(name = "examples.cache.tree", level = "FINER")})
public class BargoManagerTest
{
  @Inject @Lookup("public:///mongodb")
  private ServiceRef _ref;

  @Inject
  private RunnerBaratine _testContext;

  private static AtomicInteger _count = new AtomicInteger();

  @Test
  public void testLookup()
  {
    ServiceRef ref = _ref.lookup("/");
    BargoClientSync client = ref.as(BargoClientSync.class);
    Assert.assertEquals("mongodb:///", client.getUri());

    ServiceRef ref2 = _ref.lookup("/foo");
    BargoClientSync client2 = ref2.as(BargoClientSync.class);
    Assert.assertEquals("mongodb:///foo", client2.getUri());
  }

  private void restartBaratine()
  {
    //_testContext.closeImmediate();
    //_testContext.start();
  }
}
