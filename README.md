# Start server
```shell
$ docker compose -f docker-compose.prod.yml up -d
```

# Seeding
```shell
$ ./gradlew seed
```
or
```shell
$  docker exec -it freight-backend java -cp /app/freight-server.jar  io.github.nitkc_proken.freight.database.SeedKt 
```
# Apply migration
```shell
$ ./gradlew applyMigration
```
or
```shell
$  docker exec -it freight-backend java -cp /app/freight-server.jar  io.github.nitkc_proken.freight.database.MigrationKt 
```

# Generate migration script
```shell
$ ./gradlew generateMigrationScript
```

or

```shell
$  docker exec -it freight-backend java -cp /app/freight-server.jar  io.github.nitkc_proken.freight.database.GenerateMigrationScriptKt 
```