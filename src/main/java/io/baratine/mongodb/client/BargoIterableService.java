package io.baratine.mongodb.client;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.CursorType;

import io.baratine.core.Result;

public interface BargoIterableService
{
  //
  // MongoIterable
  //

  <T> void batchCursor(long id, Result<BargoCursor<T>> result);

  void batchSize(long id, int batchSize, Result<Void> result);

  <T> void first(long id, Result<T> result);

  <T> void forEach(long id, Block<? super T> block, Result<Void> result);

  <T,A extends Collection<? super T>> void into(long id, A target, Result<A> result);

  //
  // FindIterable
  //

  void cursorType(long id, CursorType cursorType, Result<Void> result);

  void filter(long id, Bson filter, Result<Void> result);

  void limit(long id, int limit, Result<Void> result);

  void maxTime(long id, long maxTime, TimeUnit timeUnit, Result<Void> result);

  void modifiers(long id, Bson modifiers, Result<Void> result);

  void noCursorTimeout(long id, boolean noCursorTimeout, Result<Void> result);

  //void oplogReplay(boolean opLogReplay, Result<Void> result);

  void partial(long id, boolean partial, Result<Void> result);

  void projection(long id, Bson projection, Result<Void> result);

  void skip(long id, int skip, Result<Void> result);

  void sort(long id, Bson sort, Result<Void> result);
}
