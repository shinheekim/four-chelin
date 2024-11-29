DELIMITER $$

CREATE PROCEDURE GeneratePopularKeywords()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE random_keyword VARCHAR(255);
    DECLARE random_count INT;

    WHILE i <= 1000000 DO
            -- 랜덤한 키워드 생성 (영문 5~10자 또는 한글 2~5자)
            SET random_keyword = CASE
                                     WHEN RAND() < 0.5 THEN
                                         -- 영어 문자열 (5~10자)
                                         CONCAT(
                                                 CHAR(FLOOR(RAND() * 26) + 97),
                                                 CHAR(FLOOR(RAND() * 26) + 97),
                                                 CHAR(FLOOR(RAND() * 26) + 97),
                                                 CHAR(FLOOR(RAND() * 26) + 97),
                                                 CHAR(FLOOR(RAND() * 26) + 97)
                                         )
                                     ELSE
                                         -- 한글 문자열 (2~5자)
                                         CONCAT(
                                                 CHAR(FLOOR(RAND() * (12622 - 12593) + 12593) USING utf8mb4),
                                                 CHAR(FLOOR(RAND() * (12622 - 12593) + 12593) USING utf8mb4)
                                         )
END;

            -- 랜덤한 검색 횟수 (1~10000)
            SET random_count = FLOOR(RAND() * 10000) + 1;

            -- 데이터 삽입
INSERT INTO popular_keyword (keyword, search_count, trend_date)
VALUES (random_keyword, random_count, CURDATE());

SET i = i + 1;
END WHILE;
END$$

DELIMITER ;

# 프로시저 호출
CALL GeneratePopularKeywords();