package com.redhat.demo.generator;

import javax.xml.bind.annotation.XmlElement;

public class OrderLine {
	private Article article;
	private int quantity;

	public Article getArticle() {
		return article;
	}

	@XmlElement
	public void setArticle(Article article) {
		this.article = article;
	}

	public int getQuantity() {
		return quantity;
	}

	@XmlElement
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
