package cn.artoria.game_scripts_controller.WebSocket;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;

@Controller
public class MessageHandler {
    @Value("${OSS_ACCESS_KEY_ID}")
    private String OSS_ACCESS_KEY_ID;

    @Value("${OSS_ACCESS_KEY_SECRET}")
    private String OSS_ACCESS_KEY_SECRET;

    @MessageMapping("/monitoring")
    @SendTo("/topic/monitoring")
    public Message monitoring(Message message) {
        return message;
    }

    @MessageMapping("/shot")
    @SendTo("/topic/shot")
    public Message shot(Message message) throws com.aliyuncs.exceptions.ClientException {
        String endpoint = "https://oss-cn-qingdao.aliyuncs.com";
        // 填写Bucket名称
        String bucketName = "game-scripts-controller";
        // 填写Object完整路径。Object完整路径中不能包含Bucket名称。
        String objectName = message.getSender();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);

        try {
            // 指定签名URL过期时间为10分钟。
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 5);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
            req.setExpiration(expiration);
            URL signedUrl = ossClient.generatePresignedUrl(req);
            System.out.println(signedUrl);
            message.setContent(signedUrl.toString());
            return message;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return message;
    }
}
