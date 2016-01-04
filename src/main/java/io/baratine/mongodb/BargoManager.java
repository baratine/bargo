package io.baratine.mongodb;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;

import io.baratine.core.OnLookup;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;

@Service("public:///mongodb")
public class BargoManager
{
  private static final Logger _logger
    = Logger.getLogger(BargoManager.class.getName());

  public BargoManager()
  {
    if (_logger.isLoggable(Level.FINER)) {
      _logger.info(getClass().getSimpleName() + " instantiated");
    }
  }

  @OnLookup
  public ServiceRef onLookup(String url)
  {
    if (_logger.isLoggable(Level.FINER)) {
      _logger.info("onLookup: " + url);
    }

    BargoClientService serviceClient = new BargoClientService("mongodb://" + url);

    ServiceManager manager = ServiceManager.current();

    ServiceRef ref = manager.newService().service(serviceClient).build();

    return ref;
  }
}
