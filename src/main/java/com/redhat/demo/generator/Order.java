package com.redhat.demo.generator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://com.redhat.demo/order")
public class Order {
	private String id;
	private Customer customer;
	private String date;
	private OrderLines orderlines;

	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	@XmlElement
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDate() {
		return date;
	}

	@XmlElement
	public void setDate(String date) {
		this.date = date;
	}

	public OrderLines getOrderlines() {
		return orderlines;
	}

	@XmlElement
	public void setOrderlines(OrderLines orderlines) {
		this.orderlines = orderlines;
	}
}
