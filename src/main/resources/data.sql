INSERT INTO plan (plan_id, plan_name, billing_period, base_price, created_at)
VALUES
    (1, 'Basic', 'MONTHLY', 0.00, CURRENT_TIMESTAMP),
    (2, 'Premium', 'MONTHLY', 49.00, CURRENT_TIMESTAMP),
    (3, 'Advanced', 'MONTHLY', 99.00, CURRENT_TIMESTAMP);

INSERT INTO feature (feature_id, feature_key, feature_name, description, is_active, created_at, updated_at)
VALUES
    (1, 'TKT_PAGE', 'Ticket Page', 'Root ticket page capability', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'COMMENT_SECTION', 'Comment Section', 'Comment capability inside ticket page', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'SUBJECT', 'Subject', 'Ticket subject field', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'DESCRIPTION', 'Description', 'Ticket description field', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 'AUTO_TKT_RESOLUTION', 'Auto Ticket Resolution', 'Automatic ticket resolution capability', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 'COMMENT_RES', 'Comment Response', 'Automated comment response capability', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (7, 'AI_SUBJECT_SUMMARY', 'AI Subject Summary', 'AI generated summary for ticket subjects', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (8, 'AUTH', 'Authentication', 'Authentication root feature', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (11, 'AUTH_TYPE_BASIC', 'Basic Authentication Type', 'Basic authentication type', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (9, 'AUTH_TYPE_OAUTH', 'OAuth Authentication Type', 'OAuth authentication type', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (12, 'AUTH_TYPE_SSO', 'SSO Authentication Type', 'SSO authentication type', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (15, 'AUTH_PROVIDER_BASIC', 'Basic Provider', 'Basic authentication provider', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10, 'AUTH_PROVIDER_GOOGLE', 'Google Provider', 'Google OAuth provider', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (13, 'AUTH_PROVIDER_GITHUB', 'GitHub Provider', 'GitHub OAuth provider', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (14, 'AUTH_PROVIDER_LINKEDIN', 'LinkedIn Provider', 'LinkedIn OAuth provider', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (16, 'AUTH_PROVIDER_OKTA', 'Okta Provider', 'Okta SSO provider', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO plan_feature (plan_feature_id, plan_id, feature_id, grant_type, created_at)
VALUES
    (1, 1, 8, 'SELECTABLE', CURRENT_TIMESTAMP),
    (2, 2, 8, 'SELECTABLE', CURRENT_TIMESTAMP),
    (3, 3, 8, 'SELECTABLE', CURRENT_TIMESTAMP);

INSERT INTO plan_feature_selection_rule (
    plan_feature_selection_rule_id,
    plan_feature_id,
    applies_to_feature_id,
    selection_scope,
    min_selectable,
    max_selectable,
    selection_required,
    created_at,
    updated_at
)
VALUES
    (1, 1, 8, 'DIRECT_CHILDREN', 1, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 11, 'DIRECT_CHILDREN', 1, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 1, 9, 'DIRECT_CHILDREN', 1, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 1, 12, 'DIRECT_CHILDREN', 1, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 2, 8, 'DIRECT_CHILDREN', 1, 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 2, 11, 'DIRECT_CHILDREN', 1, 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (7, 2, 9, 'DIRECT_CHILDREN', 1, 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (8, 2, 12, 'DIRECT_CHILDREN', 1, 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (9, 3, 8, 'DIRECT_CHILDREN', 1, 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10, 3, 11, 'DIRECT_CHILDREN', 1, 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (11, 3, 9, 'DIRECT_CHILDREN', 1, 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (12, 3, 12, 'DIRECT_CHILDREN', 1, 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
