package io.baratine.mongodb;

import io.baratine.core.ServiceInitializer;
import io.baratine.core.ServiceManager;

/**
 * Binding of the mongodb service.
 */
public class ServiceInitMongo implements ServiceInitializer
{
  @Override
  public void init(ServiceManager manager)
  {
    manager.newService()
           .service(new BargoManager())
           .address("mongodb:")
           .build();
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[]";
  }
}

