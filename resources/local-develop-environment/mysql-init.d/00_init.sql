CREATE
    USER 'navigation-local'@'localhost' IDENTIFIED BY 'navigation-local';
CREATE
    USER 'navigation-local'@'%' IDENTIFIED BY 'navigation-local';

GRANT ALL PRIVILEGES ON *.* TO
    'navigation-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'navigation-local'@'%';

CREATE
    DATABASE navigation_jungle DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;