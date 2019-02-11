package n.kidding.user.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import n.kidding.thrift.user.UserService;

@Configuration
public class ThriftServer {

    @Value("${service.port}")
    private int servicePort;

    /*
     * 调用 UserService 类，会自动注入实现的类
     */
    @Autowired
    private UserService.Iface userService;

    @PostConstruct
    public void startThriftServer() {

        //初始化处理器
        TProcessor processor = new UserService.Processor<>(userService);

        // 生成非阻塞网络连接
        TNonblockingServerSocket socket = null;
        try {
            // 添加自定义的端口号
            socket = new TNonblockingServerSocket(servicePort);
        } catch (TTransportException e) {
            e.printStackTrace();
        }

        // 添加 Server 需要的参数
        TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
        args.processor(processor);
        // 传输方式
        args.transportFactory(new TFramedTransport.Factory());
        // 二进制传输格式
        args.protocolFactory(new TBinaryProtocol.Factory());

        // 构建 Server
        TServer server = new TNonblockingServer(args);
        server.serve();
    }
}

