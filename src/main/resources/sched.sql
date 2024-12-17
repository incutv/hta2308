CREATE TABLE shedlock(
    name VARCHAR(64),  //job의 name PK로 활용
    lock_until TIMESTAMP(3) NULL,  // 2024-03-12T20:31:00.012189300 단위까지 체크 가능하도록 필드 타입 설정
    locked_at TIMESTAMP(3) NULL,
    locked_by VARCHAR(255), // 작성자 정보
    PRIMARY KEY (name)
);

