package com.zhixinhuixue.armor.helper;

import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.service.impl.ZSYUserService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Lang on 2017/10/20 0020.
 */
public class MailHelper {
    //jwt密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    //jwt发行者
    @Value("${jwt.issuer}")
    private String jwtIssuer;

    private static final Logger logger = LoggerFactory.getLogger(ZSYUserService.class);

    public static final String HOST = "smtp.163.com";
    public static final String PROTOCOL = "smtp";
    public static final int PORT = 25;
    public static final String FROM = "langzhezsh2@163.com";//发件人的email
    public static final String PWD = "langzhe";//发件人密码


    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址
        props.put("mail.store.protocol" , PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口
        props.put("mail.smtp.auth" , true);

        Authenticator authenticator = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }

        };
        Session session = Session.getDefaultInstance(props , authenticator);

        return session;
    }

    public static void send(String toEmail) {
        String activeCode =MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(toEmail),
                        ZSYConstants.HINT_EMAIL_KEY), 32, false);
        String content = "<p>您好 <br><br>欢迎加入知心慧学!<br><br>"
                +"帐户需要验证邮箱才能使用，赶紧激活成为知心慧学正式的一员吧:)<br><br>请登录后输入以下验证码验证邮箱："
                +"<br><p>"+activeCode+"</p></p>";

        Session session = getSession();
        try {
            logger.info("--邮箱验证内容--"+content);
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("知心慧学积分账号激活邮件");
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");

            Transport.send(msg);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
            throw new ZSYAuthException("激活邮件发送失败");
        }
    }
}
