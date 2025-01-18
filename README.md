# Start server
```shell
$ docker compose up
```

# Apply migration
```shell
$ ./gradlew applyMigration
```
or
```shell
$  docker exec -it freight-backend java -cp /app/freight-server.jar  io.github.nitkc_proken.freight.backend.MigrationKt 
```

# Generate migration script
```shell
$ ./gradlew generateMigrationScript
```

or

```shell
$  docker exec -it freight-backend java -cp /app/freight-server.jar  io.github.nitkc_proken.freight.backend.GenerateMigrationScriptKt 
```