    DO $$
    BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'permissions') THEN
    CREATE TYPE Permissions AS ENUM ('Owner', 'Member', 'Guest');
END IF;
    END$$;
CREATE TABLE IF NOT EXISTS users (id uuid PRIMARY KEY, username VARCHAR(50) NOT NULL, password_hash VARCHAR(128) NOT NULL);
ALTER TABLE users ADD CONSTRAINT users_username_unique UNIQUE (username);
CREATE TABLE IF NOT EXISTS networks (id uuid PRIMARY KEY, "name" VARCHAR(50) NOT NULL, network_addr BIGINT NOT NULL, containers_network_addr BIGINT NOT NULL, client_network_addr BIGINT NOT NULL, docker_network_id VARCHAR(64) NULL, tun_interface_name VARCHAR(15) NULL, vrf_interface_name VARCHAR(15) NULL, "owner" uuid NOT NULL, numeric_id BIGSERIAL NOT NULL, CONSTRAINT fk_networks_owner__id FOREIGN KEY ("owner") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT chk_Networks_unsigned_numeric_id CHECK (numeric_id BETWEEN 0 AND 4294967295));
ALTER TABLE networks ADD CONSTRAINT nw_numeric_id UNIQUE (numeric_id);
ALTER TABLE networks ADD CONSTRAINT full_name UNIQUE ("name", "owner");
CREATE TABLE IF NOT EXISTS networkmembers ("user" uuid, network uuid, permission Permissions NOT NULL, CONSTRAINT pk_NetworkMembers PRIMARY KEY ("user", network));
CREATE TABLE IF NOT EXISTS tokens (id VARCHAR(128) NOT NULL, "user" uuid NOT NULL, expires_at TIMESTAMP NOT NULL, CONSTRAINT fk_tokens_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT);
CREATE TABLE IF NOT EXISTS tunnelsessions (id uuid PRIMARY KEY, network uuid NOT NULL, client_ip BIGINT NOT NULL, "user" uuid NOT NULL, CONSTRAINT fk_tunnelsessions_network__id FOREIGN KEY (network) REFERENCES networks(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT fk_tunnelsessions_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT chk_TunnelSessions_unsigned_client_ip CHECK (client_ip BETWEEN 0 AND 4294967295));
CREATE TABLE IF NOT EXISTS containers (id uuid PRIMARY KEY, container_id VARCHAR(64) NOT NULL, network uuid NOT NULL, ip_address BIGINT NOT NULL, host_veth_name VARCHAR(15) NOT NULL, container_veth_name VARCHAR(15) NOT NULL, CONSTRAINT fk_containers_network__id FOREIGN KEY (network) REFERENCES networks(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT chk_Containers_unsigned_ip_address CHECK (ip_address BETWEEN 0 AND 4294967295));
CREATE SEQUENCE IF NOT EXISTS Networks_numeric_id_seq START WITH 1 MINVALUE 1 MAXVALUE 9223372036854775807;
