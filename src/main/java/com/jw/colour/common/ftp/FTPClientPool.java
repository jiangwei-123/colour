package com.jw.colour.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * implements objectPool to create an ftpClient pool
 *
 * @author jw on 2021/1/25
 */
public class FTPClientPool implements ObjectPool<FTPClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPClientPool.class);

    public static final int DEFAULT_POOL_SIZE = 10;

    public BlockingQueue<FTPClient> blockingQueue;

    private FTPClientFactory factory;

    public FTPClientPool(FTPClientFactory factory) throws Exception {
        this(DEFAULT_POOL_SIZE, factory);
    }

    public FTPClientPool(int poolSize, FTPClientFactory factory) throws Exception {
        this.factory = factory;
        this.blockingQueue = new ArrayBlockingQueue<FTPClient>(poolSize);
        initPool(poolSize);
    }


    /**
     * init pool
     *
     * @param maxPoolSize max pool size
     * @throws Exception
     */
    private void initPool(int maxPoolSize) throws Exception {
        int count = 0;
        while (count < maxPoolSize) {
            this.addObject();
            count++;
        }
    }


    @Override
    public FTPClient borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        LOGGER.info("borrowObject ,blockingQueue size:{}", blockingQueue.size());
        FTPClient client = blockingQueue.poll(1, TimeUnit.MINUTES);
        if (client == null) {
            this.addObject();
            LOGGER.info("client==null ");
            client = borrowObject();
        } else if (!factory.validateObject(client)) {
            LOGGER.info("get validateObject is:false");
            //invalidateObject(client);
            try {
                factory.destroyObject(client);
            } catch (Exception e) {
                LOGGER.info("invalidateObject error:", e);
                throw e;
            }
            //制造并添加新对象到池中
            LOGGER.info("addObject client");
            this.addObject();
            LOGGER.info("borrowObject. again");
            client = borrowObject();
        }
        return client;

    }

    /**
     * 返还一个对象(链接)
     */
    @Override
    public void returnObject(FTPClient client) throws Exception {
        LOGGER.info("returnObject before blockingQueue size:{}", blockingQueue.size());
        if ((client != null)) {
            if (!blockingQueue.offer(client, 1, TimeUnit.SECONDS)) {
                LOGGER.info("returnObject offer time out,blockingQueue size:{}", blockingQueue.size());
                try {
                    factory.destroyObject(client);
                } catch (Exception e) {
                    throw e;
                }
            }

        }
    }

    /**
     * 移除无效的对象(FTP客户端)
     */
    @Override
    public void invalidateObject(FTPClient client) throws Exception {
        blockingQueue.remove(client);
    }

    /**
     * 增加一个新的链接，超时失效
     */
    @Override
    public void addObject() throws Exception {
        blockingQueue.offer(factory.makeObject(), 2, TimeUnit.MINUTES);
    }


    /**
     * 重新连接
     */
    public FTPClient reconnect() throws Exception {
        return factory.makeObject();
    }

    /**
     * 获取空闲链接数(这里暂不实现)
     */
    @Override
    public int getNumIdle() {
        return blockingQueue.size();
    }

    /**
     * 获取正在被使用的链接数
     */
    @Override
    public int getNumActive() {
        return DEFAULT_POOL_SIZE - getNumIdle();
    }

    @Override
    public void clear() throws Exception {

    }

    /**
     * 关闭连接池
     */
    @Override
    public void close() {
        try {
            while (blockingQueue.iterator().hasNext()) {
                FTPClient client = blockingQueue.take();
                factory.destroyObject(client);
            }
        } catch (Exception e) {
            LOGGER.error("close ftp client pool failed...{}", e);
        }
    }


    @Override
    public void setFactory(PoolableObjectFactory<FTPClient> poolableObjectFactory) throws IllegalStateException, UnsupportedOperationException {

    }

}
