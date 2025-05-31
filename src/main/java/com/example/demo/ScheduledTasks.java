package com.example.demo;

import com.example.demo.DAO.Impl.ForumImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ForumImpl forumImpl;

    // 每天凌晨 2 点执行定时任务
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateFollowCount() {
        logger.info("开始定时更新论坛关注数");
        // 查询 forum_follow 表，统计每个论坛的实际关注数
        String countSql = "SELECT forum_id, COUNT(*) as count FROM forum_follow GROUP BY forum_id";
        List<Map<String, Object>> countResults = jdbcTemplate.queryForList(countSql);

        for (Map<String, Object> result : countResults) {
            Integer forumId = (Integer) result.get("forum_id");
            Integer actualCount = (Integer) result.get("count");
            // 更新 forums 表中的关注数
            String updateSql = "UPDATE forums SET follow_count = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, actualCount, forumId);
        }
        logger.info("论坛关注数更新完成");
    }
}