package com.suchtool.nicelog.process.impl;

import com.suchtool.nicelog.process.NiceLogDetailProcess;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.property.NiceLogProcessProperty;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class NiceLogProcessDefaultImpl implements NiceLogProcess, ApplicationRunner {

    private volatile BlockingQueue<NiceLogInnerBO> asyncQueue;

    @Autowired
    private NiceLogDetailProcess niceLogDetailProcess;

    @Autowired
    private NiceLogProcessProperty niceLogProcessProperty;

    private volatile Thread recordAsyncThread;

    @Override
    public void run(ApplicationArguments args) {
        try {
            doCheckAndUpdateConfig();
        } catch (Exception e) {
            System.err.println("nicelog application runner error");
            e.printStackTrace();
        }
    }

    @Override
    public void process(NiceLogInnerBO logInnerBO) {
        try {
            niceLogDetailProcess.preProcess(logInnerBO);

            if (niceLogProcessProperty.getEnableRecordSync()) {
                niceLogDetailProcess.recordSync(logInnerBO);
            }

            if (niceLogProcessProperty.getEnableRecordAsync()) {
                if (asyncQueue != null) {
                    boolean offer = asyncQueue.offer(logInnerBO);
                    if (!offer) {
                        System.err.println("nicelog logQueue is full");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("nicelog process error");
            e.printStackTrace();
        }
    }

    public void doCheckAndUpdateConfig() {
        if (niceLogProcessProperty.getEnableRecordAsync()) {
            asyncResourceCheckAndInit();
        } else {
            asyncResourceDelete();
        }
    }

    private void recordAsync() {
        while (true) {
            try {
                if (niceLogProcessProperty.getEnableRecordAsync()) {
                    niceLogDetailProcess.recordAsync(asyncQueue.take());
                } else {
                    asyncResourceDelete();
                    break;
                }
            } catch (Exception e) {
                System.err.println("nicelog recordAsync exception");
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查并初始化异步资源
     */
    private void asyncResourceCheckAndInit() {
        asyncThreadStop();

        if (asyncQueue != null) {
            asyncQueue.clear();
        }
        asyncQueue = new LinkedBlockingQueue<NiceLogInnerBO>(niceLogProcessProperty.getRecordAsyncQueueCapacity());

        asyncThreadCheckAndInit();
    }

    /**
     * 删除相关资源
     */
    private void asyncResourceDelete() {
        if (asyncQueue != null) {
            asyncQueue.clear();
        }

        asyncThreadStop();
    }

    /**
     * 检查并初始化异步线程
     */
    private void asyncThreadCheckAndInit() {
        if (recordAsyncThread == null) {
            recordAsyncThread = new Thread(this::recordAsync);
            recordAsyncThread.start();
        } else {
            if (!recordAsyncThread.isAlive()) {
                recordAsyncThread.start();
            }
        }
    }

    /**
     * 停止异步线程
     */
    private void asyncThreadStop() {
        if (recordAsyncThread != null
                && recordAsyncThread.isAlive()) {
            recordAsyncThread.interrupt();
        }
        recordAsyncThread = null;
    }
}