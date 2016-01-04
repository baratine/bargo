package io.baratine.mongodb.client;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.CursorType;

public interface BargoIterableServiceSync extends BargoIterableService
{
  //
  // MongoIterable
  //

  <T> BargoCursorSync<T> batchCursor(long id);

  void batchSize(long id, int batchSize);

  <T> T first(long id);

  <T> void forEach(long id, Block<? super T> block);

  <T,A extends Collection<? super T>> A into(long id, A target);

  //
  // FindIterable
  //

  void cursorType(long id, CursorType cursorType);

  void filter(long id, Bson filter);

  void limit(long id, int limit);

  void maxTime(long id, long maxTime, TimeUnit timeUnit);

  void modifiers(long id, Bson modifiers);

  void noCursorTimeout(long id, boolean noCursorTimeout);

  //void oplogReplay(boolean opLogReplay);

  void partial(long id, boolean partial);

  void projection(long id, Bson projection);

  void skip(long id, int skip);

  void sort(long id, Bson sort);
}
