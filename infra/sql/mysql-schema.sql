-- IMS subscription and feature schema
-- Target: MySQL 8+
--
-- Hybrid storage note:
-- 1. MySQL stores organization, subscription, plan, feature catalog,
--    plan entitlements, restrictions, organization selections, and coupons.
-- 2. MongoDB stores the feature adjacency graph only.
--
-- Feature catalog examples:
-- 1  TKT_PAGE
-- 2  COMMENT_SECTION
-- 3  SUBJECT
-- 4  DESCRIPTION
-- 5  AUTO_TKT_RESOLUTION
-- 6  COMMENT_RES
-- 7  AI_SUBJECT_SUMMARY
-- 8  AUTH
-- 9  AUTH_TYPE_OAUTH
-- 10 AUTH_PROVIDER_GOOGLE
--
-- MongoDB adjacency examples:
-- 1  -> [2,3,4,5,7]
-- 2  -> [6]
-- 8  -> [9]
-- 9  -> [10]

CREATE DATABASE IF NOT EXISTS ims_subscription
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ims_subscription;

CREATE TABLE IF NOT EXISTS organization (
    organization_id BIGINT NOT NULL AUTO_INCREMENT,
    organization_code VARCHAR(64) NOT NULL,
    organization_name VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (organization_id),
    UNIQUE KEY uk_organization_code (organization_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS organization_domain (
    organization_domain_id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    domain_name VARCHAR(320) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (organization_domain_id),
    UNIQUE KEY uk_organization_domain_name (domain_name),
    KEY idx_org_domain_primary (organization_id, is_primary),
    CONSTRAINT fk_org_domain_org
        FOREIGN KEY (organization_id) REFERENCES organization (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS feature (
    feature_id BIGINT NOT NULL AUTO_INCREMENT,
    feature_key VARCHAR(80) NOT NULL,
    feature_name VARCHAR(120) NOT NULL,
    description VARCHAR(500) NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (feature_id),
    UNIQUE KEY uk_feature_key (feature_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `plan` (
    plan_id BIGINT NOT NULL AUTO_INCREMENT,
    plan_code VARCHAR(40) NOT NULL,
    plan_name VARCHAR(120) NOT NULL,
    billing_period ENUM('MONTHLY', 'YEARLY') NOT NULL DEFAULT 'MONTHLY',
    base_price DECIMAL(12, 2) NOT NULL,
    currency_code CHAR(3) NOT NULL DEFAULT 'USD',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (plan_id),
    UNIQUE KEY uk_plan_code (plan_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS plan_feature (
    plan_feature_id BIGINT NOT NULL AUTO_INCREMENT,
    plan_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (plan_feature_id),
    UNIQUE KEY uk_plan_feature (plan_id, feature_id),
    CONSTRAINT fk_plan_feature_plan
        FOREIGN KEY (plan_id) REFERENCES `plan` (plan_id),
    CONSTRAINT fk_plan_feature_feature
        FOREIGN KEY (feature_id) REFERENCES feature (feature_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS plan_feature_restriction (
    plan_feature_restriction_id BIGINT NOT NULL AUTO_INCREMENT,
    plan_feature_id BIGINT NOT NULL,
    restriction_key VARCHAR(80) NOT NULL,
    restriction_type ENUM('BOOLEAN', 'NUMERIC', 'TEXT', 'DURATION') NOT NULL,
    restriction_operator ENUM('EQ', 'NE', 'LT', 'LTE', 'GT', 'GTE', 'IN') NOT NULL,
    restriction_value VARCHAR(255) NOT NULL,
    restriction_unit ENUM('COUNT', 'DAY', 'MINUTE', 'FLAG', 'CHARACTER', 'PERCENT') NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (plan_feature_restriction_id),
    UNIQUE KEY uk_plan_feature_restriction (plan_feature_id, restriction_key),
    CONSTRAINT fk_plan_feature_restriction_plan_feature
        FOREIGN KEY (plan_feature_id) REFERENCES plan_feature (plan_feature_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS organization_feature_selection (
    organization_feature_selection_id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    selected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (organization_feature_selection_id),
    UNIQUE KEY uk_org_feature_selection (organization_id, feature_id),
    KEY idx_org_feature_status (organization_id, status),
    CONSTRAINT fk_org_feature_selection_org
        FOREIGN KEY (organization_id) REFERENCES organization (organization_id),
    CONSTRAINT fk_org_feature_selection_feature
        FOREIGN KEY (feature_id) REFERENCES feature (feature_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS subscription (
    subscription_id BIGINT NOT NULL AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NULL DEFAULT NULL,
    status ENUM('PENDING', 'ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED') NOT NULL DEFAULT 'PENDING',
    auto_renew BOOLEAN NOT NULL DEFAULT TRUE,
    cancelled_at TIMESTAMP NULL DEFAULT NULL,
    termination_reason VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (subscription_id),
    KEY idx_subscription_org_status_window (organization_id, status, start_at, end_at),
    CONSTRAINT fk_subscription_org
        FOREIGN KEY (organization_id) REFERENCES organization (organization_id),
    CONSTRAINT fk_subscription_plan
        FOREIGN KEY (plan_id) REFERENCES `plan` (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS coupon (
    coupon_id BIGINT NOT NULL AUTO_INCREMENT,
    coupon_code VARCHAR(64) NOT NULL,
    discount_type ENUM('PERCENTAGE', 'FIXED') NOT NULL,
    discount_value DECIMAL(12, 2) NOT NULL,
    valid_from TIMESTAMP NOT NULL,
    valid_to TIMESTAMP NULL DEFAULT NULL,
    max_redemptions INT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (coupon_id),
    UNIQUE KEY uk_coupon_code (coupon_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS coupon_plan (
    coupon_plan_id BIGINT NOT NULL AUTO_INCREMENT,
    coupon_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (coupon_plan_id),
    UNIQUE KEY uk_coupon_plan (coupon_id, plan_id),
    CONSTRAINT fk_coupon_plan_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id),
    CONSTRAINT fk_coupon_plan_plan
        FOREIGN KEY (plan_id) REFERENCES `plan` (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS subscription_coupon (
    subscription_coupon_id BIGINT NOT NULL AUTO_INCREMENT,
    subscription_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    applied_discount_value DECIMAL(12, 2) NOT NULL,
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (subscription_coupon_id),
    UNIQUE KEY uk_subscription_coupon (subscription_id, coupon_id),
    CONSTRAINT fk_subscription_coupon_subscription
        FOREIGN KEY (subscription_id) REFERENCES subscription (subscription_id),
    CONSTRAINT fk_subscription_coupon_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Suggested seed examples (optional)
-- INSERT INTO feature (feature_id, feature_key, feature_name) VALUES
-- (1, 'TKT_PAGE', 'Ticket Page'),
-- (2, 'COMMENT_SECTION', 'Comment Section'),
-- (3, 'SUBJECT', 'Subject'),
-- (4, 'DESCRIPTION', 'Description'),
-- (5, 'AUTO_TKT_RESOLUTION', 'Auto Ticket Resolution'),
-- (6, 'COMMENT_RES', 'Comment Response'),
-- (7, 'AI_SUBJECT_SUMMARY', 'AI Subject Summary'),
-- (8, 'AUTH', 'Authentication'),
-- (9, 'AUTH_TYPE_OAUTH', 'OAuth Authentication'),
-- (10, 'AUTH_PROVIDER_GOOGLE', 'Google Provider');
--
-- INSERT INTO `plan` (plan_code, plan_name, billing_period, base_price, currency_code)
-- VALUES ('BASIC', 'Basic', 'MONTHLY', 0.00, 'USD');
--
-- Restriction example:
-- BASIC + COMMENT_SECTION => MAX_COMMENTS LTE 200 COUNT
