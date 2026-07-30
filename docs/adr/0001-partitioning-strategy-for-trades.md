Status: Accepted | Date: 2026-07-18
Context
ReconX ingestsStatus: Accepted | Date: 2026-07-18
Context
ReconX ingests ~50,000 trades/day with a 5-year retention requirement, serving 10 concurrent recon analysts running daily/monthly date-range queries. A single flat trades table would grow to tens of millions of rows over the retention period, degrading query performance, bloating indexes, and complicating data archival.
Decision
Partition the trades table using PostgreSQL declarative RANGE partitioning on trade_date, with monthly partitions and a DEFAULT catch-all partition for safety. Primary key is (id, trade_date) since Postgres requires all unique/PK indexes on a partitioned table to include the partition key.
Alternatives Considered

No partitioning (single table) — rejected; index/query performance degrades predictably at 5-year scale, and there's no clean archival boundary.
Partition by instrument_id — rejected; recon workflows are date-driven, not instrument-driven, so this wouldn't align with actual query patterns.
Hash partitioning by id — rejected; distributes rows evenly but gives no pruning benefit for the dominant date-range access pattern.

Constraints / Forces

Recon queries are overwhelmingly filtered by trade_date.
5-year retention demands a clean way to archive/drop old data without full-table deletes.
Postgres partitioning mechanics (no cross-partition unique index) constrain PK design.

Consequences
Date-range queries benefit from partition pruning; old partitions can be dropped/archived cheaply. Trade-off: PK is now composite (id, trade_date), so any FK from child tables (e.g. trade_breaks) must carry trade_date too. New monthly partitions must be created proactively (via Liquibase or a scheduled job); rows landing in DEFAULT signal a missing partition and need monitoring. Indexes must be created on the parent table to propagate to all partitions automatically. ~50,000 trades/day with a 5-year retention requirement, serving 10 concurrent recon analysts running daily/monthly date-range queries. A single flat trades table would grow to tens of millions of rows over the retention period, degrading query performance, bloating indexes, and complicating data archival.
Decision
Partition the trades table using PostgreSQL declarative RANGE partitioning on trade_date, with monthly partitions and a DEFAULT catch-all partition for safety. Primary key is (id, trade_date) since Postgres requires all unique/PK indexes on a partitioned table to include the partition key.
Alternatives Considered

No partitioning (single table) — rejected; index/query performance degrades predictably at 5-year scale, and there's no clean archival boundary.
Partition by instrument_id — rejected; recon workflows are date-driven, not instrument-driven, so this wouldn't align with actual query patterns.
Hash partitioning by id — rejected; distributes rows evenly but gives no pruning benefit for the dominant date-range access pattern.

Constraints / Forces

Recon queries are overwhelmingly filtered by trade_date.
5-year retention demands a clean way to archive/drop old data without full-table deletes.
Postgres partitioning mechanics (no cross-partition unique index) constrain PK design.

Consequences
Date-range queries benefit from partition pruning; old partitions can be dropped/archived cheaply. Trade-off: PK is now composite (id, trade_date), so any FK from child tables (e.g. trade_breaks) must carry trade_date too. New monthly partitions must be created proactively (via Liquibase or a scheduled job); rows landing in DEFAULT signal a missing partition and need monitoring. Indexes must be created on the parent table to propagate to all partitions automatically.