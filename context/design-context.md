# Design Context

This file captures the design preferences and decisions established during the subscription, feature, and registration discussions so future sessions can continue from the same baseline.

## Core Preferences

- Prefer a layered backend design with:
  - controller
  - service/application layer
  - executor layer
  - transformation layer
  - DAO layer
- Executor, transformation, and DAO are central/common concepts and should be generic reusable abstractions, not registration-only ideas.
- Favor generic Java contracts such as:
  - `Dao<T>`
  - `TransformationStrategy<T, E>`
  - `Executor<T, E>`
- Factories are used to resolve implementations by type/step.
- Registration is only one consumer of the common abstraction layer.

## Registration Flow Preferences

- Frontend registration payload currently includes:
  - organization name
  - domain name
  - subscription plan id
  - auto renew flag
- Only one controller should handle the registration POST request.
- The service layer orchestrates execution order.
- Each entity in the flow can have its own executor.
- Current registration scope is:
  - organization
  - organization domain
  - subscription
- The model should remain extensible for more organization-registration details later.

## RDBMS Direction

- Prefer PostgreSQL over SQL Server for now.
- Keep the design migration-friendly for Amazon RDS later.
- Current infra includes both:
  - MySQL DDL file
  - PostgreSQL DDL file
- PostgreSQL is the recommended practical choice for local and near-term usage.

## Feature Modeling Direction

- Features are not flat; they may have sub-features.
- Feature identity/catalog should be stored in RDBMS.
- Example feature keys discussed:
  - `TKT_PAGE`
  - `COMMENT_SECTION`
  - `SUBJECT`
  - `DESCRIPTION`
  - `AUTO_TKT_RESOLUTION`
  - `COMMENT_RES`
  - `AI_SUBJECT_SUMMARY`
  - `AUTH`
  - `AUTH_TYPE_BASIC`
  - `AUTH_TYPE_OAUTH`
  - `AUTH_TYPE_SSO`
  - `AUTH_PROVIDER_BASIC`
  - `AUTH_PROVIDER_GOOGLE`
  - `AUTH_PROVIDER_GITHUB`
  - `AUTH_PROVIDER_LINKEDIN`
  - `AUTH_PROVIDER_OKTA`
- Feature graph/adjacency is stored separately in config, not in the relational catalog.
- Current graph config artifact:
  - `src/main/resources/configs/feature-graph.json`
- The current preferred split is:
  - PostgreSQL for business truth, entitlements, restrictions, and selections
  - config JSON for feature adjacency graph

## Feature Graph Config Preference

- Feature graph config is intended for adjacency/hierarchy traversal, not as the primary source of truth for plans or subscriptions.
- Example adjacency discussed:
  - `1 -> [2,3,4,5,7]`
  - `2 -> [6]`
  - `AUTH -> [AUTH_TYPE_BASIC, AUTH_TYPE_OAUTH, AUTH_TYPE_SSO]`
  - `AUTH_TYPE_BASIC -> [AUTH_PROVIDER_BASIC]`
  - `AUTH_TYPE_OAUTH -> [AUTH_PROVIDER_GOOGLE, AUTH_PROVIDER_GITHUB, AUTH_PROVIDER_LINKEDIN]`
  - `AUTH_TYPE_SSO -> [AUTH_PROVIDER_OKTA]`
- RDBMS and graph config should be connected conceptually by stable `feature_key`.
- Separate feature-type config also exists:
  - `src/main/resources/configs/feature-type-config.json`
- Feature hierarchy and feature UI type must remain separate concerns:
  - `feature-graph.json` = structure
  - `feature-type-config.json` = PAGE/BUTTON/CARD/etc metadata

## Plan and Feature Entitlement Preferences

- Plans are currently three named records:
  - `BASIC`
  - `PREMIUM`
  - `ADVANCED`
- `plan_feature` stores plan-level grants and now distinguishes between:
  - `DIRECT` = automatically granted by the plan
  - `SELECTABLE` = granted as a selectable feature family/configuration area
- A feature being granted by a plan is different from an organization actually selecting concrete child nodes under that feature family.
- Example:
  - plan may grant `AUTH` as `SELECTABLE`
  - plan may grant `COMMENT_SECTION` or `DESCRIPTION` as `DIRECT`
  - actual chosen auth type/provider is stored separately

## Auth as Feature Preference

- Authentication is treated as a feature family.
- Auth choices are not just subscription metadata; they are feature selections.
- Basic plan rule:
  - user can select any one auth type
  - then any one provider under the selected auth type
- Premium and Advanced allow selecting more auth types/providers through plan-driven selection rules.
- Auth restrictions should be enforced in business logic, not hardcoded as DB constraints.
- `AUTH` is the root feature family.
- Auth types and providers are resolved through the feature graph, not explicitly whitelisted in every `plan_feature` row.

## Restriction Design Preference

- Restrictions should be modeled at the `plan_feature` level, not the raw `feature` level.
- Example reason:
  - `COMMENT_SECTION` may allow `200` comments for Basic and a different limit for Premium.
- Restriction model should be generic and extensible.
- Preferred restriction structure:
  - restriction key
  - restriction type
  - operator
  - value
  - unit
- Restriction examples discussed:
  - `MAX_COMMENTS LTE 200 COUNT`
- Usage tracking should be separate from restriction definition.

## Selection Rule Preference

