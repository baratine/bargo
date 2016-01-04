package io.baratine.mongodb.client;

import java.util.List;

public interface BargoCursorSync<T>
{
  void close();

  int getBatchSize();

  boolean isClosed();

  List<T> next();

  static class BargoCursorSyncImpl<T> implements BargoCursorSync<T> {
    private long _id;
    private BargoCursorServiceSync _service;

    private boolean _isClosed;

    public BargoCursorSyncImpl(long id, BargoCursorServiceSync service)
    {
      _id = id;
      _service = service;
    }

    public void close()
    {
      _service.close(_id);

      _isClosed = true;
    }

    public int getBatchSize()
    {
      return _service.getBatchSize(_id);
    }

    public boolean isClosed()
    {
      if (_isClosed) {
        return _isClosed;
      }

      return _service.isClosed(_id);
    }

    public List<T> next()
    {
      return (List) _service.next(_id);
    }
  }
}
