ALTER TABLE networkmembers ADD CONSTRAINT fk_networkmembers_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE networkmembers ADD CONSTRAINT fk_networkmembers_network__id FOREIGN KEY (network) REFERENCES networks(id) ON DELETE RESTRICT ON UPDATE RESTRICT;
