package com.lxtest.springbootdemo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class Sender {
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws java.io.IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("liuxin");
    factory.setUsername("liuxin");
    factory.setPassword("liuxin");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.confirmSelect();
    SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
    System.out.println("mainThread :"+ Thread.currentThread().getName());



    channel.addConfirmListener(new ConfirmListener() {
      @Override
      public void handleAck(long deliveryTag, boolean multiple) throws IOException {
        System.out.println("Ack :"+ Thread.currentThread().getName()+ "---"+deliveryTag + "multiple:" +multiple);
      }

      @Override
      public void handleNack(long deliveryTag, boolean multiple) throws IOException {
        System.out.println("Nack :"+ Thread.currentThread().getName()+ deliveryTag + "multiple:" +multiple);
      }
    });


    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello World!";
    for(int i= 1;i<3;i++){
      message = message+i;
      channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, message.getBytes());
    }
    System.out.println(" [x] Sent '" + message + "'");
  }
}
