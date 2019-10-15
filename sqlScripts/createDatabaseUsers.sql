CREATE USER 'webAppUser'@'localhost' IDENTIFIED BY 'webAppUser';
FLUSH PRIVILEGES;

GRANT ALL PRIVILEGES ON * . * TO 'webAppUser'@'localhost';

CREATE USER 'adminPanelAdmin'@'localhost' IDENTIFIED BY 'adminPanelAdmin';
FLUSH PRIVILEGES;

GRANT ALL PRIVILEGES ON * . * TO 'adminPanelAdmin'@'localhost';