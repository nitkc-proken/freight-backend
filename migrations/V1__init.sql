    DO $$
    BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'permissions') THEN
    CREATE TYPE Permissions AS ENUM ('Owner', 'Member', 'Guest');
END IF;
    END$$;
CREATE TABLE IF NOT EXISTS users (id uuid PRIMARY KEY, username VARCHAR(50) NOT NULL, password_hash VARCHAR(128) NOT NULL);
ALTER TABLE users ADD CONSTRAINT users_username_unique UNIQUE (username);
CREATE TABLE IF NOT EXISTS networks (id uuid PRIMARY KEY, "name" VARCHAR(50) NOT NULL, address INT NOT NULL, subnetmask_length INT NOT NULL, docker_network_id VARCHAR(64) NULL, tap_interface_name VARCHAR(15) NULL, docker_interface_name VARCHAR(15) NULL);
CREATE TABLE IF NOT EXISTS networkmembers ("user" uuid, network uuid, permission Permissions NOT NULL, CONSTRAINT pk_NetworkMembers PRIMARY KEY ("user", network), CONSTRAINT fk_networkmembers_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT fk_networkmembers_network__id FOREIGN KEY (network) REFERENCES networks(id) ON DELETE RESTRICT ON UPDATE RESTRICT);
CREATE TABLE IF NOT EXISTS tokens (id VARCHAR(128) NOT NULL, "user" uuid NOT NULL, expires_at TIMESTAMP NOT NULL, CONSTRAINT fk_tokens_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT);
CREATE TABLE IF NOT EXISTS tunnelsessions (id uuid PRIMARY KEY, network uuid NOT NULL, client_ip INT NOT NULL, "user" uuid NOT NULL, CONSTRAINT fk_tunnelsessions_network__id FOREIGN KEY (network) REFERENCES networks(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT fk_tunnelsessions_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT);
