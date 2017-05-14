package com.module.net;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.util.*;


/** 
 * 
 * auto class not modify 
 * 
 */
public class NetParent {

public static class NetCommond {
	@Protobuf(fieldType = FieldType.STRING, order =  1, required = true)
	private String sn;
	@Protobuf(fieldType = FieldType.BYTES, order =  2, required = true)
	private byte[] obj;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = true)
	private int seq;

	public void setsn(String sn){
		this.sn=sn;
	}
	public String getsn(){
		return sn;
	}
	public void setobj(byte[] obj){
		this.obj=obj;
	}
	public byte[] getobj(){
		return obj;
	}
	public void setseq(int seq){
		this.seq=seq;
	}
	public int getseq(){
		return seq;
	}
}
public static class NetError {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = true)
	private int errorCode;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private List<Integer> paramters;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private int seq;

	public void seterrorCode(int errorCode){
		this.errorCode=errorCode;
	}
	public int geterrorCode(){
		return errorCode;
	}
	public void setparamters(List<Integer> paramters){
		this.paramters=paramters;
	}
	public List<Integer> getparamters(){
		return paramters;
	}
	public void setseq(int seq){
		this.seq=seq;
	}
	public int getseq(){
		return seq;
	}
}
public static class NetServerCommond {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = true)
	private int uid;
	@Protobuf(fieldType = FieldType.BYTES, order =  2, required = true)
	private byte[] obj;
	@Protobuf(fieldType = FieldType.STRING, order =  3, required = false)
	private String sn;

	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
	public void setobj(byte[] obj){
		this.obj=obj;
	}
	public byte[] getobj(){
		return obj;
	}
	public void setsn(String sn){
		this.sn=sn;
	}
	public String getsn(){
		return sn;
	}
}
}
