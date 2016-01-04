package io.baratine.mongodb.client;

import java.util.List;

import io.baratine.core.Result;

public interface BargoCursorService
{
  void close(long id, Result<Void> result);

  void getBatchSize(long id, Result<Integer> result);

  void isClosed(long id, Result<Boolean> result);

  void next(long id, Result<List<Object>> result);

  void setBatchSize(long id, int batchSize, Result<Void> result);
}
