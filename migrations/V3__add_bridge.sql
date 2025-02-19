ALTER TABLE networks ADD bridge_interface_name VARCHAR(15) NULL;
ALTER TABLE containers ALTER COLUMN host_veth_name TYPE VARCHAR(15), ALTER COLUMN host_veth_name DROP NOT NULL;
ALTER TABLE containers ALTER COLUMN container_veth_name TYPE VARCHAR(15), ALTER COLUMN container_veth_name DROP NOT NULL;
