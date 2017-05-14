package com.module.net;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


/** 
 * 
 * auto class not modify 
 * 
 */
public class DB {

public static class UK {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = true)
	private int uid;
	@Protobuf(fieldType = FieldType.STRING, order =  2, required = false)
	private String ip_port;
	@Protobuf(fieldType = FieldType.STRING, order =  3, required = true)
	private String key;

	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
	public void setip_port(String ip_port){
		this.ip_port=ip_port;
	}
	public String getip_port(){
		return ip_port;
	}
	public void setkey(String key){
		this.key=key;
	}
	public String getkey(){
		return key;
	}
}
}
