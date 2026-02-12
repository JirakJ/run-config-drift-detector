# Copilot Instructions — Run Config Drift Detector

## Projekt
JetBrains IDE plugin — validuje environment požadavky projektu (JDK, env vars, nástroje, porty) a zobrazuje health report.

## Technický stack
- **Jazyk:** Kotlin (JVM 17+)
- **Build:** Gradle (Kotlin DSL) s `org.jetbrains.intellij.platform` plugin 2.x
- **Platform:** IntelliJ Platform SDK 2024.3+
- **Testování:** JUnit 5 + `BasePlatformTestCase`

## Build a testy
```bash
./gradlew build
./gradlew test
./gradlew test --tests "*.HealthCheckServiceTest.testMissingEnvVar"
./gradlew runIde
./gradlew verifyPlugin
```

## Architektura
- **Manifest:** `ManifestParser` — parsuje `.run-requirements.yaml` (SnakeYAML)
- **Validators:** `JdkValidator`, `EnvVarValidator`, `ToolValidator`, `PortValidator` — implementují `RequirementValidator`
- **Health check:** `HealthCheckService` — orchestruje validaci, produkuje `HealthCheckResult` list
- **UI:** `HealthCheckToolWindow` (tool window s checklist), `HealthCheckNotification` (startup notifikace)
- **Quick-fixes:** `SetEnvVarQuickFix`, `DownloadJdkQuickFix`, `KillPortQuickFix`
- **Auto-generate:** `ManifestGenerator` — extrahuje požadavky z run configurations (Paid)

## Konvence
- Balíčky: `com.jirak.driftdetector.*`
- Manifest soubor: `.run-requirements.yaml` v repo root
- Status: OK, MISSING, INCOMPATIBLE, WARNING
- Validace na startup (ProjectActivity) — non-blocking
- Texty v `messages/DriftDetectorBundle.properties`
- Verzování: `2026.2.X`

## Freemium hranice
- Free: manifest + health check, max 10 požadavků, basic quick-fixes
- Paid: neomezeno, auto-generate, onboarding mode, team sync
