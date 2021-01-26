package com.jw.colour.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.io.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.StringTokenizer;

/**
 * use ftp to get files
 *
 * @author jw on 2021/1/15
 */
public class FtpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtil.class);

    private static FTPClientPool ftpClientPool;

    static {
        FTPClientFactory factory = new FTPClientFactory("ftpClient.properties");
        try {
            ftpClientPool = new FTPClientPool(factory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FtpUtil() {

    }

    //Static inner classes to create singletons
    private static class InnerClass {
        private static FtpUtil ftpUtil = new FtpUtil();
    }

    public static FtpUtil getFtpUtil() {
        return InnerClass.ftpUtil;
    }


    /**
    * Get connection object
    * @author jw
    * @date 2021/1/26
    * @return: org.apache.commons.net.ftp.FTPClient
    **/
    public FTPClient getFTPClient() {
        try {
            FTPClient ftpClient = ftpClientPool.borrowObject();
            ftpClient.changeWorkingDirectory("/");
            return ftpClient;
        } catch (Exception e) {
            LOGGER.error("getFTPClient", e.getCause());
            return null;
        }
    }

    /**
     * 当前命令执行完成命令完成
     *
     * @throws IOException
     */
    public void complete(FTPClient ftpClient) throws IOException {
        ftpClient.completePendingCommand();
    }

    /**
     * 当前线程任务处理完成，加入到队列的最后
     *
     * @return
     */
    public void disconnect(FTPClient ftpClient) {
        if (ftpClient != null) {
            try {
                ftpClientPool.returnObject(ftpClient);
            } catch (Exception e) {
                LOGGER.error("disconnect,error:", e.getCause());
            }
        }

    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param remoteFile 上传到FTP服务器上的文件名
     * @param input      本地文件流
     * @return 成功返回true，否则返回false
     * @Version1.0
     */
    public boolean uploadFile(String remoteFile, InputStream input) {
        boolean result = false;
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("uploadFile getFTPClient is null");
            return false;
        }
        try {
            result = ftpClient.storeFile(remoteFile, input);
            if (!result) {
                LOGGER.error("uploadFile file {} error:{}", remoteFile, ftpClient.getReplyString());
            }
        } catch (Exception e) {
            LOGGER.error("uploadFile  error:{}", e.getCause());
        } finally {
            Util.closeQuietly(input);
            disconnect(ftpClient);
        }
        return result;
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param remoteFile 上传到FTP服务器上的文件名
     * @param localFile  本地文件
     * @return 成功返回true，否则返回false
     * @Version1.0
     */
    public boolean uploadFile(String remoteFile, String localFile) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(localFile));
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException:{},error:{}", localFile, e);
        }
        return uploadFile(remoteFile, input);
    }

    /**
     * 拷贝文件
     *
     * @param fromFile
     * @param toFile
     * @return
     * @throws Exception
     */
    public boolean copyFile(String fromFile, String toFile) {

        InputStream in = getFileInputStream(fromFile);
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("copyFile getFTPClient is null");
            return false;
        }
        boolean flag = false;
        try {
            flag = ftpClient.storeFile(toFile, in);
            in.close();
        } catch (IOException e) {
            LOGGER.error("ftpClient IOException", e.getCause());
        } finally {
            Util.closeQuietly(in);
            disconnect(ftpClient);
        }
        return flag;
    }

    /**
     * 获取文件输入流
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public InputStream getFileInputStream(String fileName) {
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("getFileInputStream getFTPClient is null");
            return null;
        }

        ByteArrayInputStream in = null;
        try {
            ftpClient.retrieveFile(fileName, fos);
            in = new ByteArrayInputStream(fos.toByteArray());
            fos.close();
        } catch (IOException e) {
            LOGGER.error("ftp getFileInputStream", e.getCause());
        } finally {
            disconnect(ftpClient);
        }
        return in;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @return
     * @Version1.0
     */
    public boolean downFile(String remoteFile, String localFile) {
        boolean result = false;
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("downFile getFTPClient is null");
            return false;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(remoteFile, os);
            result = true;
        } catch (Exception e) {
            LOGGER.error("downFile ", e.getCause());
        } finally {
            Util.closeQuietly(os);
            disconnect(ftpClient);
        }
        return result;
    }

    /**
     * 从ftp中获取文件流
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public InputStream getInputStream(String filePath) {
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("getInputStream getFTPClient is null");
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = ftpClient.retrieveFileStream(filePath);
        } catch (IOException e) {
            LOGGER.error("getInputStream ", e.getCause());
        } finally {
            disconnect(ftpClient);
        }
        return inputStream;
    }

    /**
     * ftp中文件重命名
     *
     * @param fromFile
     * @param toFile
     * @return
     * @throws Exception
     */
    public boolean rename(String fromFile, String toFile) {
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("rename getFTPClient is null");
            return false;
        }
        boolean result = false;
        try {
            result = ftpClient.rename(fromFile, toFile);
        } catch (IOException e) {
            LOGGER.error("rename ", e.getCause());
        } finally {
            disconnect(ftpClient);
        }
        return result;
    }

    /**
     * 获取ftp目录下的所有文件
     *
     * @param dir
     * @return
     */
    public FTPFile[] getFiles(String dir) {
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("getFiles getFTPClient is null");
            return null;
        }

        FTPFile[] files = new FTPFile[0];
        try {
            files = new FTPFile[0];
            files = ftpClient.listFiles(dir);
        } catch (IOException e) {
            LOGGER.error("获取ftp目录下的所有文件", e.getCause());
        } finally {
            disconnect(ftpClient);
        }

        return files;
    }

    /**
     * 获取ftp目录下的某种类型的文件
     *
     * @param dir
     * @param filter
     * @return
     */
    public FTPFile[] getFiles(String dir, FTPFileFilter filter) {
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("getFiles getFTPClient is null");
            return null;
        }

        FTPFile[] files = new FTPFile[0];
        try {
            files = new FTPFile[0];
            files = ftpClient.listFiles(dir, filter);
        } catch (IOException e) {
            LOGGER.error("获取ftp目录下的某种类型的文件", e.getCause());
        } finally {
            disconnect(ftpClient);
        }

        return files;
    }

    /**
     * 创建文件夹
     *
     * @param remoteDir g
     * @return 如果已经有这个文件夹返回false
     */
    public boolean makeDirectory(String remoteDir) {
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("makeDirectory getFTPClient is null");
            return false;
        }

        boolean result = false;
        try {
            result = ftpClient.makeDirectory(remoteDir);
        } catch (IOException e) {
            LOGGER.error("创建文件夹", e.getCause());
        } finally {
            disconnect(ftpClient);
        }
        return result;
    }

    public boolean mkDirs(String dir) {
        boolean result = false;
        if (null == dir) {
            return result;
        }
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("mkdirs getFTPClient is null");
            return false;
        }

        try {
            ftpClient.changeWorkingDirectory("/");
            StringTokenizer dirs = new StringTokenizer(dir, File.separator);
            String temp = null;
            while (dirs.hasMoreElements()) {
                temp = dirs.nextElement().toString();
                //创建目录
                ftpClient.makeDirectory(temp);
                //进入目录
                ftpClient.changeWorkingDirectory(temp);
                result = true;
            }
            ftpClient.changeWorkingDirectory("/");
        } catch (Exception e) {
            LOGGER.error("ftp mkdirs", e.getCause());
        } finally {
            disconnect(ftpClient);
        }

        return result;
    }

    public boolean removeDirectoryALLFile(String pathName) {
        boolean result = false;
        if (null == pathName) {
            return result;
        }
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("mkdirs getFTPClient is null");
            return result;
        }
        try {
            ftpClient.changeWorkingDirectory("/");
            FTPFile[] files = ftpClient.listFiles(pathName);
            if (null != files && files.length > 0) {
                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        removeDirectoryALLFile(pathName + File.separator + file.getName());
                    } else {
                        result = ftpClient.deleteFile(pathName + File.separator + file.getName());
                        if (!result) {
                            LOGGER.error("删除指定文件" + pathName + "/" + file.getName() + "失败!");
                            continue;
                        }
                    }
                }
            }
            ftpClient.changeWorkingDirectory(File.separator + pathName.substring(0, pathName.lastIndexOf(File.separator)));
            result = ftpClient.removeDirectory(File.separator + pathName);
            return result;
        } catch (IOException e) {
            LOGGER.error("删除指定文件夹" + pathName + "失败：" + e);
            return false;
        } finally {
            disconnect(ftpClient);
        }
    }

    public boolean changeWorkingDirectory(String dir) {
        boolean result = false;
        if (null == dir) {
            return result;
        }
        FTPClient ftpClient = getFTPClient();
        if (ftpClient == null) {
            LOGGER.error("changeWorkingDirectory getFTPClient is null");
            return false;
        }
        try {
            ftpClient.changeWorkingDirectory("/");
            result = ftpClient.changeWorkingDirectory(dir);
        } catch (Exception e) {
            LOGGER.error("ftp changeWorkingDirectory", e.getCause());
        } finally {
            disconnect(ftpClient);
        }
        return result;
    }

    public void destroy() {
        ftpClientPool.close();
    }


}
