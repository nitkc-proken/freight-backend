CREATE TABLE IF NOT EXISTS users (id uuid PRIMARY KEY, username VARCHAR(50) NOT NULL);
ALTER TABLE users ADD CONSTRAINT users_username_unique UNIQUE (username);
CREATE TABLE IF NOT EXISTS networks (id uuid PRIMARY KEY, "name" VARCHAR(50) NOT NULL, docker_network_id VARCHAR(64) NULL, tap_interface_name VARCHAR(15) NULL, docker_interface_name VARCHAR(15) NULL);
CREATE TABLE IF NOT EXISTS networkmembers ("user" uuid, network uuid, CONSTRAINT pk_NetworkMembers PRIMARY KEY ("user", network), CONSTRAINT fk_networkmembers_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT fk_networkmembers_network__id FOREIGN KEY (network) REFERENCES networks(id) ON DELETE RESTRICT ON UPDATE RESTRICT);
