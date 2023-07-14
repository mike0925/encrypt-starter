package com.encrypt.filter;

import com.encrypt.biz.IEncryptOptions;
import com.encrypt.config.EncryptProperties;
import com.encrypt.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "encryptFilter")
public class ParamsDecryptFilter implements Filter {

    private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //默认只有配置路径才能放行
        IEncryptOptions encryptOptions = SpringContextUtils.getBean(IEncryptOptions.class);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //排除路径判断
        String excludeUrl = EncryptProperties.excludeUrl;
        String requestURI = request.getRequestURI();
        boolean pathMatchFlag = false;
        if (StringUtils.hasText(excludeUrl)) {
            pathMatchFlag = Arrays.stream(excludeUrl.split(",")).anyMatch(item -> PATH_MATCHER.match(item, requestURI));
        }
        if (pathMatchFlag) {
            response.setHeader("x-no-encrypt", Boolean.TRUE.toString());
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //加解密操作
        ParamsDecryptRequestWrapper requestWrapper = new ParamsDecryptRequestWrapper(request, encryptOptions);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        byte[] data = responseWrapper.getResponseData();
        String encryptData = encryptOptions.encrypt(new String(data, StandardCharsets.UTF_8));
        writeResponse(response, encryptData);
    }

    private void writeResponse(HttpServletResponse response, String responseString)
            throws IOException {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            out = response.getWriter();
            out.write(responseString);
        } catch (IOException e) {
            log.error("客户段信息返回异常", e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}
