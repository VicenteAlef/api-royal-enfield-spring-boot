CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tb_motorcycles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    model_name VARCHAR(100) NOT NULL,
    family VARCHAR(50) NOT NULL,
    engine_cc INTEGER NOT NULL,
    starting_price NUMERIC(10, 2) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_motorcycle_variants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    motorcycle_id UUID NOT NULL REFERENCES tb_motorcycles(id) ON DELETE CASCADE,
    variant_name VARCHAR(100) NOT NULL,
    color_name VARCHAR(100) NOT NULL,
    hex_color_code VARCHAR(10),
    price NUMERIC(10, 2) NOT NULL,
    image_url VARCHAR(255),
    included_accessories TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_motorcycle_gallery (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    motorcycle_id UUID NOT NULL REFERENCES tb_motorcycles(id) ON DELETE CASCADE,
    image_url VARCHAR(255) NOT NULL,
    caption VARCHAR(150),
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_technical_specs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    motorcycle_id UUID NOT NULL UNIQUE REFERENCES tb_motorcycles(id) ON DELETE CASCADE,
    power_hp VARCHAR(50),
    torque_nm VARCHAR(50),
    weight_kg NUMERIC(6, 2),
    fuel_capacity_l NUMERIC(5, 2),
    seat_height_mm INTEGER,
    transmission VARCHAR(50),
    front_brake VARCHAR(100),
    rear_brake VARCHAR(100),
    cooling_system VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_dealerships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    address VARCHAR(200) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_test_rides (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    preferred_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    motorcycle_id UUID NOT NULL REFERENCES tb_motorcycles(id),
    variant_id UUID REFERENCES tb_motorcycle_variants(id),
    dealership_id UUID NOT NULL REFERENCES tb_dealerships(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_motorcycles_model_name ON tb_motorcycles(model_name);
CREATE INDEX idx_motorcycles_family ON tb_motorcycles(family);
CREATE INDEX idx_variants_motorcycle_id ON tb_motorcycle_variants(motorcycle_id);
CREATE INDEX idx_gallery_motorcycle_id ON tb_motorcycle_gallery(motorcycle_id);
CREATE INDEX idx_specs_motorcycle_id ON tb_technical_specs(motorcycle_id);
CREATE INDEX idx_dealerships_city_state ON tb_dealerships(city, state);
CREATE INDEX idx_test_rides_dealership_id ON tb_test_rides(dealership_id);
CREATE INDEX idx_test_rides_motorcycle_id ON tb_test_rides(motorcycle_id);
CREATE INDEX idx_users_email ON tb_users(email);
