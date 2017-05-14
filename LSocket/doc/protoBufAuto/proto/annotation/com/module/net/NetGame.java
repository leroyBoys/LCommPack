package com.module.net;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import java.util.*;


/** 
 * 
 * auto class not modify 
 * 
 */
public class NetGame {

public static class NetUserData {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int uid;
	@Protobuf(fieldType = FieldType.STRING, order =  2, required = false)
	private String image;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private int idex;
	@Protobuf(fieldType = FieldType.INT32, order =  4, required = false)
	private int status;
	@Protobuf(fieldType = FieldType.OBJECT, order =  5, required = false)
	private NetExtraData extra;

	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
	public void setimage(String image){
		this.image=image;
	}
	public String getimage(){
		return image;
	}
	public void setidex(int idex){
		this.idex=idex;
	}
	public int getidex(){
		return idex;
	}
	public void setstatus(int status){
		this.status=status;
	}
	public int getstatus(){
		return status;
	}
	public void setextra(NetExtraData extra){
		this.extra=extra;
	}
	public NetExtraData getextra(){
		return extra;
	}
}
public static class NetExtraData {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private List<Integer> list;
	@Protobuf(fieldType = FieldType.OBJECT, order =  2, required = false)
	private List<NetKvData> kvDatas;
	@Protobuf(fieldType = FieldType.OBJECT, order =  3, required = false)
	private List<NetOprateData> operates;

	public void setlist(List<Integer> list){
		this.list=list;
	}
	public List<Integer> getlist(){
		return list;
	}
	public void setkvDatas(List<NetKvData> kvDatas){
		this.kvDatas=kvDatas;
	}
	public List<NetKvData> getkvDatas(){
		return kvDatas;
	}
	public void setoperates(List<NetOprateData> operates){
		this.operates=operates;
	}
	public List<NetOprateData> getoperates(){
		return operates;
	}
}
public static class NetOprateData {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int otype;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int uid;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private int dval;
	@Protobuf(fieldType = FieldType.INT32, order =  4, required = false)
	private int flag;
	@Protobuf(fieldType = FieldType.INT32, order =  5, required = false)
	private List<Integer> dlist;
	@Protobuf(fieldType = FieldType.OBJECT, order =  6, required = false)
	private List<NetKvData> kvDatas;

	public void setotype(int otype){
		this.otype=otype;
	}
	public int getotype(){
		return otype;
	}
	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
	public void setdval(int dval){
		this.dval=dval;
	}
	public int getdval(){
		return dval;
	}
	public void setflag(int flag){
		this.flag=flag;
	}
	public int getflag(){
		return flag;
	}
	public void setdlist(List<Integer> dlist){
		this.dlist=dlist;
	}
	public List<Integer> getdlist(){
		return dlist;
	}
	public void setkvDatas(List<NetKvData> kvDatas){
		this.kvDatas=kvDatas;
	}
	public List<NetKvData> getkvDatas(){
		return kvDatas;
	}
}
public static class NetKvData {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int k;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int v;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private List<Integer> dlist;

	public void setk(int k){
		this.k=k;
	}
	public int getk(){
		return k;
	}
	public void setv(int v){
		this.v=v;
	}
	public int getv(){
		return v;
	}
	public void setdlist(List<Integer> dlist){
		this.dlist=dlist;
	}
	public List<Integer> getdlist(){
		return dlist;
	}
}
public static class RPCreateRoom {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int roomId;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int gameId;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private List<Integer> type;

	public void setroomId(int roomId){
		this.roomId=roomId;
	}
	public int getroomId(){
		return roomId;
	}
	public void setgameId(int gameId){
		this.gameId=gameId;
	}
	public int getgameId(){
		return gameId;
	}
	public void settype(List<Integer> type){
		this.type=type;
	}
	public List<Integer> gettype(){
		return type;
	}
}
public static class RQCreateRoom {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int roomId;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int gameId;
	@Protobuf(fieldType = FieldType.OBJECT, order =  3, required = false)
	private List<NetUserData> users;
	@Protobuf(fieldType = FieldType.INT32, order =  4, required = false)
	private List<Integer> type;
	@Protobuf(fieldType = FieldType.INT32, order =  5, required = false)
	private int curRount;
	@Protobuf(fieldType = FieldType.INT32, order =  6, required = false)
	private int gameStatus;
	@Protobuf(fieldType = FieldType.INT32, order =  7, required = false)
	private int ownerId;
	@Protobuf(fieldType = FieldType.OBJECT, order =  8, required = false)
	private NetExtraData extra;

