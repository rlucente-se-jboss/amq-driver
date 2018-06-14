package com.redhat.demo.generator;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class OrderLines {
	private List<OrderLine> orderlines = null;

	public List<OrderLine> getOrderLines() {
		return orderlines;
	}

	@XmlElement(name = "orderline")
	public void setOrderLines(List<OrderLine> orderlines) {
		this.orderlines = orderlines;
	}
}
