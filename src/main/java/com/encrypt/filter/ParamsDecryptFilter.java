package com.encrypt.filter;

import com.encrypt.biz.IEncryptOptions;
import com.encrypt.config.EncryptProperties;
import com.encrypt.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        IEncryptOptions encryptOptions = SpringContextUtils.getBean(IEncryptOptions.class);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String excludeUrl = EncryptProperties.excludeUrl;
        String requestURI = request.getRequestURI();
        if (StringUtils.hasText(excludeUrl) && Arrays.asList(excludeUrl.split(",")).contains(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
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
            response.setContentType("text/plain;charset=utf-8");
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
