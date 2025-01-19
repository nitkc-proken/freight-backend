# Start server
```shell
$ docker compose -f docker-compose.prod.yml up -d
```

http://localhost:8080/api/swagger

# Seeding (テストデータの注入)
```shell
$  docker exec -it freight-backend java -cp /app/freight-server.jar  io.github.nitkc_proken.freight.database.SeedKt 
```
or
```shell
$ ./gradlew seed
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