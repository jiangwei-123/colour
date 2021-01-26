package com.jw.colour.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final int DEFAULT_POOL_SIZE = 50;

    public BlockingQueue<FTPClient> blockingQueue;

    private FTPClientFactory factory;

    public FTPClientPool(FTPClientFactory factory) throws Exception {
        this(DEFAULT_POOL_SIZE, factory);
    }

    public FTPClientPool(int poolSize, FTPClientFactory factory) throws Exception {
        this.factory = factory;
        this.blockingQueue = new ArrayBlockingQueue<>(poolSize);
        initPool(poolSize);
    }

    /**
     * init pool
     *
     * @param maxPoolSize maxPoolSize
     * @author jw
     * @date 2021/1/26
     **/
    private void initPool(int maxPoolSize) throws Exception {
        int count = 0;
        while (count < maxPoolSize) {
            this.addObject();
            count++;
        }
    }


    @Override
    public FTPClient borrowObject() throws Exception {
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


    @Override
    public void returnObject(FTPClient client) throws Exception {
        LOGGER.info("returnObject before blockingQueue size:{}", blockingQueue.size());
        if ((client != null)) {
            if (!blockingQueue.offer(client, 1, TimeUnit.SECONDS)) {
                LOGGER.info("returnObject offer time out,blockingQueue size:{}", blockingQueue.size());
                try {
                    factory.destroyObject(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    public void invalidateObject(FTPClient client) {
        blockingQueue.remove(client);
    }


    @Override
    public void addObject() throws Exception {
        blockingQueue.offer(factory.makeObject(), 2, TimeUnit.MINUTES);
    }


    @Override
    public int getNumIdle() {
        return blockingQueue.size();
    }

    @Override
    public int getNumActive() {
        return DEFAULT_POOL_SIZE - getNumIdle();
    }

    @Override
    public void clear() {

    }


    @Override
    public void close() {
        try {
            while (blockingQueue.iterator().hasNext()) {
                FTPClient client = blockingQueue.take();
                factory.destroyObject(client);
            }
        } catch (Exception e) {
            LOGGER.error("close ftp client pool failed...", e);
        }
    }


    @Override
    public void setFactory(PoolableObjectFactory<FTPClient> ObjectFactory)
            throws IllegalStateException, UnsupportedOperationException {

    }

}
