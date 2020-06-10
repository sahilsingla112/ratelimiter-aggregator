DROP TABLE IF EXISTS user_api_limit;
DROP TABLE IF EXISTS api_info;

CREATE TABLE api_info(
  id int NOT NULL PRIMARY KEY,
  url VARCHAR(1024) NOT NULL,
  ratelimit INT not null,
  rate_limit_strategy VARCHAR(32) default 'BETTER_FIXED_WINDOW',
  accuracy_level INT
);

CREATE TABLE user_api_limit (
  user_id VARCHAR(16) NOT NULL,
  api_id int NOT NULL,
  ratelimit INT not null,
  PRIMARY KEY (user_id, api_id),
  Foreign key (api_id) references api_info(id)
);
