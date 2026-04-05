CREATE TABLE user_roles (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username    VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255),
    role        VARCHAR(20) NOT NULL CHECK (role IN ('viewer', 'operator', 'admin')),
    ad_group    VARCHAR(255),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_login  TIMESTAMPTZ
);

CREATE TABLE ad_group_role_mappings (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ad_group    VARCHAR(255) NOT NULL UNIQUE,
    role        VARCHAR(20) NOT NULL CHECK (role IN ('viewer', 'operator', 'admin')),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_roles_username ON user_roles(username);
CREATE INDEX idx_user_roles_role ON user_roles(role);
