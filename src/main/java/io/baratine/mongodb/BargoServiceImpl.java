package io.baratine.mongodb;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;

import io.baratine.core.OnInit;
import io.baratine.core.Result;

public class BargoServiceImpl implements BargoService
{
  private String _uri;

  private MongoClient _client;

  public BargoServiceImpl(String uri)
  {
    _uri = uri;
  }

  @OnInit
  public void onInit(Result<Void> result)
  {
    _client = MongoClients.create(_uri);

    result.complete(null);
  }
}
