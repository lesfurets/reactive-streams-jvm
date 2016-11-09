package org.reactivestreams.example.flow;

import org.testng.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Test
public class PeriodicPublisherTest {

  private ExecutorService e;

  private PeriodicPublisher<Long> periodic;

  @BeforeClass
  void setUp(){
    e = Executors.newFixedThreadPool(4);
    AtomicLong i = new AtomicLong(0);
    periodic = new PeriodicPublisher<>(e, 20, i::incrementAndGet, 1, TimeUnit.SECONDS);
  }

  @AfterClass
  void after() {
    if (e != null) e.shutdown();
  }

  @Test
  public void testFlow() throws Exception {

    periodic.subscribe(new PrinterSubscriber(Long.MAX_VALUE));

    System.out.println("Subscribers :" + periodic.getNumberOfSubscribers());

    periodic.subscribe(new PrinterSubscriber(5L));
    System.out.println("Subscribers :" + periodic.getNumberOfSubscribers());

    Thread.sleep(10000);
  }

  @Test(alwaysRun = true)
  public void testProcessor() throws Exception {

    TransformProcessor<Long, String> transformer = new TransformProcessor<>(e, 20, (s) -> Thread.currentThread().getName() + ": " + s);
    periodic.subscribe(transformer);
    transformer.subscribe(new Flow.Subscriber<>() {
      @Override
      public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(String s) {
        System.out.println(s);
      }

      @Override
      public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("Complete");
      }
    });

    Thread.sleep(10000);

  }
}