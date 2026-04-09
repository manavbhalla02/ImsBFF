-- IMS seed data for PostgreSQL
-- Run after: infra/sql/postgres-schema.sql
--
-- This script seeds:
-- 1. plan records
-- 2. feature catalog records
-- 3. plan_feature grants
-- 4. plan_feature_selection_rule rows
-- 5. plan_feature_restriction rules
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

WITH plan_feature_seed(plan_code, feature_key, grant_type) AS (
    VALUES
        ('BASIC', 'TKT_PAGE', 'DIRECT'),
        ('BASIC', 'COMMENT_SECTION', 'DIRECT'),
        ('BASIC', 'SUBJECT', 'DIRECT'),
        ('BASIC', 'DESCRIPTION', 'DIRECT'),
        ('BASIC', 'AUTH', 'SELECTABLE'),

        ('PREMIUM', 'TKT_PAGE', 'DIRECT'),
        ('PREMIUM', 'COMMENT_SECTION', 'DIRECT'),
        ('PREMIUM', 'SUBJECT', 'DIRECT'),
        ('PREMIUM', 'DESCRIPTION', 'DIRECT'),
        ('PREMIUM', 'AI_SUBJECT_SUMMARY', 'DIRECT'),
        ('PREMIUM', 'AUTH', 'SELECTABLE'),

        ('ADVANCED', 'TKT_PAGE', 'DIRECT'),
        ('ADVANCED', 'COMMENT_SECTION', 'DIRECT'),
        ('ADVANCED', 'SUBJECT', 'DIRECT'),
        ('ADVANCED', 'DESCRIPTION', 'DIRECT'),
        ('ADVANCED', 'AUTO_TKT_RESOLUTION', 'DIRECT'),
        ('ADVANCED', 'COMMENT_RES', 'DIRECT'),
        ('ADVANCED', 'AI_SUBJECT_SUMMARY', 'DIRECT'),
        ('ADVANCED', 'AUTH', 'SELECTABLE')
)
INSERT INTO plan_feature (plan_id, feature_id, grant_type)
SELECT p.plan_id, f.feature_id, seed.grant_type::feature_grant_type
FROM plan_feature_seed seed
JOIN plan p ON p.plan_code = seed.plan_code
JOIN feature f ON f.feature_key = seed.feature_key
ON CONFLICT (plan_id, feature_id) DO UPDATE
SET grant_type = EXCLUDED.grant_type;

WITH selection_rule_seed(plan_code, entitlement_feature_key, applies_to_feature_key, min_selectable, max_selectable, selection_required) AS (
    VALUES
        ('BASIC', 'AUTH', 'AUTH', 1, 1, TRUE),
        ('BASIC', 'AUTH', 'AUTH_TYPE_BASIC', 1, 1, TRUE),
        ('BASIC', 'AUTH', 'AUTH_TYPE_OAUTH', 1, 1, TRUE),
        ('BASIC', 'AUTH', 'AUTH_TYPE_SSO', 1, 1, TRUE),

        ('PREMIUM', 'AUTH', 'AUTH', 1, 2, TRUE),
        ('PREMIUM', 'AUTH', 'AUTH_TYPE_BASIC', 1, 3, TRUE),
        ('PREMIUM', 'AUTH', 'AUTH_TYPE_OAUTH', 1, 3, TRUE),
        ('PREMIUM', 'AUTH', 'AUTH_TYPE_SSO', 1, 3, TRUE),

        ('ADVANCED', 'AUTH', 'AUTH', 1, 3, TRUE),
        ('ADVANCED', 'AUTH', 'AUTH_TYPE_BASIC', 1, 10, TRUE),
        ('ADVANCED', 'AUTH', 'AUTH_TYPE_OAUTH', 1, 10, TRUE),
        ('ADVANCED', 'AUTH', 'AUTH_TYPE_SSO', 1, 10, TRUE)
),
resolved_selection_rules AS (
    SELECT
        pf.plan_feature_id,
        applies_to_feature.feature_id AS applies_to_feature_id,
        seed.min_selectable,
        seed.max_selectable,
        seed.selection_required
    FROM selection_rule_seed seed
    JOIN plan p ON p.plan_code = seed.plan_code
    JOIN feature entitlement_feature ON entitlement_feature.feature_key = seed.entitlement_feature_key
    JOIN feature applies_to_feature ON applies_to_feature.feature_key = seed.applies_to_feature_key
    JOIN plan_feature pf
        ON pf.plan_id = p.plan_id
       AND pf.feature_id = entitlement_feature.feature_id
)
INSERT INTO plan_feature_selection_rule (
    plan_feature_id,
    applies_to_feature_id,
    selection_scope,
    min_selectable,
    max_selectable,
    selection_required
)
SELECT
    plan_feature_id,
    applies_to_feature_id,
    'DIRECT_CHILDREN'::selection_scope,
    min_selectable,
    max_selectable,
    selection_required
FROM resolved_selection_rules
ON CONFLICT (plan_feature_id, applies_to_feature_id, selection_scope) DO UPDATE
SET
    min_selectable = EXCLUDED.min_selectable,
    max_selectable = EXCLUDED.max_selectable,
    selection_required = EXCLUDED.selection_required,
    updated_at = CURRENT_TIMESTAMP;

WITH restriction_seed(plan_code, feature_key, restriction_key, restriction_type, restriction_operator, restriction_value, restriction_unit) AS (
    VALUES
        ('BASIC', 'COMMENT_SECTION', 'MAX_COMMENTS', 'NUMERIC', 'LTE', '200', 'COUNT'),

        ('PREMIUM', 'COMMENT_SECTION', 'MAX_COMMENTS', 'NUMERIC', 'LTE', '1000', 'COUNT'),

        ('ADVANCED', 'COMMENT_SECTION', 'MAX_COMMENTS', 'NUMERIC', 'LTE', '5000', 'COUNT'),
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
