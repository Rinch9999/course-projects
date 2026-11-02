-- H2 兼容建表脚本（对应 sql/schema.sql 的 12 张表，MySQL 版见 sql/schema.sql）
-- 注意：H2 里 user 是保留字，故账号表命名为 sys_user

CREATE TABLE IF NOT EXISTS sys_user (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    role     VARCHAR(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS admin (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name    VARCHAR(64),
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

CREATE TABLE IF NOT EXISTS community (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS building (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    community_id INT NOT NULL,
    name         VARCHAR(64),
    FOREIGN KEY (community_id) REFERENCES community(id)
);

CREATE TABLE IF NOT EXISTS room (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    building_id INT NOT NULL,
    room_no     VARCHAR(32),
    FOREIGN KEY (building_id) REFERENCES building(id)
);

CREATE TABLE IF NOT EXISTS owner (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    user_id  INT NOT NULL,
    room_id  INT,
    name     VARCHAR(64),
    phone    VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE IF NOT EXISTS repair_type (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS repair_order (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    owner_id     INT NOT NULL,
    type_id      INT,
    description  VARCHAR(500),
    status       VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owner(id),
    FOREIGN KEY (type_id)  REFERENCES repair_type(id)
);

-- 难点突破：联合索引（Explain 提速）
CREATE INDEX IF NOT EXISTS idx_repair_status_time ON repair_order(status, created_at);

CREATE TABLE IF NOT EXISTS repair_status_log (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    order_id    INT NOT NULL,
    from_status VARCHAR(16),
    to_status   VARCHAR(16),
    log_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES repair_order(id)
);

CREATE TABLE IF NOT EXISTS payment_bill (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    owner_id   INT NOT NULL,
    amount     DECIMAL(10,2) NOT NULL,
    status     VARCHAR(16) DEFAULT 'UNPAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES owner(id)
);

CREATE TABLE IF NOT EXISTS payment_record (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    bill_id   INT NOT NULL,
    paid_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bill_id) REFERENCES payment_bill(id)
);

CREATE TABLE IF NOT EXISTS announcement (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(128) NOT NULL,
    content    VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
