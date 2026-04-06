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
  - `AUTH_TYPE_OAUTH`
  - `AUTH_PROVIDER_GOOGLE`
- Feature graph/adjacency should be stored separately in MongoDB.
- The current preferred split is:
  - PostgreSQL for business truth, entitlements, restrictions, and selections
  - MongoDB for feature adjacency graph only

## MongoDB Feature Graph Preference

- MongoDB is intended for adjacency/hierarchy traversal, not as the primary source of truth for plans or subscriptions.
- Example adjacency discussed:
  - `1 -> [2,3,4,5,7]`
  - `2 -> [6]`
  - `8 -> [9]`
  - `9 -> [10]`
- RDBMS and MongoDB should be connected conceptually by `feature_id` or stable `feature_key`.

## Plan and Feature Entitlement Preferences

- Plans are currently three named records:
  - `BASIC`
  - `PREMIUM`
  - `ADVANCED`
- `plan_feature` represents what a plan permits.
- A feature being allowed by a plan is different from an organization actually selecting it.

## Auth as Feature Preference

- Authentication is treated as a feature family.
- Auth choices are not just subscription metadata; they are feature selections.
- Basic plan rule:
  - only one auth type
  - only one provider
- Higher plans may allow multiple auth types and multiple providers.
- Auth restrictions should be enforced in business logic, not hardcoded as DB constraints.

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
  - `MAX_AUTH_TYPES LTE 1 COUNT`
  - `MAX_PROVIDERS LTE 1 COUNT`
- Usage tracking should be separate from restriction definition.

## Organization Feature Selection

- `subscription` tells which plan an organization has purchased.
- It does not tell which optional allowed features the organization actually selected.
- `organization_feature_selection` was introduced to capture actual chosen features such as auth type/provider.
- This table exists to distinguish:
  - what a plan allows
  - what an organization actually enables/selects

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
  - plan_feature_restriction
  - organization_feature_selection
  - subscription
  - coupon-related tables
- MongoDB stores:
  - feature adjacency graph only

## Collaboration Preference

- Important design preferences should be captured in-repo so future sessions do not lose context.
- This file should be updated when major architecture or schema decisions change.
