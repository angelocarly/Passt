

/* Authorities */
INSERT INTO authority(id, name) VALUES(1, 'ROLE_ADMIN')
INSERT INTO authority(id, name) VALUES(2, 'ROLE_USER')

/* All passwords are lowercase: firstname123 */
/* Hashing algorithm is bcrypt 12 rounds */
INSERT INTO user(id, email, username, password, using2fa, secret2FA) VALUES('8a919ac169e3bca10169e3bccb450000', 'angelo.carly@realdolmen.com', 'angeloc', '$2a$12$nT4tBe5SxS1OqkResIUyRueoqlAqyhA.8XcC1X/w3J2.rTHVNC5fi', false, 'WAKQDDMGCTMOKMX3');
INSERT INTO user(id, email, username, password, using2fa) VALUES('8a919ac169e3bca10169e3bece290001', 'sander.beazar@realdolmen.com', 'sanderb', '$2a$12$gDEq1IuZTx2Xdon4T1k7duiGyvx6jUQJAQgWMnj/fUpRRffRqpMtW', false);
INSERT INTO user(id, email, username, password, using2fa) VALUES('8a919ac169e3bca10169e3bf35830002', 'anthony.van.noppen@realdolmen.com', 'anthonyvn', '$2a$12$gDEq1IuZTx2Xdon4T1k7duiGyvx6jUQJAQgWMnj/fUpRRffRqpMtW', false);
INSERT INTO user(id, email, username, password, using2fa) VALUES('8a919ac169e3bca10169e3c016310003', 'nathan.westerlinck@realdolmen.com', 'nathanw', '$2a$12$OV/ooVD.E7sbk9dQot6IY.50f.CpClmp4344VlVJUq5iPBFTEiOw2', false);
INSERT INTO user(id, email, username, password, using2fa) VALUES('8a919ac169e3bca10169e72ac7b40004', 'jarne.kerkaert@realdolmen.com', 'jarnek', '$2a$12$xi2lxVPKrp5kFjlgs7PJUeuhz/eqSvCCmDyRc7d6l.5dX5f/atNwK', false);

/* Map Authority_User relation */
INSERT INTO user_authority(authority_id, user_id) VALUES(1, '8a919ac169e3bca10169e3bccb450000')
INSERT INTO user_authority(authority_id, user_id) VALUES(2, '8a919ac169e3bca10169e3bece290001')
INSERT INTO user_authority(authority_id, user_id) VALUES(2, '8a919ac169e3bca10169e3bf35830002')
INSERT INTO user_authority(authority_id, user_id) VALUES(2, '8a919ac169e3bca10169e3c016310003')
INSERT INTO user_authority(authority_id, user_id) VALUES(2, '8a919ac169e3bca10169e72ac7b40004')