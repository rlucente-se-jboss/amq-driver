/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

import com.redhat.demo.generator.OrderGenerator;

/**
 * A simple driver to put orders on a JMS queue or publish to JMS topics
 */
public class AmqDriver {

	public static void main(final String[] args) throws Exception {
		Connection connection = null;
		InitialContext initialContext = null;
		try {
			CmdLineHandler handler = new CmdLineHandler();
			handler.parse(args);

			initialContext = new InitialContext();
			Queue queue = (Queue) initialContext.lookup("queue/orderQueue");
			Topic topic = (Topic) initialContext.lookup("topic/orderTopic");
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer queueProducer = session.createProducer(queue);
			MessageProducer topicProducer = session.createProducer(topic);

			MessageConsumer topicConsumer = session.createConsumer(topic);

			OrderGenerator generator = new OrderGenerator();

			TextMessage msg;
			int sentCount = 0;
			int recvCount = 0;

			for (int i = 0; i < handler.getNumMsgs(); i++) {
				if (handler.isQueue()) {
					msg = session.createTextMessage(generator.toXml());
					queueProducer.send(msg);
				}

				if (handler.isPub()) {
					msg = session.createTextMessage(generator.toXml());
					topicProducer.send(msg);
					++sentCount;
				}
			}

			connection.start();

			for (int i = 0; i < handler.getNumMsgs(); i++) {
				Message received = topicConsumer.receive();
				if (received != null) {
					++recvCount;
				}
			}

			connection.stop();
			System.out.println("topic msgs sent: " + sentCount + ", received: " + recvCount);

			System.exit(0); // to prevent hang on exec:java
		} catch (Exception e) {
			e.printStackTrace(System.err);
			;
		} finally {
			if (initialContext != null) {
				initialContext.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
