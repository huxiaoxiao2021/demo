package com.example.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHexample {

    static int localPort = 8080;// 本地端口
    static String remoteHost = "11.51.195.90";// 远程服务器
    static int remotePort = 80;// 远程服务端口

    public static void startSSH() throws JSchException {
        // SSH连接用户名
        String sshUser = "hujiping";
        // SSH连接密码
        String sshPassword = "!HXXjy092431!!";
        // SSH服务器
        String sshHost = "127.0.0.1";
        // SSH访问端口
        int sshPort = 22;
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        // 打印SSH服务器版本信息
        System.out.println(session.getServerVersion());
        // 设置SSH本地端口转发,本地转发到远程
        int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);
        // 删除本地端口的转发
        // session.delPortForwardingL(localPort);
        // 断开SSH链接
        // session.disconnect();
        // 设置SSH远程端口转发,远程转发到本地
        // session.setPortForwardingR(remotePort, remoteHost, localPort);
        System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
    }

    public static void testSSH() throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 设置SSH本地端口转发后，访问本地ip+port就可以访问到远程的ip+port
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
            st = conn.createStatement();
            String sql = "select * from test limit 10";
            rs = st.executeQuery(sql);
            while (rs.next())
                System.out.println(rs.getString(1));
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs!=null) {rs.close();rs=null;}
            if (st!=null) {st.close();st=null;}
            if (conn!=null) {conn.close();conn=null;}
        }
    }

    public static void main(String[] args) throws Exception {
        startSSH();
        testSSH();
    }
}