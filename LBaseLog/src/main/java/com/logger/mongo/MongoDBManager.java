package com.logger.mongo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leroy
 */
public class MongoDBManager {

    private final static Object objLock = new Object();
    private DB db;
    private Mongo mongo;
    ///是否发送服务器内存信息间隔(小于 等于0为不发送（默认值）)
    private int sendMemoryMsgPerSeconds = 0;
    private static MongoDBManager mongoDBManager;

    public static MongoDBManager getInstance(String uri, String db) {
        if (mongoDBManager == null) {
            synchronized (objLock) {
                if (mongoDBManager == null) {
                    try {
                        mongoDBManager = new MongoDBManager();
                        mongoDBManager.mongo = new Mongo(uri);
                        mongoDBManager.db = mongoDBManager.mongo.getDB(db);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return mongoDBManager;
    }

    public static MongoDBManager getInstance(String uri, String db, MongoOptions poolOption) {
        if (mongoDBManager == null) {
            synchronized (objLock) {
                if (mongoDBManager == null) {
                    try {
                        mongoDBManager = new MongoDBManager();
                        mongoDBManager.mongo = new Mongo(uri, poolOption);
                        mongoDBManager.db = mongoDBManager.mongo.getDB(db);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MongoDBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
        return mongoDBManager;
    }

    private MongoDBManager()  {
        Runtime.getRuntime().addShutdownHook(
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mongo.close();
                    }
                }, "mongo shutdown"));
    }

    public DB getDB(String dbName)  {
        return mongo.getDB(dbName);
    }

    public DB getDB() {
        return db;
    }

    public void append(String collection, BasicDBObject... desc)  {
        getDB().getCollection(collection).insert(desc);
    }

    public void append(String collection, List<BasicDBObject> desc) {
        getDB().getCollection(collection).insert(desc.toArray(new BasicDBObject[]{}));
    }

    public int getSendMemoryMsgPerSeconds() {
        return sendMemoryMsgPerSeconds;
    }

    public void setSendMemoryMsgPerSeconds(int sendMemoryMsgPerSeconds) {
        this.sendMemoryMsgPerSeconds = sendMemoryMsgPerSeconds;
        if (this.sendMemoryMsgPerSeconds > 0) {
            JvmMonitor.getInstance(this, sendMemoryMsgPerSeconds, true);
        }
    }

}
