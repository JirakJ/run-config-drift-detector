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
- Balíčky: `com.jakubjirak.driftdetector.*`
- Manifest soubor: `.run-requirements.yaml` v repo root
- Status: OK, MISSING, INCOMPATIBLE, WARNING
- Validace na startup (ProjectActivity) — non-blocking
- Texty v `messages/DriftDetectorBundle.properties`
- Verzování: `2026.2.X`

## Production-grade požadavky
- Všechny I/O operace na background threadech (`Task.Backgroundable`, coroutines)
- Read/Write actions správně (`ReadAction.run {}`, `WriteAction.run {}`)
- `Disposable` pattern pro listeners a services
- Exception handling: žádné unhandled exceptions, graceful degradation
- Logging: `Logger.getInstance(MyClass::class.java)` v každé třídě
- EDT nesmí být blokován — žádné síťové/git/file operace na EDT
- Smart mode check (`DumbService.isDumb()`) kde potřeba

## Freemium architektura
- `FreemiumGate` centrální guard: `FreemiumGate.isPaidFeatureAvailable("feature.id")`
- JetBrains Marketplace Licensing API přes `<product-descriptor>` v `plugin.xml`
- Paid UI elementy: skryté nebo disabled ve free tieru (ne crash/error)
- Trial: 30 dní plný přístup, pak downgrade na free tier
- Upgrade prompt: max 1× denně, non-intrusive balloon

## CI/CD
- GitHub Actions: `./gradlew check`, `verifyPlugin`, `runPluginVerifier`
- Detekt pro statickou analýzu Kotlin kódu
- Tag-based release → `publishPlugin` na Marketplace
- Kotlin coding conventions (JetBrains official style)

## Ikona
- `src/main/resources/META-INF/pluginIcon.svg` (40×40, light)
- `src/main/resources/META-INF/pluginIcon_dark.svg` (40×40, dark)
- Action ikony: 16×16 + 13×13 SVG (light + dark varianty)

## Freemium hranice
- Free: manifest + health check, max 10 požadavků, basic quick-fixes
- Paid: neomezeno, auto-generate, onboarding mode, team sync
