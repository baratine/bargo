package io.baratine.mongodb;

import io.baratine.core.Result;
import io.baratine.core.ServiceClient;

public class BargoClientMain
{
  public static void main(String[] args)
    throws Exception
  {
    ServiceClient baratineClient = ServiceClient.newClient("http://127.0.0.1:8085/s/pod").build();

    BargoClientService client = baratineClient.lookup("remote:///mongodb/").as(BargoClientService.class);

    client.listDatabaseNames(Result.from(list -> {
      System.out.println("result: " + list);
    }, t -> {
      t.printStackTrace();
    }));

    Thread.sleep(1000 * 5);
  }
}
