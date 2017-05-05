package com.logger.mongo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mongodb.BasicDBObject;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author leroy
 */
public class JvmMonitor {

    private static JvmMonitor uniqueInstance = null;

//    private Logger logger = LoggerFactory.getLogger(JvmMonitor.class);
    private long lastProcessCpuTime = 0;
    private long lastUptime = 0;
    public static final int DEFAULT_REFRESH_SECONDS = 60;
    private MongoDBManager connectionSource;
    private boolean logToLocal = false;
    ScheduledExecutorService executorService = null;

    public synchronized static JvmMonitor getInstance(MongoDBManager connectionSource, int periodSeconds, boolean logToLocal) {
        if (uniqueInstance == null) {
            uniqueInstance = new JvmMonitor(connectionSource, periodSeconds, logToLocal);
        }
        return uniqueInstance;
    }

    public synchronized static JvmMonitor getInstance(MongoDBManager connectionSource, boolean logToLocal) {
        if (uniqueInstance == null) {
            uniqueInstance = new JvmMonitor(connectionSource, logToLocal);
        }
        return uniqueInstance;
    }

    private JvmMonitor(MongoDBManager connectionSource, boolean logToLocal) {
        this(connectionSource, DEFAULT_REFRESH_SECONDS, logToLocal);
    }

    private JvmMonitor(MongoDBManager connectionSource, int periodSeconds, boolean logToLocal) {
        this.connectionSource = connectionSource;
        this.logToLocal = logToLocal;
//        logger.info("jvm monitor start  ...");
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                record();
            }

        }, periodSeconds, periodSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        executorService.shutdown();
    }

    public void record() {
        String message = "memoryUsed=" + getMemoryUsed() + "k "
                + " cpuUsed=" + getCpu() + " threadCount=" + getThreadCount();
        BasicDBObject logEntry = new BasicDBObject();
        logEntry.append("message", message);
        logEntry.append("timestamp", System.currentTimeMillis());
        String collection = getIp() + "_" + getPid();
        connectionSource.getDB("memory_sysm").getCollection(collection).insert(logEntry);
        if (logToLocal) {
            System.out.println(String.format("%s %s", new Date(), message));
        }

    }
    public static final String UNKNOW_HOST = "unknow host";

    private String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return UNKNOW_HOST;
        }
    }

    private String getPid() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    protected int getThreadCount() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }

    protected long getMemoryUsed() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024);
    }

    protected double getCpu() {
        OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        RuntimeMXBean runbean = ManagementFactory
                .getRuntimeMXBean();
        long uptime = runbean.getUptime();
        //long processCpuTime = osbean.getProcessCpuTime();
        long processCpuTime = 10;
        //cpu count
        int processors = osbean.getAvailableProcessors();
        //uptime in milliseconds ,and    processCpuTime in nao seconds
        double cpu = (processCpuTime - lastProcessCpuTime) / ((uptime - lastUptime) * 10000f * processors);
        lastProcessCpuTime = processCpuTime;
        lastUptime = uptime;
        return (int) cpu;  //
    }

}