- The problem is better modeled as a tree with node-level selection policy and per-plan override.
- MongoDB stores tree/adjacency only.
- PostgreSQL stores the entitlement anchor and selection rules.
- Preferred rule model:
  - direct children only
  - `min_selectable`
  - `max_selectable`
  - plan overrides node behavior
- A separate table is preferred for this rather than adding many fields to `plan_feature`.
- Current chosen shape:
  - `plan_feature` = plan-level grant with `grant_type`
  - `plan_feature_selection_rule` = selection behavior for a node in the granted subtree
  - `organization_feature_selection` = actual chosen nodes

Auth example under this model:
- `BASIC` grants `AUTH` as `SELECTABLE`
- `BASIC` grants `COMMENT_SECTION`, `SUBJECT`, and `DESCRIPTION` as `DIRECT`
- selection rule on `AUTH` allows selecting exactly one auth type from direct children
- selection rule on chosen auth type allows selecting exactly one provider from direct children
- same pattern should work for any future feature family, not just auth

## Organization Feature Selection

- `subscription` tells which plan an organization has purchased.
- It does not tell which optional allowed features the organization actually selected.
- `organization_feature_selection` was introduced to capture actual chosen features such as auth type/provider.
- This table exists to distinguish:
  - what a plan allows
  - what an organization actually enables/selects
- This remains valid even after refining `plan_feature` because subscription still represents purchase/lifecycle, not actual chosen configuration.

## Current Schema Artifacts

- DBML:
  - [subscription-model.dbml](/c:/DevCodes/IMSProject/bff/subscription-model.dbml)
- MySQL DDL:
  - [mysql-schema.sql](/c:/DevCodes/IMSProject/bff/infra/sql/mysql-schema.sql)
- PostgreSQL DDL:
  - [postgres-schema.sql](/c:/DevCodes/IMSProject/bff/infra/sql/postgres-schema.sql)

## Current Hybrid Schema Summary

- PostgreSQL stores:
  - organization
  - organization_domain
  - feature
  - plan
  - plan_feature
  - plan_feature_selection_rule
  - plan_feature_restriction
  - organization_feature_selection
  - subscription
  - coupon-related tables
- Config stores:
  - feature adjacency graph only

## Employee and Org-Owned Taxonomy Direction

- `employee` is a business/person record and should not be conflated with auth identity.
- Employee auth/account mapping is a separate future concern.
- Current employee-side schema direction includes:
  - `designation`
  - `employment_type`
  - `employment_status`
  - `employee`
- Org-owned taxonomy should remain table-driven and flexible, not Java/DB enum-driven.
- Important employee field for future app entry workflow:
  - `work_email`

## Org Discovery / AuthZ Entry Flow

- There is now a separate flow under:
  - `authZ/orgDiscovery`
- Current endpoint shape:
  - `POST /api/v1/authz/org-discovery/otp/send`
  - `POST /api/v1/authz/org-discovery/otp/verify`
- Current implementation uses:
  - state machine pattern
  - OTP provider factory
  - email OTP delivery provider
  - in-memory OTP session store
- State progression currently is:
  - `EMAIL_CAPTURED`
  - `OTP_SENT`
  - `ORGANIZATION_RESOLVED`
  - plus failure/expiry states
- Important response decision:
  - the `otp/verify` response contract is correct and should be retained
  - it vends organization details after successful OTP verification

### Important correction for future implementation

- Organization resolution should **not** ultimately be based on `organization_domain`.
- Desired long-term resolution source:
  - `employee.work_email` from `employee` table
- Reason:
  - app entry should resolve a real employee identity anchor, not only a domain-to-org mapping
- Current code still uses `organization_domain` as a temporary bridge because employee persistence/model is not yet implemented in the running codebase.
- This is a temporary implementation shortcut, not the desired final design.

### Discovery workflow preference

- User enters work email first.
- System sends 6-digit OTP.
- Frontend sends OTP back.
- Backend verifies OTP.
- Backend vends organization details to frontend.
- The page where the user enters email is a bootstrap/discovery page, not a normal feature-tree page.
- `AUTH` is the first real feature-driven page after org discovery succeeds.

## Team / Resolver / Permission / LDAP Group Direction

- These are important missing concepts and should not be collapsed into employee or designation.
- `team`
  - hierarchical business structure
  - example: `S Team -> Store -> Returns & Recommerce -> FC Team`
- `resolver_group`
  - broad operational responsibility unit
  - should be defined at industry/work-ownership level, not narrowly as only ticket routing
- `permission_group`
  - access bundle / capability grouping
- `ldap_group`
  - external directory/integration-facing group
- Important modeling stance:
  - designation must not carry permissions
  - permission groups remain separate from teams and designations

## Collaboration Preference

- Important design preferences should be captured in-repo so future sessions do not lose context.
- This file should be updated when major architecture or schema decisions change.

## PostgreSQL Enum Mapping Note

- Some schema columns are native PostgreSQL enum types, for example:
  - `subscription.status -> subscription_status`
  - `organization_feature_selection.status -> organization_feature_selection_status`
- Plain `@Enumerated(EnumType.STRING)` without a cast is not enough for runtime PostgreSQL inserts/updates against those native enum columns.
- Current preferred JPA mapping approach for these fields is:
  - keep Java enums
  - keep `@Enumerated(EnumType.STRING)`
  - keep the native Postgres column definition
  - add `@ColumnTransformer(write = "cast(? as <postgres_enum_type>)")`
- Reason:
  - this keeps real Postgres runtime compatible
  - while still allowing H2 tests to run with enum types created in test datasource init
