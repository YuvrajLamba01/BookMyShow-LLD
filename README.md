# BookMyShow LLD (Java)

Production-style LLD implementation with clear layers.

## Packages
- `src/bookmyshow/model`: domain entities and enums
- `src/bookmyshow/repository`: repository interfaces
- `src/bookmyshow/repository/inmemory`: in-memory repository implementations
- `src/bookmyshow/payment`: payment gateway abstraction and mock gateway
- `src/bookmyshow/service`: business logic services
- `src/bookmyshow/app`: application entrypoint

## Main Flow
- Search show
- Lock seats
- Confirm booking with payment
- Cancel booking with refund

## Class Diagram
- `docs/class-diagram.md`

## Run
```bash
$files = Get-ChildItem -Path src/bookmyshow -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
java -cp out bookmyshow.app.Main
```
