package n.kidding.user.thrift;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import n.kidding.thrift.message.MessageService;
import n.kidding.thrift.user.UserService;

/**
 * 构建 thrift 客户端，调用 thrift 接口
 */
@Component
public class ServiceProvider {

    @Value("${thrift.user.ip}")
    private String serverIp;

    @Value("${thrift.user.port}")
    private int serverPort;

    @Value("${thrift.message.ip}")
    private String messageServerIp;

    @Value("${thrift.message.port}")
    private int messageServerPort;

    private enum ServiceType {
        USER,
        MESSAGE,
    }

    public UserService.Client getUserService() {
        return getService(serverIp, serverPort, ServiceType.USER);
    }

    public MessageService.Client getMessageService() {
        return getService(messageServerIp, messageServerPort, ServiceType.MESSAGE);
    }

    public <T> T getService(String ip, int port, ServiceType serviceType) {

        /**
         * thrift 服务端，传输的方式使用真传输，使用二进制协议
         * 客户端，需要和服务端使用同样的传输方式及传输协议才能通信
         */
        TSocket socket = new TSocket(ip, port, 3000);

        // 传输方式
        TTransport transport = new TFramedTransport(socket);
        try {
            // 开启连接
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            return null;
        }

        // 传输协议
        TProtocol protocol = new TBinaryProtocol(transport);

        // 构建客户端对象
        TServiceClient result = null;
        switch (serviceType) {
            case USER:
                result = new UserService.Client(protocol);
                break;
            case MESSAGE:
                result = new MessageService.Client(protocol);
                break;
        }
        return (T) result;
    }
}
