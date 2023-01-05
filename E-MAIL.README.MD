# Add E-mail Settings to the application.properties file:
**Email Settings**
```java
spring.mail.protocol=smtp
spring.mail.host: mail.domesne.com
spring.mail.port: 465
spring.mail.username: ark-no-reply@domesne.com
spring.mail.password: wPvFSG7VaHlP
spring.mail.properties.mail.smtp.auth: true
spring.mail.properties.mail.smtp.starttls.enable: true
mail.smtp.starttls.enable=false
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.enable=true
```
# Add spring.mail in build.gradle
```java
  implementation 'org.springframework.boot:spring-boot-starter-mail'
```
# Create EmailService
```java
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendMail(String toEmail, String htmlText, String subject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom("ark-no-reply@domesne.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlText,true);
        javaMailSender.send(mimeMessage);
    }
}
```
# Add EmailService to Authentication Controller and Send Email using EmailService
```java
    @Autowired
    EmailService emailService;
    
    //generate unique UID when user registers
    newUserDetails.setUid(registrationFormDTO.getEmailAddress());
    
    //emailService.sendMail(String recipientEmailAddress, String htmlText, String subject);
    emailService.sendMail(newUserDetails.getEmailAddress(), String.format(new EmailTemplateStrings().getRegistrationEmail(),newUserDetails.getUid()),"Verify E-mail Address");
```
# Create unique UID in UserDetails (your user or userDetails model)
```java
  private String uid;
  
  public String getUid() {
        return uid;
    }
    
  public void setUid(String emailAddress) {
        if(isNull(emailAddress)){
            this.uid = null;
        } else {
            //generate unique hash based on userEmailAddress and random or fixed value
            this.uid = encoder.encode("ARK"+emailAddress);
        }
    }
```
# Create EmailTemplateStrings
```java
private String registrationEmail = """
            <!DOCTYPE html>
            <html lang="en" xmlns:th="http;//www.thymeleaf.org">
            <head></head>
            <body style="text-align:center;">
            <div>
                <table>
                    <tr>
                        <td colspan=3>
                            <h1>Thank you for registering!</h1>
                            <h3>Click the button below to verify your email address.</h3>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="button">
                            /* %s will be substituted with UID from user utilizing String.format()
                            <a href="https://www.domesne.com/arkDemo/mail?uid=%s">Verify</a>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </div>
            </body>
            </html>
            """;
```
