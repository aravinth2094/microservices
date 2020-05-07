CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (username)
);
   
CREATE TABLE IF NOT EXISTS authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username),
  UNIQUE(username, authority)
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
    authentication_id varchar(255) NOT NULL PRIMARY KEY,
    token_id varchar(255) NOT NULL,
    token blob NOT NULL,
    user_name varchar(255) NOT NULL,
    client_id varchar(255) NOT NULL,
    authentication blob NOT NULL,
    refresh_token varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
    token_id varchar(255) NOT NULL,
    token blob NOT NULL,
    authentication blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists oauth_client_details (
    client_id VARCHAR(256) PRIMARY KEY,
    resource_ids VARCHAR(256),
    client_secret VARCHAR(256),
    scope VARCHAR(256),
    authorized_grant_types VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities VARCHAR(256),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information VARCHAR(4096),
    autoapprove VARCHAR(256)
);