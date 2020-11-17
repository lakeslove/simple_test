package com.lxtest.springbootdemo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws java.io.IOException, TimeoutException {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("liuxin");
    factory.setUsername("liuxin");
    factory.setPassword("liuxin");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
        System.out.println("consumerTag:"+consumerTag);
        System.out.println("getDeliveryTag:"+envelope.getDeliveryTag());
        System.out.println("------------------");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        channel.basicNack(envelope.getDeliveryTag(), false, true);
      }
    };
    channel.basicConsume(QUEUE_NAME, false, consumer);
  }
}
