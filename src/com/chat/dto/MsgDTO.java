package com.chat.dto;

import java.io.Serializable;
import java.awt.Font;
import java.awt.Color;
@SuppressWarnings("serial")
public class MsgDTO implements Serializable{
	private String userId="***";
	private String toWhere="***";
	private String content="###";
	private Font font=new Font("ÐÂËÎÌå",Font.PLAIN,12);
	private Color color=new Color(0,0,0);
	public MsgDTO(){}
	public MsgDTO(String userId,String toWhere,String content,Font font,Color color){
		this.userId=userId;
		this.toWhere=toWhere;
		this.content=content;
		this.font=font;
		this.color=color;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public String getToWhere() {
		return toWhere;
	}
	public void setToWhere(String toWhere) {
		this.toWhere = toWhere;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
