DROP TABLE IF EXISTS notice;

CREATE TABLE notice(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(30),
    content VARCHAR(100),
    who VARCHAR(30),
    views INT DEFAULT 0,
    createDate TIMESTAMP NOT NULL,
    updateDate TIMESTAMP NOT NULL
);

CREATE TABLE shedlock(
    name VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at TIMESTAMP(3) NULL,
    locked_by VARCHAR(255),
    PRIMARY KEY (name)
);

