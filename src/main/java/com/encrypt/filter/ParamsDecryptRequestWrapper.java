package com.encrypt.filter;

import com.encrypt.biz.IEncryptOptions;
import com.encrypt.utils.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class ParamsDecryptRequestWrapper extends HttpServletRequestWrapper {

    public static final String ENCRYPT_URL_KEY = "_encrypt";
    private IEncryptOptions encryptOptions;
    private final Map<String, String[]> params = new HashMap<>();
    private InputStream is;

    public ParamsDecryptRequestWrapper(HttpServletRequest request) {
        super(request);

    }

    public ParamsDecryptRequestWrapper(HttpServletRequest request, IEncryptOptions encryptOptions) {
        this(request);
        this.encryptOptions = encryptOptions;
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            params.putAll(request.getParameterMap());
        }
        handleRequestParams();
        handleRequestBody(request);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    @Override
    public String getParameter(String name) {
        String[] valueArray = params.get(name);
        if (valueArray == null || valueArray.length == 0) {
            return null;
        }
        return valueArray[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.params;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return is == null ? null : new DefaultServletInputStream(is);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return is == null ? null : new BufferedReader(new InputStreamReader(is));
    }

    private void handleRequestBody(HttpServletRequest request) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            if (inputStream == null) {
                return;
            }
            String inputStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            if (inputStr == null || "".equals(inputStr)) {
                return;
            }
            //TODO 特殊处理 有部分会对data格式化处理,多一对引号,理论上避免不止一对,进行替换处理
            if (inputStr.startsWith("\"") && inputStr.endsWith("\"")) {
                inputStr = inputStr.replaceAll("\"", "");
            }
            inputStr = encryptOptions.decrypt(inputStr);
            this.is = IOUtils.toInputStream(inputStr, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取requestBody内容失败");
        }
    }

    private void handleRequestParams() {
        if (encryptOptions == null || !params.containsKey(ENCRYPT_URL_KEY)) {
            return;
        }
        String[] encryptParams = params.get(ENCRYPT_URL_KEY);
        params.remove(ENCRYPT_URL_KEY);
        for (String s : encryptParams) {
            String decrypt = encryptOptions.decrypt(s);
            handleParamsValue(decrypt, params);
        }
    }

    private void handleParamsValue(String value, Map<String, String[]> params) {
        if (value == null || "".equals(value)) {
            return;
        }
        Arrays.stream(value.split("&")).forEach(item -> {
            String[] split = item.split("=");
            if (split.length != 2) {
                log.error("错误参数, 长度拆解之后不为2: {}", item);
                return;
            }
            String key = split[0];
            params.put(key, ArrayUtils.append(params.get(key), split[1]));
        });
    }
}
