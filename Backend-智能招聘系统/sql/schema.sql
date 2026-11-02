-- 智能招聘系统建表脚本（MySQL），共 9 张表
CREATE DATABASE IF NOT EXISTS recruit DEFAULT CHARACTER SET utf8mb4;
USE recruit;

-- 1. 账号表（三种角色共用）
CREATE TABLE user (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    role     VARCHAR(16) NOT NULL              -- SEEKER / ENTERPRISE / ADMIN
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 管理员
CREATE TABLE admin (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name    VARCHAR(64),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 求职者
CREATE TABLE job_seeker (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT NOT NULL,
    name      VARCHAR(64),
    phone     VARCHAR(20),
    education VARCHAR(32),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 企业
CREATE TABLE enterprise (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    user_id  INT NOT NULL,
    name     VARCHAR(128),
    industry VARCHAR(64),
    status   VARCHAR(16) DEFAULT 'PENDING',    -- PENDING / APPROVED / REJECTED
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 岗位分类
CREATE TABLE job_category (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 岗位
CREATE TABLE job (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    enterprise_id INT NOT NULL,
    category_id INT,
    title       VARCHAR(128) NOT NULL,
    description TEXT,
    status      VARCHAR(16) DEFAULT 'OPEN',     -- OPEN / CLOSED
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enterprise_id) REFERENCES enterprise(id),
    FOREIGN KEY (category_id) REFERENCES job_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 简历
CREATE TABLE resume (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    seeker_id  INT NOT NULL,
    content    TEXT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seeker_id) REFERENCES job_seeker(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. 投递记录（含状态跟踪）
CREATE TABLE application (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    job_id     INT NOT NULL,
    seeker_id  INT NOT NULL,
    resume_id  INT,
    status     VARCHAR(16) DEFAULT 'SUBMITTED', -- SUBMITTED / REVIEWING / ACCEPTED / REJECTED
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id)    REFERENCES job(id),
    FOREIGN KEY (seeker_id) REFERENCES job_seeker(id),
    FOREIGN KEY (resume_id) REFERENCES resume(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. 企业审核
CREATE TABLE enterprise_audit (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    enterprise_id INT NOT NULL,
    status        VARCHAR(16),
    audit_time    DATETIME,
    FOREIGN KEY (enterprise_id) REFERENCES enterprise(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================= 初始演示数据 =================
-- 安全说明：密码为明文仅用于本地演示，生产环境务必改为 BCrypt 等加盐哈希存储。
INSERT INTO user(username, password, role) VALUES
('seeker', '123456', 'SEEKER'),
('enterprise', '123456', 'ENTERPRISE'),
('admin', '123456', 'ADMIN'),
('pendingco', '123456', 'ENTERPRISE');

INSERT INTO job_seeker(user_id, name, phone, education) VALUES
(1, '苏然', '13800138000', '本科');

INSERT INTO enterprise(user_id, name, industry, status) VALUES
(2, '星辰科技', '互联网', 'APPROVED'),
(4, '待审制造公司', '制造业', 'PENDING');

INSERT INTO admin(user_id, name) VALUES (3, '系统管理员');

INSERT INTO job_category(name) VALUES ('Java 后端'), ('前端'), ('算法');

INSERT INTO job(enterprise_id, category_id, title, description, status) VALUES
(1, 1, 'Java 开发工程师', '负责后端服务的设计与开发，熟悉 Spring/MySQL。', 'OPEN'),
(1, 2, '前端工程师', '负责 H5 页面开发，熟悉 Vue/React。', 'OPEN');
