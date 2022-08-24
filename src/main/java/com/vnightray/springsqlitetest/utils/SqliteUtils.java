package com.vnightray.springsqlitetest.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteUtils {

    private static Logger logger = LoggerFactory.getLogger(SqliteUtils.class);

    public static void initSqliteDb(Connection connection) {
        // 判断数据表是否存在
        boolean hasConnection = false;
        try {
            hasConnection = true;
            connection.prepareStatement("select * from pro").execute();
        } catch (SQLException e) {
            logger.error("table pro is not exist");
            hasConnection = false;
        }

        // 不存在时创建表
        if (!hasConnection) {
            logger.info(">>>start init blog db");
            File file = null;
            try {
                file = ResourceUtils.getFile("classpath:sql" + File.separator + "init.sql");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 获取sql
            String sql = null;
            FileInputStream fis = null;
            InputStreamReader ireader = null;
            try {
                fis = new FileInputStream(file);
                ireader = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(ireader);
                String content = "";
                StringBuilder sb = new StringBuilder();
                while (content != null) {
                    content = br.readLine();
                    if (content == null) break;
                    sb.append(content.trim());
                }
                sql = sb.toString();
            } catch (FileNotFoundException | UnsupportedEncodingException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                try {
                    ireader.close();
                    fis.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            String[] sqls = sql.split(";");

            try {
                connection.setAutoCommit(false);
                for (String ss : sqls) {
                    System.out.println(ss);
                    connection.prepareStatement(ss).execute();
                }
                connection.commit();
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            logger.info("finish init table");
        } else {
            logger.info("pro db table exists");
        }
    }

    public static String initSqliteFile(String path) {

        Resource resource = new ClassPathResource(path);

        File file = null;

        try {
            file = resource.getFile();
            logger.info("exists db path: " + file.getAbsolutePath());
        } catch (IOException exception) {
            file = new File(Thread.currentThread().getContextClassLoader()
                    .getResource("").getPath() + File.separator + path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            logger.info("to create new db path: " + file.getAbsolutePath());
        }

        return file.getAbsolutePath();
//        File file = new File(path);
//        File dir = file.getParentFile();
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            }
//            catch (IOException exception) {
//                exception.printStackTrace();
//            }
//        }
    }


    public static String getFilePath(String url) {
        url = url.replace("jdbc:sqlite::resource:", "");
        return url;
    }

}
