-- IMS seed data for PostgreSQL
-- Run after: infra/sql/postgres-schema.sql
--
-- This script seeds:
-- 1. plan records
-- 2. feature catalog records
-- 3. plan_feature mappings
-- 4. plan_feature_restriction rules
--
-- It intentionally uses stable keys and ON CONFLICT handling
-- so the script can be rerun safely.

BEGIN;

INSERT INTO plan (plan_code, plan_name, billing_period, base_price, currency_code, is_active)
VALUES
    ('BASIC', 'Basic', 'MONTHLY', 0.00, 'USD', TRUE),
    ('PREMIUM', 'Premium', 'MONTHLY', 49.00, 'USD', TRUE),
    ('ADVANCED', 'Advanced', 'MONTHLY', 99.00, 'USD', TRUE)
ON CONFLICT (plan_code) DO UPDATE
SET
    plan_name = EXCLUDED.plan_name,
    billing_period = EXCLUDED.billing_period,
    base_price = EXCLUDED.base_price,
    currency_code = EXCLUDED.currency_code,
    is_active = EXCLUDED.is_active,
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO feature (feature_key, feature_name, description, is_active)
VALUES
    ('TKT_PAGE', 'Ticket Page', 'Root ticket page capability', TRUE),
    ('COMMENT_SECTION', 'Comment Section', 'Comment capability inside ticket page', TRUE),
    ('SUBJECT', 'Subject', 'Ticket subject field', TRUE),
    ('DESCRIPTION', 'Description', 'Ticket description field', TRUE),
    ('AUTO_TKT_RESOLUTION', 'Auto Ticket Resolution', 'Automatic ticket resolution capability', TRUE),
    ('COMMENT_RES', 'Comment Response', 'Automated or assisted comment response capability', TRUE),
    ('AI_SUBJECT_SUMMARY', 'AI Subject Summary', 'AI generated summary for ticket subjects', TRUE),
    ('AUTH', 'Authentication', 'Authentication root feature', TRUE),
    ('AUTH_TYPE_BASIC', 'Basic Authentication Type', 'Basic authentication type', TRUE),
    ('AUTH_TYPE_OAUTH', 'OAuth Authentication Type', 'OAuth authentication type', TRUE),
    ('AUTH_TYPE_SSO', 'SSO Authentication Type', 'SSO authentication type', TRUE),
    ('AUTH_PROVIDER_BASIC', 'Basic Provider', 'Basic authentication provider', TRUE),
    ('AUTH_PROVIDER_GOOGLE', 'Google Provider', 'Google OAuth provider', TRUE),
    ('AUTH_PROVIDER_GITHUB', 'GitHub Provider', 'GitHub OAuth provider', TRUE),
    ('AUTH_PROVIDER_LINKEDIN', 'LinkedIn Provider', 'LinkedIn OAuth provider', TRUE),
    ('AUTH_PROVIDER_OKTA', 'Okta Provider', 'Okta SSO provider', TRUE)
ON CONFLICT (feature_key) DO UPDATE
SET
    feature_name = EXCLUDED.feature_name,
    description = EXCLUDED.description,
    is_active = EXCLUDED.is_active,
    updated_at = CURRENT_TIMESTAMP;

