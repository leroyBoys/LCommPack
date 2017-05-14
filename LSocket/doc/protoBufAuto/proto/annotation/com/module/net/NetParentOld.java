package com.module.net;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


/** 
 * 
 * auto class not modify 
 * 
 */
public class NetParentOld {

public static class NetCommond {
	@Protobuf(fieldType = FieldType.STRING, order =  1, required = false)
	private String sn;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int cmd;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private int status;
	@Protobuf(fieldType = FieldType.INT32, order =  4, required = false)
	private int time;
	@Protobuf(fieldType = FieldType.BYTES, order =  5, required = false)
	private byte[] obj;
	@Protobuf(fieldType = FieldType.INT32, order =  6, required = false)
	private int uid;
	@Protobuf(fieldType = FieldType.INT32, order =  7, required = false)
	private int seq;

	public void setsn(String sn){
		this.sn=sn;
	}
	public String getsn(){
		return sn;
	}
	public void setcmd(int cmd){
		this.cmd=cmd;
	}
	public int getcmd(){
		return cmd;
	}
	public void setstatus(int status){
		this.status=status;
	}
	public int getstatus(){
		return status;
	}
	public void settime(int time){
		this.time=time;
	}
	public int gettime(){
		return time;
	}
	public void setobj(byte[] obj){
		this.obj=obj;
	}
	public byte[] getobj(){
		return obj;
	}
	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
	public void setseq(int seq){
		this.seq=seq;
	}
	public int getseq(){
		return seq;
	}
}
}
