import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

public class ActiveMQSend
{

   BufferedReader br = null;
   String sLine = null;
   String tokenizedEnvString[] = null;
   String readLine[] = null;
   Date currentTime = null;
   String dTime = null;
   String fileName = null;
 
   StringBuffer buff = new StringBuffer();
   static String receiveQueueName = null;
   static String sendQueueName = null;
   static File envFile = null;
   static int sleepTime = 0;
   static int iterationCount = 0;
    
   public void init()
   {
	try { 
		br = new BufferedReader(new FileReader(envFile));
		while ((sLine = br.readLine()) != null) {
                    buff.append(sLine+"\r\n");
		}
        } catch (Exception e) {
            e.printStackTrace();
        }
 			
  
       System.out.println(buff.toString());
    }

  
  public void send() 
  //public void send() throws Exception
  {
    try{
    String url = "failover:(ssl://b-a827c188-6f38-4f76-9569-03e3046023fd-1.mq.ap-northeast-2.amazonaws.com:61617,ssl://b-a827c188-6f38-4f76-9569-03e3046023fd-2.mq.ap-northeast-2.amazonaws.com:61617)";

    //ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
    factory.setUserName("kilhan");
    factory.setPassword("Kimkilhan0304!");




    Connection connection = factory.createConnection();
    connection.start();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
    
    Queue queue = new ActiveMQQueue(sendQueueName);
    MessageProducer producer = session.createProducer(queue);
    Destination destination = session.createQueue(receiveQueueName);
    

    for(int i=0; i<iterationCount; i++)
    {
        //Message message = session.createTextMessage("MESSAGE jjouhiu");
        //
        //


        System.out.println("buff.toString() : " + buff.toString());
        TextMessage message = session.createTextMessage(buff.toString());
        message.setJMSReplyTo(destination);
        producer.send(queue, message);
        System.out.println("[SEND] " + message.toString());

        Thread.sleep(sleepTime);
    }

  /* 
    message = session.createTextMessage("MESSAGE 2");
    message.setJMSReplyTo(destination);
    producer.send(queue, message);
    System.out.println("[SEND] " + message.toString());
 */	
    
    session.close();
    connection.close();
   }catch(Exception e)
  {
      System.out.println(e.toString());

  }
  }
  
  public static void main(String args[]) throws Exception
  {
    if(args.length !=4)
    {
      System.out.println("Usage :  java -classpath lib/activemq-all-5.15.11.jar:. ActiveMQSend <queue name> <file path & name> <think time> <iteration time>" ); 
      return;
    }
    ActiveMQSend qsr = new ActiveMQSend();
    receiveQueueName = args[0].toString();
    sendQueueName = args[0].toString();
    envFile = new File(args[1].toString());
    sleepTime = Integer.parseInt(args[2].toString());
    iterationCount = Integer.parseInt(args[3].toString());
    qsr.init();
    qsr.send();
  }
} 
