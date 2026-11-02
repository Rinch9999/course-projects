-- 智能招聘系统数据统计（难点：多表关联 + 分组统计 + 二次派生）
USE recruit;

-- ==================== 指标查询 ====================

-- 1. 岗位热度排行（按投递量降序）
SELECT j.id, j.title, COUNT(a.id) AS apply_count
FROM job j
LEFT JOIN application a ON a.job_id = j.id
GROUP BY j.id, j.title
ORDER BY apply_count DESC
LIMIT 10;

-- 2. 企业活跃度统计（发布岗位数 + 收到投递数）
SELECT e.id, e.name,
       COUNT(DISTINCT j.id) AS job_count,
       COUNT(a.id) AS apply_count
FROM enterprise e
LEFT JOIN job j         ON j.enterprise_id = e.id
LEFT JOIN application a ON a.job_id = j.id
GROUP BY e.id, e.name
ORDER BY apply_count DESC;

-- 3. 投递状态分布（SUBMITTED/REVIEWING/ACCEPTED/REJECTED）
SELECT status, COUNT(*) AS cnt
FROM application
GROUP BY status
ORDER BY cnt DESC;

-- 4. 岗位分类热度（各分类投递量）
SELECT c.name, COUNT(a.id) AS apply_count
FROM job_category c
LEFT JOIN job j         ON j.category_id = c.id
LEFT JOIN application a ON a.job_id = j.id
GROUP BY c.name
ORDER BY apply_count DESC;

-- 5. 投递趋势（按天统计投递量，招聘漏斗支撑）
SELECT DATE_FORMAT(a.created_at, '%Y-%m-%d') AS day, COUNT(*) AS cnt
FROM application a
GROUP BY day
ORDER BY day;

-- 6. 求职者学历分布
SELECT education, COUNT(*) AS cnt
FROM job_seeker
GROUP BY education
ORDER BY cnt DESC;

-- 7. 企业审核通过率
SELECT COUNT(*) AS total,
       SUM(status = 'APPROVED') AS approved,
       SUM(status = 'REJECTED') AS rejected,
       SUM(status = 'PENDING') AS pending,
       ROUND(SUM(status = 'APPROVED') / COUNT(*) * 100, 2) AS approve_rate_pct
FROM enterprise;

-- 8. 岗位状态分布（OPEN/CLOSED）
SELECT status, COUNT(*) AS cnt
FROM job
GROUP BY status;

-- 9. 转化漏斗（岗位 → 投递 → 录用）
SELECT COUNT(DISTINCT j.id) AS job_cnt,
       COUNT(a.id) AS apply_cnt,
       SUM(a.status = 'ACCEPTED') AS accepted_cnt,
       ROUND(SUM(a.status = 'ACCEPTED') / NULLIF(COUNT(a.id), 0) * 100, 2) AS accept_rate_pct
FROM job j
LEFT JOIN application a ON a.job_id = j.id;

-- ==================== 性能优化（难点突破） ====================
-- 岗位热度排行需按 application.job_id 关联后计数，无索引时回表扫描慢。
-- 建 (job_id, created_at) 联合索引，Explain type 从 ALL 变成 ref/range。
CREATE INDEX idx_application_job_time ON application(job_id, created_at);

EXPLAIN
SELECT j.id, j.title, COUNT(a.id) AS apply_count
FROM job j
LEFT JOIN application a ON a.job_id = j.id
GROUP BY j.id, j.title
ORDER BY apply_count DESC;