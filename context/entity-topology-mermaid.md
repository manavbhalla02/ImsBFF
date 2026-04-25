# Entity Topology Mermaid

## Permission Topology

```mermaid
flowchart LR

  %% Column 1: roots
  organization["organization"]
  employee["employee"]
  feature["feature"]

  %% Column 2: permission domain entities
  permission_group["permission_group"]
  feature_permission["feature_permission"]

  %% Column 3: terminal mappings
  employee_permission_group["employee_permission_group"]
  permission_group_feature_permission["permission_group_feature_permission"]

  %% Invisible ordering links to keep visual gaps between columns
  organization --- permission_group
  permission_group --- employee_permission_group
  feature --- feature_permission
  feature_permission --- permission_group_feature_permission

  %% Actual dependencies
  organization --> permission_group
  permission_group --> permission_group

  employee --> employee_permission_group
  permission_group --> employee_permission_group

  feature --> feature_permission
  permission_group --> permission_group_feature_permission
  feature_permission --> permission_group_feature_permission
```

Topological order:

1. `permission_group`
2. `feature_permission`
3. `employee_permission_group`
4. `permission_group_feature_permission`

External anchors:

- `organization -> permission_group`
- `employee -> employee_permission_group`
- `feature -> feature_permission`

## LDAP Topology

```mermaid
flowchart LR

  %% Column 1: roots
  ldap_directory["ldap_directory"]
  employee["employee"]
  permission_group["permission_group"]
  team["team"]
  resolver_group["resolver_group"]

  %% Column 2: first-level LDAP entities
  ldap_group["ldap_group"]
  ldap_user["ldap_user"]

  %% Column 3: terminal LDAP mappings
  employee_ldap_identity["employee_ldap_identity"]
  ldap_user_group["ldap_user_group"]
  ldap_group_permission_group["ldap_group_permission_group"]
  ldap_group_team["ldap_group_team"]
  ldap_group_resolver_group["ldap_group_resolver_group"]

  %% Invisible ordering links to keep visual gaps between columns
  ldap_directory --- ldap_group
  ldap_group --- ldap_user_group
  ldap_user --- employee_ldap_identity

  %% Actual dependencies
  ldap_directory --> ldap_group
  ldap_group --> ldap_group
  ldap_directory --> ldap_user

  employee --> employee_ldap_identity
  ldap_user --> employee_ldap_identity

  ldap_user --> ldap_user_group
  ldap_group --> ldap_user_group

  ldap_group --> ldap_group_permission_group
  permission_group --> ldap_group_permission_group

  ldap_group --> ldap_group_team
  team --> ldap_group_team

  ldap_group --> ldap_group_resolver_group
  resolver_group --> ldap_group_resolver_group
```

Topological order:

1. `ldap_directory`
2. `ldap_group`
3. `ldap_user`
4. `employee_ldap_identity`
5. `ldap_user_group`
6. `ldap_group_permission_group`
7. `ldap_group_team`
8. `ldap_group_resolver_group`

External anchors:

- `employee -> employee_ldap_identity`
- `permission_group -> ldap_group_permission_group`
- `team -> ldap_group_team`
- `resolver_group -> ldap_group_resolver_group`

Note:

- `ldap_group` has a self-reference through `parent_ldap_group_id`, so root groups load before child groups.
