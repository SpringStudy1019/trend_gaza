use enjoytrip;
show tables;

CREATE TABLE review (
    review_idx INT PRIMARY KEY auto_increment,
    score ENUM ('1', '2', '3', '4', '5') NOT NULL,
    title VARCHAR(20) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    companion ENUM('FAMILY', 'COUPLE', 'BUSINESS', 'FRIEND', 'SOLO') NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modify_date TIMESTAMP,
    user_id VARCHAR(20) NOT NULL,
    content_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (content_id) REFERENCES attraction_info(content_id)
);

select * from review;

