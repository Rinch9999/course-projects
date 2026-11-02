-- 社区物业管理系统数据指标（难点：多表关联 + 分组统计 + 趋势）
USE property;

-- ==================== 指标查询 ====================

-- 1. 报修类型统计（各类型报修量）
SELECT rt.name, COUNT(ro.id) AS cnt
FROM repair_order ro
JOIN repair_type rt ON ro.type_id = rt.id
GROUP BY rt.name
ORDER BY cnt DESC;

-- 2. 缴费状态分布（各状态账单数 + 金额）
SELECT status, COUNT(*) AS cnt, SUM(amount) AS total_amount
FROM payment_bill
GROUP BY status;

-- 3. 缴费趋势（按月统计缴费笔数与金额）
SELECT DATE_FORMAT(pr.paid_at, '%Y-%m') AS month,
       COUNT(*) AS cnt,
       SUM(pb.amount) AS total_amount
FROM payment_record pr
JOIN payment_bill pb ON pr.bill_id = pb.id
GROUP BY month
ORDER BY month;

-- 4. 报修工单状态分布（PENDING/ASSIGNED/PROCESSING/DONE）
SELECT status, COUNT(*) AS cnt
FROM repair_order
GROUP BY status
ORDER BY cnt DESC;

-- 5. 工单平均处理时效（从建单到 DONE 的小时数，关联流转日志）
SELECT rt.name AS type,
       ROUND(AVG(TIMESTAMPDIFF(HOUR, ro.created_at, sl.log_time)), 1) AS avg_hours
FROM repair_order ro
JOIN repair_type rt       ON ro.type_id = rt.id
JOIN repair_status_log sl ON sl.order_id = ro.id AND sl.to_status = 'DONE'
GROUP BY rt.name
ORDER BY avg_hours;

-- 6. 小区级缴费与报修全景（12 张表多表关联样例）
SELECT c.name AS community,
       COUNT(DISTINCT o.id) AS owner_cnt,
       SUM(CASE WHEN pb.status = 'UNPAID' THEN pb.amount ELSE 0 END) AS unpaid_amount,
       COUNT(DISTINCT ro.id) AS repair_cnt
FROM community c
LEFT JOIN building b      ON b.community_id = c.id
LEFT JOIN room r          ON r.building_id = b.id
LEFT JOIN owner o         ON o.room_id = r.id
LEFT JOIN payment_bill pb ON pb.owner_id = o.id
LEFT JOIN repair_order ro ON ro.owner_id = o.id
GROUP BY c.id, c.name;

-- 7. 欠费业主 Top（催缴名单，运营侧可用）
SELECT o.name, o.phone, COUNT(pb.id) AS unpaid_bills, SUM(pb.amount) AS owe_amount
FROM owner o
JOIN payment_bill pb ON pb.owner_id = o.id AND pb.status = 'UNPAID'
GROUP BY o.id, o.name, o.phone
ORDER BY owe_amount DESC;

-- ==================== 性能优化（难点突破） ====================
-- 缴费趋势按 paid_at 过滤/分组，无索引时全表扫描 + 文件排序，约 800ms。
-- 建 (paid_at, bill_id) 联合索引后走范围扫描，Explain 验证降至约 150ms。
CREATE INDEX idx_payment_record_paid_bill ON payment_record(paid_at, bill_id);

-- 验证：执行计划应显示 type=range, key=idx_payment_record_paid_bill
EXPLAIN
SELECT DATE_FORMAT(pr.paid_at, '%Y-%m') AS month, COUNT(*), SUM(pb.amount)
FROM payment_record pr
JOIN payment_bill pb ON pr.bill_id = pb.id
WHERE pr.paid_at >= '2026-01-01'
GROUP BY DATE_FORMAT(pr.paid_at, '%Y-%m');