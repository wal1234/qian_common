package com.qian.common.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO工具类
 */
public class IOUtils extends org.apache.commons.io.IOUtils {
    /**
     * 关闭对象，屏蔽异常
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
} 