-- 1. Create a db (already create by docker-compose, so commented out)
-- CREATE DATABASE tyrdb;

-- 2. Users table (additional metadata for users)
-- user_id references uid() (stored as uuid). We keep this table lightweight.
CREATE TABLE IF NOT EXISTS users (
  user_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  email text NOT NULL UNIQUE,             -- optional, for convenience
  hashed_password text NOT NULL,          -- store hashed password (e.g., bcrypt)
  is_active boolean DEFAULT true,         -- is the user active?
  last_login timestamptz,                   -- last login timestamp
  display_name text,
  avatar_url text,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- 4. Roles table
CREATE TABLE IF NOT EXISTS roles (
  role_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL UNIQUE,         -- e.g., 'admin', 'editor', 'viewer'
  description text,
  created_at timestamptz DEFAULT now()
);

-- 5. Permissions table (granular permissions)
CREATE TABLE IF NOT EXISTS permissions (
  permission_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL UNIQUE,         -- e.g., 'users.read', 'invoices.write'
  description text,
  created_at timestamptz DEFAULT now()
);

-- 6. Role -> Permission mapping (many-to-many)
CREATE TABLE IF NOT EXISTS role_permissions (
  role_id uuid NOT NULL REFERENCES roles(role_id) ON DELETE CASCADE,
  permission_id uuid NOT NULL REFERENCES permissions(permission_id) ON DELETE CASCADE,
  PRIMARY KEY (role_id, permission_id)
);

-- 7. User -> Role mapping (many-to-many).
CREATE TABLE IF NOT EXISTS user_roles (
  user_id uuid NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  role_id uuid NOT NULL REFERENCES roles(role_id) ON DELETE CASCADE,
  assigned_at timestamptz DEFAULT now(),
  PRIMARY KEY (user_id, role_id)
);

-- 8. Convenience view: compute a user's effective permissions (for reads only)
CREATE OR REPLACE VIEW user_permissions AS
SELECT
  u.user_id,
  rp.permission_id,
  p.name AS permission_name
FROM users u
JOIN user_roles ur ON ur.user_id = u.user_id
JOIN role_permissions rp ON rp.role_id = ur.role_id
JOIN permissions p ON p.permission_id = rp.permission_id;

-- CLIENTS TABLE ------------------------------------------
CREATE TABLE IF NOT EXISTS clients (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    email text NOT NULL UNIQUE,
    phone text,
    country_code text,
    address text,
    notes text,
    created_by uuid NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

-- BLOGS TABLE ------------------------------------------
CREATE TABLE IF NOT EXISTS blogs (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    title text NOT NULL,
    slug text NOT NULL UNIQUE,
    content text NOT NULL,
    excerpt text NOT NULL,
    cover_image_url text,
    status text NOT NULL,
    published_at timestamptz,
    created_by uuid NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

-- PROVIDER TABLE ------------------------------------------
CREATE TABLE IF NOT EXISTS providers (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    contact_name text NOT NULL,
    email text NOT NULL,
    phone text NOT NULL,
    category text NOT NULL,
    website text,
    notes text,
    created_by uuid NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

-- PROVIDER TABLE ------------------------------------------
CREATE TABLE IF NOT EXISTS promotions (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    title text NOT NULL,
    description text NOT NULL,
    destination text NOT NULL,
    original_price numeric(12,2) NOT NULL,
    promo_price numeric(12,2) NOT NULL,
    currency text NOT NULL,
    image_url text,
    start_date timestamptz,
    end_date timestamptz,
    is_active boolean NOT NULL DEFAULT true,
    created_by uuid NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);