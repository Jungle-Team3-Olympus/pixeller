CREATE
    USER 'board-local'@'localhost' IDENTIFIED BY 'board-local';
CREATE
    USER 'boardo-local'@'%' IDENTIFIED BY 'board-local';

GRANT ALL PRIVILEGES ON *.* TO
    'board-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'board-local'@'%';

CREATE
    DATABASE board_jungle DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;