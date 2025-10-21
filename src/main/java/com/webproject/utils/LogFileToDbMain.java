package com.webproject.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单独运行：将指定日志文件内容解析后写入 sys_log 表（方案二）
 */
public class LogFileToDbMain {

    public static void main(String[] args) throws Exception {

        // 1. 连接数据库
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tlias",
                "root", "CJHLTQL2024");

        String logFile = "D:/tlias-2025-07-31-0.log"; // 日志文件地址
        BufferedReader reader = new BufferedReader(new FileReader(logFile));

        // 2. 日志格式正则匹配
        /*
          2025-08-04 00:02:03.292 [线程] INFO  loggerName-日志内容
         */
        Pattern pattern = Pattern.compile("^(.{23}) \\[(.*?)\\] (\\w+)\\s+(.*?)\\-(.*)$");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // 3. insert sql
        String sql = "INSERT INTO sys_log(level, logger, message, create_time) VALUES (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        String line;
        while ((line = reader.readLine()) != null) {
            Matcher m = pattern.matcher(line);
            if (m.matches()) {
                // 解析
                String timeStr = m.group(1);         // 2025-08-04 00:02:03.292
                String level   = m.group(3);         // INFO
                String logger  = m.group(4).trim();  // c.p.positionproject.controller.positionController
                String message = m.group(5).trim();  // 分页查询 MapQueryParam(...)

                // 写入
                ps.setString(1, level);
                ps.setString(2, logger);
                ps.setString(3, message);
                ps.setTimestamp(4,
                        Timestamp.valueOf(LocalDateTime.parse(timeStr, formatter))
                );
                ps.executeUpdate();
            } else {
                System.out.println("无法解析日志行：" + line);
            }
        }

        System.out.println("导入完成！");
        ps.close();
        conn.close();
        reader.close();
    }
}
