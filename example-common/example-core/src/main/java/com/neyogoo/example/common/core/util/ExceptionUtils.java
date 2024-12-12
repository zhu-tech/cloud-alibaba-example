package com.neyogoo.example.common.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 关于异常的工具类
 */
public class ExceptionUtils {

    /**
     * 搜索基础包的前缀
     */
    private static final String BASE_PACKAGE_NAME = "com.neyogoo.example";

    /**
     * 将 ErrorStack 转化为 String
     */
    public static String getStackTraceAsString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        // SKIP CHECKSTYLE:START
        e.printStackTrace(new PrintWriter(stringWriter));
        // SKIP CHECKSTYLE:END
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起.
     */
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 在request中获取异常类
     */
    public static Throwable getThrowable(HttpServletRequest request) {
        Throwable ex = null;
        if (request.getAttribute("exception") != null) {
            ex = (Throwable) request.getAttribute("exception");
        } else if (request.getAttribute("javax.servlet.error.exception") != null) {
            ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
        return ex;
    }

    /**
     * 转化异常信息
     * 格式 异常描述:[]
     * -> ClassName:[], MethodName:[], LineNumber:[]
     * -> ClassName:[], MethodName:[], LineNumber:[]
     * ...
     * @param e 一个具体的异常
     * @return 描述String

     */
    public static String resolveExInfoToString(Exception e) {
        String exDesc = e.getMessage();
        if (e instanceof NullPointerException) {
            exDesc = "空指针";
        }

        StringBuilder exBuilder = new StringBuilder();
        String format = "-> ClassName:[%s], MethodName:[%s], LineNumber:[%d]\n";
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().contains(BASE_PACKAGE_NAME)) {
                exBuilder.append(
                        String.format(
                                format,
                                stackTraceElement.getClassName(),
                                stackTraceElement.getMethodName(),
                                stackTraceElement.getLineNumber()
                        )
                );
            }
        }

        return String.format("异常描述:[%s]\n%s", exDesc, exBuilder);
    }

    /**
     * 转化异常信息
     *
     * @param e 一个具体的异常
     * @return 描述List
     */
    public static List<String> resolveExInfoToList(Exception e) {
        String exDesc = e instanceof NullPointerException ? "空指针" : e.getMessage();
        String format = "%s -> ClassName:[%s]，MethodName:[%s]，LineNumber:[%d]";
        return Arrays.stream(e.getStackTrace())
                .filter(o -> o.getClassName().contains(BASE_PACKAGE_NAME))
                .map(o -> String.format(format, exDesc, o.getClassName(), o.getMethodName(), o.getLineNumber()))
                .collect(Collectors.toList());
    }

    /**
     * 转化异常信息
     *
     * @param e 一个具体的异常
     * @return 一个具体的Map key 发生异常的ClassName value 该Class多个方法栈{@link MethodDesc}
     */
    public static Map<String, List<MethodDesc>> resolveExInfoToMap(Exception e) {
        Map<String, List<MethodDesc>> descMap = new HashMap<>(16);
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (!stackTraceElement.getClassName().contains(BASE_PACKAGE_NAME)) {
                continue;
            }
            MethodDesc methodDesc = new MethodDesc();
            methodDesc.setMethodName(stackTraceElement.getMethodName());
            methodDesc.setLineNumber(stackTraceElement.getLineNumber());

            List<MethodDesc> methodDescs = descMap.get(stackTraceElement.getClassName());
            if (CollectionUtils.isEmpty(methodDescs)) {
                methodDescs = new ArrayList<>();
                methodDescs.add(methodDesc);
            }
            descMap.put(stackTraceElement.getClassName(), methodDescs);
        }
        return descMap;
    }


    /**
     * 方法 描述
     */
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MethodDesc {

        /**
         * 方法名字
         */
        private String methodName;

        /**
         * 发生错误的行
         */
        private Integer lineNumber;
    }
}
