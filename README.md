# kixi.paloma.enrich

## Installation
There are number of required JDBC connection strings required before any enrichment is performed.

```
export LLPG_DATABASE_URL=jdbc:mssql://
export NNDR_DATABASE_URL=jdbc:mssql://
export CIVICA_DATABASE_URL=jdbc:mssql://
```

Outgoing writes are sent to a Postgres database.

```
export KPE_DATABASE_URL=jdbc:postgresql://
```


Copyright © 2018 Mastodon C Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
