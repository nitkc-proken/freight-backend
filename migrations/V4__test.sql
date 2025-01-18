ALTER TABLE users ADD password_hash VARCHAR(60) NOT NULL;
ALTER TABLE networks ADD address INT NOT NULL;
ALTER TABLE networks ADD subnetmask_length INT NOT NULL;
ALTER TABLE networkmembers ADD permission Permissions NOT NULL;
