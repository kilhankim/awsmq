import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class MQReceive 
{
  String receiveQueueName = "ActiveMQ";
  
  String sendQueueName = "ActiveMQ";
  
  public void receive() 
  //public void send() throws Exception
  {
    try{
    System.out.println("check c");
    String url = "failover:(ssl://b-a827c188-6f38-4f76-9569-03e3046023fd-1.mq.ap-northeast-2.amazonaws.com:61617,ssl://b-a827c188-6f38-4f76-9569-03e3046023fd-2.mq.ap-northeast-2.amazonaws.com:61617)";
    System.out.println("check d");

    //ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
    factory.setUserName("kilhan");
    factory.setPassword("Kimkilhan0304!");




    System.out.println("check e");
    Connection connection = factory.createConnection();
    connection.start();
    System.out.println("check f");
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
    System.out.println("check 1");
    
    Queue queue = new ActiveMQQueue(receiveQueueName);
    System.out.println("check 1-2");



    Destination destination = session.createQueue(receiveQueueName);

    MessageConsumer consumer = session.createConsumer(destination);

    // Wait for a message
    Message message = consumer.receive(1000);
    if (message instanceof TextMessage) {
       TextMessage textMessage = (TextMessage) message;
       String text = textMessage.getText();
       System.out.println("Received text : " + text);
    }else {
       System.out.println("Received message : " + message);
    }
  
    consumer.close();
    session.close();
    connection.close();
   }catch(Exception e)
  {
      System.out.println(e.toString());

  }
  }
  
  public static void main(String args[]) throws Exception
  {
    System.out.println("check a");
    MQReceive qsr = new MQReceive();
    System.out.println("check b");
    qsr.receive();
    System.out.println("check c");
  }
} 
