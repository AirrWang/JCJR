package com.ql.jcjr.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName: FtpUtil
 * Description:
 * Author: Administrator
 * Date: Created on 202016/10/21.
 */
public class FtpUtil {

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param urlStr
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param path
     *            FTP服务器保存目录，是目录形式,如/photo/
     * @param filename
     *            上传到FTP服务器上的文件名,是自己定义的名字，
     * @param input
     *            输入流
     * @return 成功返回true，否则返回false
     */
    public static String uploadFile(String urlStr, int port, String username,
            String password, String path, String filename, InputStream input) {
        String success = "";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(urlStr, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            boolean loginResult = ftpClient.login(username, password);//登录
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return "Ftp连接失败！";
            }

            if (!loginResult) {
                ftpClient.disconnect();
                return "登录失败！";
            }
            if (!ftpClient.changeWorkingDirectory(path)) {
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
            }
            //上传上去的图片数据格式（）一定要写这玩意，不然在服务器就打不开了
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//            ftpClient.changeWorkingDirectory(path);
            ftpClient.setBufferSize(5000);
            ftpClient.setControlEncoding("UTF-8");
            //不加这句话图片上传失败
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(filename, input);
            input.close();
            ftpClient.logout();
            success = "success";
        } catch (IOException e) {
            e.printStackTrace();
            success = "上传失败！";
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
}
