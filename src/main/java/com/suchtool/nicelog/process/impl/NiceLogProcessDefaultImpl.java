package com.suchtool.nicelog.process.impl;

import com.suchtool.nicelog.process.NiceLogDetailProcess;
import com.suchtool.nicelog.process.NiceLogProcess;
import com.suchtool.nicelog.property.NiceLogProcessProperty;
import com.suchtool.nicelog.util.log.inner.bo.NiceLogInnerBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class NiceLogProcessDefaultImpl implements NiceLogProcess {

    private volatile Integer oldAsyncQueueCapacity;

    private volatile BlockingQueue<NiceLogInnerBO> asyncQueue;

    @Autowired
    private NiceLogDetailProcess niceLogDetailProcess;

    @Autowired
    private NiceLogProcessProperty niceLogProcessProperty;

    private volatile Thread recordAsyncThread;

    @Override
    public void process(NiceLogInnerBO logInnerBO) {
        try {
            niceLogDetailProcess.preProcess(logInnerBO);

            if (niceLogProcessProperty.getEnableRecordSync()) {
                niceLogDetailProcess.recordSync(logInnerBO);
            }

            if (niceLogProcessProperty.getEnableRecordAsync()) {
                asyncLogQueueCheckAndInit();
                asyncThreadCheckAndInit();
                boolean offer = asyncQueue.offer(logInnerBO);
                if (!offer) {
                    System.out.println("nicelog logQueue is full");
                }
            } else {
                asyncThreadStop();
                asyncLogQueueDelete();
            }
        } catch (Exception e) {
            System.err.println("nicelog process error");
            e.printStackTrace();
        }
    }

    private void recordAsync() {
        while (true) {
            try {
                niceLogDetailProcess.recordAsync(asyncQueue.take());
            } catch (Exception e) {
                System.out.println("nicelog recordAsync exception");
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查并初始化异步日志队列
     */
    private void asyncLogQueueCheckAndInit() {
        if (oldAsyncQueueCapacity == null
                || !oldAsyncQueueCapacity.equals(niceLogProcessProperty.getRecordAsyncQueueCapacity())) {
            if (asyncQueue != null) {
                asyncQueue.clear();
            }
            asyncQueue = new LinkedBlockingQueue<NiceLogInnerBO>(niceLogProcessProperty.getRecordAsyncQueueCapacity());
            oldAsyncQueueCapacity = new Integer(niceLogProcessProperty.getRecordAsyncQueueCapacity());
        }
    }

    /**
     * 删除异步队列日志
     */
    private void asyncLogQueueDelete() {
        if (asyncQueue != null) {
            asyncQueue.clear();
        }
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
                && !recordAsyncThread.isInterrupted()) {
            recordAsyncThread.interrupt();
        }
        recordAsyncThread = null;
    }
}