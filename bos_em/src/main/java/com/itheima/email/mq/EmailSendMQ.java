package com.itheima.email.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import com.itheima.utils.SendJMail;

@Service("emailSendMQ")
public class EmailSendMQ implements MessageListener {

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;

		try {

			String url = mapMessage.getString("url");
			System.out.println("URL"+url);

			String context = mapMessage.getString("context");
			System.out.println("context"+context);

			SendJMail.sendMail(url,context);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 

}