WITH plan_feature_seed(plan_code, feature_key) AS (
    VALUES
        ('BASIC', 'TKT_PAGE'),
        ('BASIC', 'COMMENT_SECTION'),
        ('BASIC', 'SUBJECT'),
        ('BASIC', 'DESCRIPTION'),
        ('BASIC', 'AUTH'),
        ('BASIC', 'AUTH_TYPE_BASIC'),
        ('BASIC', 'AUTH_PROVIDER_BASIC'),

        ('PREMIUM', 'TKT_PAGE'),
        ('PREMIUM', 'COMMENT_SECTION'),
        ('PREMIUM', 'SUBJECT'),
        ('PREMIUM', 'DESCRIPTION'),
        ('PREMIUM', 'AI_SUBJECT_SUMMARY'),
        ('PREMIUM', 'AUTH'),
        ('PREMIUM', 'AUTH_TYPE_BASIC'),
        ('PREMIUM', 'AUTH_TYPE_OAUTH'),
        ('PREMIUM', 'AUTH_PROVIDER_BASIC'),
        ('PREMIUM', 'AUTH_PROVIDER_GOOGLE'),
        ('PREMIUM', 'AUTH_PROVIDER_GITHUB'),

        ('ADVANCED', 'TKT_PAGE'),
        ('ADVANCED', 'COMMENT_SECTION'),
        ('ADVANCED', 'SUBJECT'),
        ('ADVANCED', 'DESCRIPTION'),
        ('ADVANCED', 'AUTO_TKT_RESOLUTION'),
        ('ADVANCED', 'COMMENT_RES'),
        ('ADVANCED', 'AI_SUBJECT_SUMMARY'),
        ('ADVANCED', 'AUTH'),
        ('ADVANCED', 'AUTH_TYPE_BASIC'),
        ('ADVANCED', 'AUTH_TYPE_OAUTH'),
        ('ADVANCED', 'AUTH_TYPE_SSO'),
        ('ADVANCED', 'AUTH_PROVIDER_BASIC'),
        ('ADVANCED', 'AUTH_PROVIDER_GOOGLE'),
        ('ADVANCED', 'AUTH_PROVIDER_GITHUB'),
        ('ADVANCED', 'AUTH_PROVIDER_LINKEDIN'),
        ('ADVANCED', 'AUTH_PROVIDER_OKTA')
)
INSERT INTO plan_feature (plan_id, feature_id)
SELECT p.plan_id, f.feature_id
FROM plan_feature_seed seed
JOIN plan p ON p.plan_code = seed.plan_code
JOIN feature f ON f.feature_key = seed.feature_key
ON CONFLICT (plan_id, feature_id) DO NOTHING;

WITH restriction_seed(plan_code, feature_key, restriction_key, restriction_type, restriction_operator, restriction_value, restriction_unit) AS (
    VALUES
        ('BASIC', 'COMMENT_SECTION', 'MAX_COMMENTS', 'NUMERIC', 'LTE', '200', 'COUNT'),
        ('BASIC', 'AUTH', 'MAX_AUTH_TYPES', 'NUMERIC', 'LTE', '1', 'COUNT'),
        ('BASIC', 'AUTH', 'MAX_PROVIDERS', 'NUMERIC', 'LTE', '1', 'COUNT'),

        ('PREMIUM', 'COMMENT_SECTION', 'MAX_COMMENTS', 'NUMERIC', 'LTE', '1000', 'COUNT'),
        ('PREMIUM', 'AUTH', 'MAX_AUTH_TYPES', 'NUMERIC', 'LTE', '2', 'COUNT'),
        ('PREMIUM', 'AUTH', 'MAX_PROVIDERS', 'NUMERIC', 'LTE', '3', 'COUNT'),

        ('ADVANCED', 'COMMENT_SECTION', 'MAX_COMMENTS', 'NUMERIC', 'LTE', '5000', 'COUNT'),
        ('ADVANCED', 'AUTH', 'MAX_AUTH_TYPES', 'NUMERIC', 'LTE', '3', 'COUNT'),
        ('ADVANCED', 'AUTH', 'MAX_PROVIDERS', 'NUMERIC', 'LTE', '10', 'COUNT'),
        ('ADVANCED', 'AI_SUBJECT_SUMMARY', 'MAX_AI_SUMMARIES_PER_DAY', 'NUMERIC', 'LTE', '500', 'COUNT')
),
resolved_restrictions AS (
    SELECT
        pf.plan_feature_id,
        seed.restriction_key,
        seed.restriction_type::restriction_type AS restriction_type,
        seed.restriction_operator::restriction_operator AS restriction_operator,
        seed.restriction_value,
        seed.restriction_unit::restriction_unit AS restriction_unit
    FROM restriction_seed seed
    JOIN plan p ON p.plan_code = seed.plan_code
    JOIN feature f ON f.feature_key = seed.feature_key
    JOIN plan_feature pf
        ON pf.plan_id = p.plan_id
       AND pf.feature_id = f.feature_id
)
INSERT INTO plan_feature_restriction (
    plan_feature_id,
    restriction_key,
    restriction_type,
    restriction_operator,
    restriction_value,
    restriction_unit
)
SELECT
    plan_feature_id,
    restriction_key,
    restriction_type,
    restriction_operator,
    restriction_value,
    restriction_unit
FROM resolved_restrictions
ON CONFLICT (plan_feature_id, restriction_key) DO UPDATE
SET
    restriction_type = EXCLUDED.restriction_type,
    restriction_operator = EXCLUDED.restriction_operator,
    restriction_value = EXCLUDED.restriction_value,
    restriction_unit = EXCLUDED.restriction_unit,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;
