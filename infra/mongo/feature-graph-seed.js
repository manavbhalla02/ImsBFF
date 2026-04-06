// IMS feature graph seed
// Assumption:
// - Mongo connection is already established
// - Current database is already selected
// - Collection name: feature_graph
//
// This stores the graph by feature_key instead of feature_id
// so the Mongo seed remains stable across environments.

db.feature_graph.updateOne(
  { graphKey: "IMS_FEATURE_GRAPH_V1" },
  {
    $set: {
      graphKey: "IMS_FEATURE_GRAPH_V1",
      graphName: "IMS Feature Graph",
      version: 1,
      rootFeatures: ["TKT_PAGE", "AUTH"],
      adjacency: {
        TKT_PAGE: [
          "COMMENT_SECTION",
          "SUBJECT",
          "DESCRIPTION",
          "AUTO_TKT_RESOLUTION",
          "AI_SUBJECT_SUMMARY"
        ],
        COMMENT_SECTION: [
          "COMMENT_RES"
        ],
        AUTH: [
          "AUTH_TYPE_BASIC",
          "AUTH_TYPE_OAUTH",
          "AUTH_TYPE_SSO"
        ],
        AUTH_TYPE_BASIC: [
          "AUTH_PROVIDER_BASIC"
        ],
        AUTH_TYPE_OAUTH: [
          "AUTH_PROVIDER_GOOGLE",
          "AUTH_PROVIDER_GITHUB",
          "AUTH_PROVIDER_LINKEDIN"
        ],
        AUTH_TYPE_SSO: [
          "AUTH_PROVIDER_OKTA"
        ]
      },
      metadata: {
        storageModel: "Hybrid",
        relationalCatalog: "PostgreSQL feature table",
        graphStore: "MongoDB adjacency graph"
      },
      updatedAt: new Date()
    }
  },
  { upsert: true }
);
