package io.baratine.mongodb.client;

import java.util.List;

public interface BargoCursorServiceSync extends BargoCursorService
{
  void close(long id);

  int getBatchSize(long id);

  boolean isClosed(long id);

  List<Object> next(long id);

  void setBatchSize(long id, int batchSize);
}
