/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.reactivestreams.example.flow;

import java.util.concurrent.Flow;

public class PrinterSubscriber implements Flow.Subscriber<Long> {

  Long request;

  public PrinterSubscriber(Long request) {
    this.request = request;
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    subscription.request(request);
  }

  @Override
  public void onNext(Long aLong) {
    System.out.println("Next : " + Thread.currentThread().getName() + " : " + aLong);
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {
    System.out.println("Complete");
  }
}
