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

public class consumer_durability_efs
{
  
  static String receiveQueueName = null;
  static int sleepTime = 0;
  static int iterationCount = 0;


  public void receive() 
  //public void send() throws Exception
  {
    try{
    String url = "ssl://b-726b27b9-00bd-45bb-a32d-3afbe97add9c-1.mq.ap-northeast-2.amazonaws.com:61617";


    //ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
    factory.setUserName("kilhan");
    factory.setPassword("Kimkilhan0304!");


    Connection connection = null;
    Session session = null;
    Queue queue = null;
    Destination destination = null;
    MessageConsumer consumer = null;
    Message message = null;

    connection = factory.createConnection();
    connection.start();
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    queue  = new ActiveMQQueue(receiveQueueName);
    destination = session.createQueue(receiveQueueName);
    consumer = session.createConsumer(destination);

    for(int i=0; i<iterationCount; i++){

    // Wait for a message
    message = consumer.receive(1000);

        if (message instanceof TextMessage) {
           TextMessage textMessage = (TextMessage) message;
           String text = textMessage.getText();
      //     System.out.println("Received text : " + text);
        }else {
       //    System.out.println("Received message : " + message);
        }

      Thread.sleep(sleepTime);

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

    if(args.length !=3)
    {
      System.out.println("Usage :  java -classpath lib/activemq-all-5.15.11.jar:. MQReceive <queue name> <think time> <iteration time>" );
      return;
    }
    receiveQueueName = args[0].toString();
    sleepTime = Integer.parseInt(args[1].toString());
    iterationCount = Integer.parseInt(args[2].toString());


    consumer_durability_efs qsr = new consumer_durability_efs();
    qsr.receive();
  }
} 
