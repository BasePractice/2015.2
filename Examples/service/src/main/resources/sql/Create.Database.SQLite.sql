DROP TABLE IF EXISTS 'Groups';
CREATE TABLE 'Groups' (
  id         INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  name       TEXT      NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP          DEFAULT NULL
);

DROP TABLE IF EXISTS 'Users';
CREATE TABLE 'Users' (
  id         INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  uuid       TEXT      NOT NULL UNIQUE,
  last_name  TEXT      NOT NULL,
  first_name TEXT      NOT NULL,
  middl_name TEXT      NOT NULL,
  login      TEXT      NOT NULL,
  password   TEXT      NOT NULL,
  email      TEXT               DEFAULT NULL,
  avatar_url TEXT               DEFAULT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP          DEFAULT NULL,
  group_id   INTEGER   NOT NULL,
  FOREIGN KEY (group_id) REFERENCES 'Groups' (id)
  ON DELETE CASCADE
  ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'Objects';
CREATE TABLE 'Objects' (
  id         INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  uuid       TEXT      NOT NULL UNIQUE,
  name       TEXT      NOT NULL,
  address    TEXT      NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP          DEFAULT NULL
);

DROP TABLE IF EXISTS 'ComplexIndexes';
CREATE TABLE 'ComplexIndexes' (
  id         INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  uuid       TEXT      NOT NULL UNIQUE,
  name       TEXT      NOT NULL,
  min        DOUBLE    NOT NULL DEFAULT 0.0,
  max        DOUBLE    NOT NULL DEFAULT 1.7E308,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP          DEFAULT NULL
);

DROP TABLE IF EXISTS 'Indexes';
CREATE TABLE 'Indexes' (
  id               INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  uuid             TEXT      NOT NULL UNIQUE,
  complex_index_id INTEGER   NOT NULL,
  name             TEXT      NOT NULL,
  created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP          DEFAULT NULL,
  FOREIGN KEY (complex_index_id) REFERENCES 'ComplexIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'ProfileIndexes';
CREATE TABLE 'ProfileIndexes' (
  id         INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  uuid       TEXT      NOT NULL UNIQUE,
  name       TEXT      NOT NULL,
  min        DOUBLE    NOT NULL DEFAULT 0.0,
  max        DOUBLE    NOT NULL DEFAULT 1.7E308,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP          DEFAULT NULL
);

DROP TABLE IF EXISTS 'ComplexIndexes_Of_ProfileIndexes';
CREATE TABLE 'ComplexIndexes_Of_ProfileIndexes' (
  id               INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  profile_index_id INTEGER NOT NULL,
  complex_index_id INTEGER NOT NULL,
  FOREIGN KEY (profile_index_id) REFERENCES 'ProfileIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (complex_index_id) REFERENCES 'ComplexIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'ProfileIndexes_Of_Objects';
CREATE TABLE 'ProfileIndexes_Of_Objects' (
  id               INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  profile_index_id INTEGER NOT NULL,
  object_id        INTEGER NOT NULL,
  FOREIGN KEY (profile_index_id) REFERENCES 'ProfileIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (object_id) REFERENCES 'Objects' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'Inspections';
CREATE TABLE 'Inspections' (
  id         INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  uuid       TEXT      NOT NULL UNIQUE,
  object_id  INTEGER   NOT NULL,
  user_id    INTEGER   NOT NULL,
  complete   TIMESTAMP          DEFAULT NULL,
  content    BLOB               DEFAULT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP          DEFAULT NULL,
  FOREIGN KEY (object_id) REFERENCES 'Objects' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (user_id) REFERENCES 'Users' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'ComplexIndexValues';
CREATE TABLE 'ComplexIndexValues' (
  id               INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  complex_index_id INTEGER   NOT NULL,
  primary_value    DOUBLE    NOT NULL,
  general_value    DOUBLE    NOT NULL,
  created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP          DEFAULT NULL,
  FOREIGN KEY (complex_index_id) REFERENCES 'ComplexIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'IndexValues';
CREATE TABLE 'IndexValues' (
  id                     INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  index_id               INTEGER   NOT NULL,
  complex_index_value_id INTEGER   NOT NULL,
  value                  DOUBLE    NOT NULL,
  created_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             TIMESTAMP          DEFAULT NULL,
  FOREIGN KEY (index_id) REFERENCES 'Indexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (complex_index_value_id) REFERENCES 'ComplexIndexValues' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'GradeProfileValues';
CREATE TABLE 'GradeProfileValues' (
  id            INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  profile_id    INTEGER   NOT NULL,
  inspection_id INTEGER   NOT NULL,
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP          DEFAULT NULL,
  FOREIGN KEY (profile_id) REFERENCES 'ProfileIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (inspection_id) REFERENCES 'Inspections' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS 'ComparativeValues';
CREATE TABLE 'ComparativeValues' (
  id            INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT,
  profile_id    INTEGER   NOT NULL,
  inspection_id INTEGER   NOT NULL,
  value         BLOB      NOT NULL,
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP          DEFAULT NULL,
  FOREIGN KEY (profile_id) REFERENCES 'ProfileIndexes' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT,
  FOREIGN KEY (inspection_id) REFERENCES 'Inspections' (id)
  ON DELETE CASCADE ON UPDATE RESTRICT
);

-- Content

INSERT INTO 'Groups' (name) VALUES ('Управление');
-- Password: 123456
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id)
VALUES (upper('cb01450e-4b96-4a99-9315-73a1826a75cf'),
        'Хлебников',
        'Андрей',
        'Александрович',
        'a.khlebnikov',
        'E10ADC3949BA59ABBE56E057F20F883E',
        'virtuszold@mail.ru',
        1);
INSERT INTO 'Groups' (name) VALUES ('Эксперт');
-- Password: qXNg1OHUHN4c
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('F38B8907-A66C-431D-AF49-BB0CFF8A0E3C', 'Ефимов', 'Корнил', 'Валентинович', 'EfimovKornil170',
   'D083842D9CF786A7B1186053488EBDDA', '', 2, 'http://randus.ru/avatars/m/myAvatar7.png');
-- Password: w1YchVgosKn4
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('5D6BD0E0-9A41-4A52-AC63-DEDE26AE3F89', 'Осипова', 'Ариадна', 'Федоровна', 'OsipovaAriadna25',
   'CD4BDEDF091601FAF652C0849680BD44', '', 2, 'http://randus.ru/avatars/m/myAvatar12.png');
-- Password: FjIVq1lg1ERn
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('0B665E2B-EBE3-4291-94D2-5574F62F04F0', 'Васильева', 'Ольга', 'Максимовна ', 'VasilevaOlga162',
   '7105D67999F330524F06DCE757C9AAB7', '', 2, 'http://randus.ru/avatars/m/myAvatar4.png');
-- Password: AETaRlv4w961
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('3530DB01-F671-481F-92E5-6644DF21D02E', 'Вихирев', 'Вениамин', 'Александрович', 'VihirevVeniamin295',
   '67D1BFC56D2A616D190A44507443E9EE', '', 2, 'http://randus.ru/avatars/w/myAvatar17.png');
-- Password: 8huKtA9KzD2w
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('F8454AAB-5E93-479F-BCAD-2CC74B4D0811', 'Савельев', 'Владлен', 'Богданович', 'SavelevVladlen258',
   '8378ABB4D89F62C8B021C2FCB45FC5ED', '', 2, 'http://randus.ru/avatars/m/myAvatar1.png');
-- Password: NW9gITVi5Azs
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('411B05ED-9CCA-4F90-A8E1-70D0ADF4532A', 'Степанов', 'Евлампий', 'Григорьевич', 'StepanovEvlampiy243',
   '60C8529D5183EA691E921704A8D8B9DA', '', 2, 'http://randus.ru/avatars/m/myAvatar8.png');
-- Password: 7JA2b0tmpOfo
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('F06C874F-0425-48B1-BB58-5458C0C7E454', 'Рзаев', 'Юлиан', 'Олегович', 'RzaevYulian268',
   'DEC7703F3B707E85EFAA991613BDA3CB', '', 2, 'http://randus.ru/avatars/m/myAvatar3.png');
-- Password: aPZGNcNTYP7y
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('759F642C-73BE-4CD1-983C-0768F8FA0875', 'Козлова', 'Виргиния', 'Валентиновна ', 'KozlovaVirginiya184',
   'B9808557D08BE04152D197CFBA15EBF5', '', 2, 'http://randus.ru/avatars/m/myAvatar17.png');
-- Password: ykyOPkf3xMtT
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('FBF1C4CA-4810-4262-BC79-3503B9AC04A6', 'Алексеева', 'Рогнеда', 'Кирилловна ', 'AlekseevaRogneda47',
   '6BB7673A7C7C0C0083123E07B2B5D89C', '', 2, 'http://randus.ru/avatars/m/myAvatar15.png');
-- Password: 5t1GBktVoB9U
INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url) VALUES
  ('61EE8B06-90F4-47D1-AD73-0E6EB4BC325F', 'Павлова', 'Любава', 'Владиславовна ', 'PavlovaLyubava131',
   'CC5330A3F52E7C935F6360B3A8FBB9B6', '', 2, 'http://randus.ru/avatars/w/myAvatar13.png');

--------
INSERT INTO 'Objects'(uuid, name, address) VALUES ('E7A47FF4-8D02-4A0D-8F5C-D6BEAAB8FD0F', 'ОАО "Корвет"', '');