	public void setroomId(int roomId){
		this.roomId=roomId;
	}
	public int getroomId(){
		return roomId;
	}
	public void setgameId(int gameId){
		this.gameId=gameId;
	}
	public int getgameId(){
		return gameId;
	}
	public void setusers(List<NetUserData> users){
		this.users=users;
	}
	public List<NetUserData> getusers(){
		return users;
	}
	public void settype(List<Integer> type){
		this.type=type;
	}
	public List<Integer> gettype(){
		return type;
	}
	public void setcurRount(int curRount){
		this.curRount=curRount;
	}
	public int getcurRount(){
		return curRount;
	}
	public void setgameStatus(int gameStatus){
		this.gameStatus=gameStatus;
	}
	public int getgameStatus(){
		return gameStatus;
	}
	public void setownerId(int ownerId){
		this.ownerId=ownerId;
	}
	public int getownerId(){
		return ownerId;
	}
	public void setextra(NetExtraData extra){
		this.extra=extra;
	}
	public NetExtraData getextra(){
		return extra;
	}
}
public static class RPEnterRoom {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int roomId;
	@Protobuf(fieldType = FieldType.STRING, order =  2, required = false)
	private String key;

	public void setroomId(int roomId){
		this.roomId=roomId;
	}
	public int getroomId(){
		return roomId;
	}
	public void setkey(String key){
		this.key=key;
	}
	public String getkey(){
		return key;
	}
}
public static class RQUserStatus {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int status;

	public void setstatus(int status){
		this.status=status;
	}
	public int getstatus(){
		return status;
	}
}
public static class RQREsult {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int flag;
	@Protobuf(fieldType = FieldType.OBJECT, order =  2, required = false)
	private List<NetMjUserResult> users;

	public void setflag(int flag){
		this.flag=flag;
	}
	public int getflag(){
		return flag;
	}
	public void setusers(List<NetMjUserResult> users){
		this.users=users;
	}
	public List<NetMjUserResult> getusers(){
		return users;
	}
}
public static class NetMjUserResult {
	@Protobuf(fieldType = FieldType.OBJECT, order =  1, required = false)
	private List<NetKvData> scores;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int showType;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = false)
	private int winType;
	@Protobuf(fieldType = FieldType.INT32, order =  4, required = false)
	private int score;

	public void setscores(List<NetKvData> scores){
		this.scores=scores;
	}
	public List<NetKvData> getscores(){
		return scores;
	}
	public void setshowType(int showType){
		this.showType=showType;
	}
	public int getshowType(){
		return showType;
	}
	public void setwinType(int winType){
		this.winType=winType;
	}
	public int getwinType(){
		return winType;
	}
	public void setscore(int score){
		this.score=score;
	}
	public int getscore(){
		return score;
	}
}
public static class RQEnterRoom {
	@Protobuf(fieldType = FieldType.OBJECT, order =  1, required = false)
	private NetUserData user;

	public void setuser(NetUserData user){
		this.user=user;
	}
	public NetUserData getuser(){
		return user;
	}
}
public static class RQExit {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int uid;

	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
}
public static class RPVote {
	@Protobuf(fieldType = FieldType.BOOL, order =  1, required = false)
	private boolean isagree;

	public void setisagree(boolean isagree){
		this.isagree=isagree;
	}
	public boolean getisagree(){
		return isagree;
	}
}
public static class RQVote {
	@Protobuf(fieldType = FieldType.BOOL, order =  1, required = false)
	private boolean isagree;
	@Protobuf(fieldType = FieldType.INT32, order =  2, required = false)
	private int uid;

	public void setisagree(boolean isagree){
		this.isagree=isagree;
	}
	public boolean getisagree(){
		return isagree;
	}
	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
}
public static class RQConnect {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int roomId;

	public void setroomId(int roomId){
		this.roomId=roomId;
	}
	public int getroomId(){
		return roomId;
	}
}
public static class NetResponse {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = false)
	private int status;
	@Protobuf(fieldType = FieldType.OBJECT, order =  2, required = false)
	private List<NetOprateData> operateDatas;
	@Protobuf(fieldType = FieldType.INT32, order =  3, required = true)
	private int retStatus;
	@Protobuf(fieldType = FieldType.INT32, order =  4, required = false)
	private int step;

	public void setstatus(int status){
		this.status=status;
	}
	public int getstatus(){
		return status;
	}
	public void setoperateDatas(List<NetOprateData> operateDatas){
		this.operateDatas=operateDatas;
	}
	public List<NetOprateData> getoperateDatas(){
		return operateDatas;
	}
	public void setretStatus(int retStatus){
		this.retStatus=retStatus;
	}
	public int getretStatus(){
		return retStatus;
	}
	public void setstep(int step){
		this.step=step;
	}
	public int getstep(){
		return step;
	}
}
public static class NetLoginConfirm {
	@Protobuf(fieldType = FieldType.INT32, order =  1, required = true)
	private int uid;

	public void setuid(int uid){
		this.uid=uid;
	}
	public int getuid(){
		return uid;
	}
}
}
