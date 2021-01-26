package com.jw.colour.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * First, create a factory class to create ftpclient
 *
 * @author jw on 2021/1/25
 */
public class FTPClientFactory implements PoolableObjectFactory<FTPClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPClientFactory.class);

    private Properties ftpProperties;

    public FTPClientFactory(String ftpProperties) {
        try {
            this.ftpProperties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(ftpProperties));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * make Object
     *
     * @author jw
     * @date 2021/1/25
     * @return: org.apache.commons.net.ftp.FTPClient
     **/
    @Override
    public FTPClient makeObject() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpProperties.getProperty("ftpClient_encoding"));
        ftpClient.setConnectTimeout(Integer.parseInt(ftpProperties.getProperty("ftpClient_clientTimeout")));
        try {
            ftpClient.connect(ftpProperties.get("ftpClient_host").toString(),
                    Integer.parseInt(ftpProperties.getProperty("ftpClient_port")));
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }
            boolean result = ftpClient.login(ftpProperties.getProperty("ftpClient_username"),
                    ftpProperties.getProperty("ftpClient_password"));
            ftpClient.setFileType(Integer.parseInt(ftpProperties.getProperty("ftpClient_transferFileType")));
            //Linux下模式必须设置
            ftpClient.enterLocalPassiveMode();
            if (!result) {
                LOGGER.warn("ftpClient login failed... username is {}", ftpProperties.getProperty("ftpClient_username"));
            }

        } catch (Exception e) {
            LOGGER.error("ftp client login failed...", e);
            e.printStackTrace();
            throw e;
        }

        return ftpClient;
    }

    /**
     * destroy Object
     *
     * @param ftpClient ftpClient
     * @author jw
     * @date 2021/1/25
     **/
    @Override
    public void destroyObject(FTPClient ftpClient) throws Exception {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (Exception e) {
            LOGGER.error("ftp client logout failed...", e);
            throw e;
        } finally {
            if (ftpClient != null) {
                ftpClient.disconnect();
            }
        }

    }

    /**
     * validate Object
     *
     * @param ftpClient ftpClient
     * @author jw
     * @date 2021/1/25
     * @return: boolean
     **/
    @Override
    public boolean validateObject(FTPClient ftpClient) {
        try {
            return ftpClient.sendNoOp();
        } catch (Exception e) {
            LOGGER.error("Failed to validate client:", e);
        }
        return false;
    }

    @Override
    public void activateObject(FTPClient ftpClient) throws Exception {

    }

    @Override
    public void passivateObject(FTPClient ftpClient) throws Exception {

    }
}
