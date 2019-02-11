package n.kidding.user.client;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import n.kidding.thrift.user.dto.UserDTO;

public abstract class LoginFilter implements Filter {

    // 定义客户端缓存
    private static Cache<String, UserDTO> cache =
            CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterWrite(3, TimeUnit.MINUTES).build();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 从请求中获取 token
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            // 从 cookie 中获取 token
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("token")) {
                        token = c.getValue();
                    }
                }
            }
        }

        UserDTO userDTO = null;
        if (StringUtils.isNotBlank(token)) {
            // 从缓存中获取用户信息
            userDTO = cache.getIfPresent(token);
            if (userDTO == null) {
                // 根据 token 获取用户信息
                userDTO = requestUserInfo(token);
                if (userDTO != null) {
                    // 添加用户信息到缓存中
                    cache.put(token, userDTO);
                }
            }
        }

        if (userDTO == null) {
            response.sendRedirect("http://www.kidding.com/user/login");
            return;
        }

        login(request, response, userDTO);

        filterChain.doFilter(request, response);
    }

    protected abstract void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    private UserDTO requestUserInfo(String token) {
        String url = "http://user-edge-service:8082/user/authentication";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.addHeader("token", token);
        InputStream inputStream = null;
        try {
            HttpResponse response = client.execute(post);
            // 校验请求状态是否正常
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("request user info failed! StatusLine: " + response.getStatusLine());
            }
            inputStream = response.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = inputStream.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }

            // 将 String 转换成 UserDTO 对象
            UserDTO userDTO = new ObjectMapper().readValue(sb.toString(), UserDTO.class);
            return userDTO;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void destroy() {

    }
}
