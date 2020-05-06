-- User user/pass
INSERT IGNORE INTO users (username, password, enabled)
  values ('test_user',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
    1);
 
INSERT IGNORE INTO authorities (username, authority)
  values ('test_user', 'ROLE_USER');
  
  INSERT IGNORE INTO users (username, password, enabled)
  values ('test_admin',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
    1);
 
INSERT IGNORE INTO authorities (username, authority)
  values ('test_admin', 'ROLE_ADMIN');
  
INSERT IGNORE INTO oauth_client_details
(client_id,
resource_ids,
client_secret,
scope,
authorized_grant_types,
web_server_redirect_uri,
authorities,
access_token_validity,
refresh_token_validity,
additional_information,
autoapprove)
VALUES
('test_clientid',
'OCRService',
'$2y$12$4uk/FFVSTcLgrs0ck2bRJulurP2TJcRdhwDMe9XQigvr2c4zoaKDC',
'greet,profile,upload',
'authorization_code,password,implicit,refresh_token',
'http://localhost:8049',
'ROLE_ADMIN,ROLE_USER',
120,
86400,
NULL,
true);
