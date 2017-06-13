package com.lgame.util.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * 简单发送和接收邮件
 * @author u1
 *
 */
public class MailManager {
    private final static MailManager mailManager = new MailManager();

    private MailManager(){}
    //邮件通信会话
    Session session;
    //连接邮件发送的账号与密码
    private static final String default_username="leroy_boy@139.com";
    private static final String default_passwd="lxh19870718";
    private static final String default_smtpHost="smtp.139.com";
    private String username="systemRobot@163.com";
    private String passwd="shouquanma1987";

    public static MailManager getInstance(String username,String authPwd,String smtpHost){
        try {
            mailManager.init(username,authPwd,smtpHost);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mailManager;
    }

    public static MailManager getInstance(){
        try {
            mailManager.init(default_username,default_passwd,default_smtpHost);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mailManager;
    }

    /**
     * 邮件配置参数和连接接收邮件服务器
     * @throws MessagingException
     */
    private void init(String usrName,String pwd,String smtpHost) throws MessagingException{
        this.username = usrName;
        this.passwd = pwd;
        // 从配置文件中读取配置信息
        Properties pro = new Properties();
        pro.put("mail.smtp.host", smtpHost);
        pro.put("mail.smtp.auth", "true");
      //  pro.put("mail.smtp.localhost", "mail.digu.com");
        // Properties pro = mailConfig.getProperties();
        // 根据邮件的回话属性构造一个发送邮件的Session
        Authenticator auth = new MailAuthenticator();
        session=Session.getDefaultInstance(pro,auth);
        //session.setDebug(true);
    }

    /**
     * 创建一封简单的邮件
     * @param toAddr
     * @return
     * @throws AddressException
     * @throws MessagingException
     */
    public Message createSimpleMessage(String toAddr,String titleName,String content) throws AddressException, MessagingException{
        //建立一封邮件
        MimeMessage message=new MimeMessage(session);
        //设置发送者和接收者
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
        //设置主题
        message.setSubject(titleName);
        //设置日期
        message.setSentDate(new Date(System.currentTimeMillis()));
        //设置正文
        message.setText(content);
        return message;
    }

    public Message createSimpleMessage(String titleName,String content) throws AddressException, MessagingException{
        return createSimpleMessage(username,titleName,content);
    }

    public Message createComplexMessage(String toAddr) throws AddressException, MessagingException{
        //建立一封邮件
        MimeMessage message=new MimeMessage(session);
        //设置发送者和接收者
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
        //设置主题
        message.setSubject("使用JAVAMAIL发送邮件");
        //设置日期
        message.setSentDate(new Date(System.currentTimeMillis()));
        //设置正文
        Multipart mp=createMultiPart();
        message.setContent(mp);
        return message;
    }

    /**
     * 创建复杂的正文
     * @return
     * @throws MessagingException
     */
    private Multipart createMultiPart() throws MessagingException {
        // TODO Auto-generated method stub
        Multipart multipart=new MimeMultipart();

        //第一块
        BodyPart bodyPart1=new MimeBodyPart();
        bodyPart1.setText("创建复杂的邮件，此为正文部分");
        multipart.addBodyPart(bodyPart1);

        //第二块 以附件形式存在
        MimeBodyPart bodyPart2=new MimeBodyPart();
        //设置附件的处理器
        FileDataSource attachFile=new FileDataSource(ClassLoader.getSystemResource("attach.txt").getFile());
        DataHandler dh=new DataHandler(attachFile);
        bodyPart2.setDataHandler(dh);
        bodyPart2.setDisposition(Part.ATTACHMENT);
        bodyPart2.setFileName("test");
        multipart.addBodyPart(bodyPart2);

        return multipart;
    }

    /**
     * 发送邮件
     * @param message
     * @throws MessagingException
     */
    public void send(Message message) throws MessagingException{
        Transport.send(message);
    }

    public static void main(String[] args){
        MailManager sendReceiveMessage=MailManager.getInstance();
        try {
            Message message=sendReceiveMessage.createSimpleMessage("656515489@qq.com","服务器监控数据","服务器出问题了");
            //Message message=sendReceiveMessage.createSimpleMessage("服务器监控数据","服务器出问题了");
            sendReceiveMessage.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登陆认证
     * @author u1
     *
     */
    private class MailAuthenticator extends Authenticator{

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, passwd);
        }

    }
}

