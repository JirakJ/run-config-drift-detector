# Run Config Drift Detector — Implementační plán

**Verze:** 2026.2.1
**Status:** Připraveno k implementaci

---

## Fáze 1: Projekt scaffolding

- [ ] Inicializovat IntelliJ Platform Plugin projekt (Gradle Kotlin DSL)
  - [ ] `build.gradle.kts` — IntelliJ Platform 2024.3+, JVM 17
  - [ ] `settings.gradle.kts`, `gradle.properties` (verze `2026.2.1`)
  - [ ] Gradle wrapper (8.x)
  - [ ] Dependency: SnakeYAML (manifest parsing)
- [ ] `src/main/resources/META-INF/plugin.xml`
  - [ ] Plugin ID: `com.jirak.driftdetector`
  - [ ] Závislosti: `com.intellij.modules.platform`
- [ ] Balíčková struktura: `com.jirak.driftdetector.*`
  - [ ] `.model` — datové třídy
  - [ ] `.manifest` — manifest parsing
  - [ ] `.validators` — requirement validátory
  - [ ] `.health` — health check orchestrace
  - [ ] `.ui` — tool window, notifikace, dialogy
  - [ ] `.quickfix` — quick-fix actions
  - [ ] `.settings` — konfigurace
- [ ] `src/main/resources/messages/DriftDetectorBundle.properties`
- [ ] `.gitignore`
- [ ] Ověřit `./gradlew build`

## Fáze 2: Datový model

- [ ] `EnvironmentRequirement.kt`
  - [ ] `type: RequirementType` (JDK, ENV_VAR, TOOL, PORT, SERVICE)
  - [ ] `name: String`, `constraint: String?` (version range, regex)
  - [ ] `required: Boolean`, `description: String?`
- [ ] `RequirementType.kt` — enum
- [ ] `HealthCheckResult.kt`
  - [ ] `requirement: EnvironmentRequirement`
  - [ ] `status: CheckStatus` (OK, MISSING, INCOMPATIBLE, WARNING)
  - [ ] `actualValue: String?`, `message: String`
  - [ ] `quickFixId: String?`
- [ ] `CheckStatus.kt` — enum s ikonami a barvami
- [ ] `EnvironmentManifest.kt` — kontejner pro celý manifest
- [ ] Unit testy

## Fáze 3: Manifest Parser

- [ ] `ManifestParser.kt`
  - [ ] `fun parse(file: VirtualFile): EnvironmentManifest`
  - [ ] Parsování `.run-requirements.yaml` (SnakeYAML)
  - [ ] Sekce: `jdk`, `env_vars`, `tools`, `ports`, `services`
  - [ ] Validace schématu (unknown keys → warning)
- [ ] `ManifestLocator.kt`
  - [ ] Hledání `.run-requirements.yaml` v projekt root
  - [ ] File watcher pro změny
- [ ] YAML schema pro validaci a autocomplete
- [ ] Unit testy (valid, invalid, partial manifesty)

## Fáze 4: Requirement Validators

- [ ] `RequirementValidator.kt` — interface
  - [ ] `fun validate(requirement: EnvironmentRequirement): HealthCheckResult`
- [ ] `JdkValidator.kt`
  - [ ] Detekce nainstalovaného JDK (ProjectJdkTable, JdkUtil)
  - [ ] Porovnání verze s constraint (version range parsing)
  - [ ] Vendor check (optional)
- [ ] `EnvVarValidator.kt`
  - [ ] `System.getenv(name)` — existuje/neexistuje
  - [ ] Regex validace hodnoty (pokud constraint definován)
- [ ] `ToolValidator.kt`
  - [ ] Detekce nástrojů v PATH: `which docker`, `which node`, etc.
  - [ ] Version check: `docker --version`, `node --version` → parse + compare
  - [ ] Asynchronní (process execution)
- [ ] `PortValidator.kt`
  - [ ] Check zda port je volný: `ServerSocket.bind()` test
  - [ ] Pokud obsazený: identifikace PID (platform-specific: `lsof`, `netstat`)
- [ ] Unit testy pro každý validátor

## Fáze 5: Health Check Service

- [ ] `HealthCheckService.kt` — project-level service
  - [ ] `fun runHealthCheck(): List<HealthCheckResult>`
  - [ ] Orchestrace: načíst manifest → spustit validátory → seřadit výsledky
  - [ ] Asynchronní provádění (coroutines / `Task.Backgroundable`)
  - [ ] Events: `HealthCheckCompletedEvent` pro UI update
- [ ] Registrace jako `ProjectService` v `plugin.xml`
- [ ] `HealthCheckStartupActivity.kt` implements `ProjectActivity`
  - [ ] Automatický health check při otevření projektu (pokud manifest existuje)
  - [ ] Notification: „Environment issues detected" → klik otevře tool window
- [ ] Integration testy

## Fáze 6: Health Check Tool Window

- [ ] `HealthCheckToolWindow.kt` implements `ToolWindowFactory`
  - [ ] Registrace v `plugin.xml`
  - [ ] Checklist UI:
    - [ ] ✅ JDK 17 — Temurin 17.0.9 installed
    - [ ] ❌ DATABASE_URL — not set
    - [ ] ⚠️ Docker 23.0 — minimum 24+ recommended
    - [ ] ❌ Port 5432 — in use (PID 12345)
  - [ ] Ikony dle status (✅ zelená, ❌ červená, ⚠️ žlutá)
  - [ ] Quick-fix tlačítko vedle každého problému
  - [ ] „Re-check" toolbar action
  - [ ] „Create manifest" action (pokud manifest neexistuje)
  - [ ] Progress bar při health checku
- [ ] Klik na quick-fix → spustí příslušný fixer

## Fáze 7: Quick Fixes

- [ ] `QuickFixAction.kt` — interface
  - [ ] `fun getId(): String`
  - [ ] `fun getLabel(): String`
  - [ ] `fun execute(requirement: EnvironmentRequirement, project: Project)`
- [ ] `SetEnvVarQuickFix.kt`
  - [ ] Dialog: „Set environment variable NAME"
  - [ ] Nastavení do Run Configuration env vars
  - [ ] Nabídka: „Add to .env file" (pokud EnvFile plugin přítomen)
- [ ] `DownloadJdkQuickFix.kt`
  - [ ] Otevře IntelliJ JDK Manager (ProjectStructure → Platform Settings → SDKs)
- [ ] `KillPortQuickFix.kt`
  - [ ] Identifikace procesu na portu
  - [ ] Potvrzovací dialog: „Kill process PID (processName) on port PORT?"
  - [ ] Provedení `kill PID`
- [ ] `InstallToolQuickFix.kt`
  - [ ] Otevře terminál s příkazem: `brew install X` / `apt install X`
  - [ ] Nebo: odkaz na download stránku

## Fáze 8: Manifest Generator

- [ ] `ManifestGenerator.kt`
  - [ ] Analýza existujících run configurations → extrakce požadavků
  - [ ] Detekce JDK z project SDK
  - [ ] Detekce env vars z run configs a .env souborů
  - [ ] Detekce nástrojů z Dockerfile, docker-compose.yml
  - [ ] Output: `.run-requirements.yaml`
- [ ] „Generate Manifest" action v context menu (projekt root)
- [ ] Dialog: preview generovaného manifestu, checkbox pro každý požadavek

## Fáze 9: Konfigurace

- [ ] `DriftDetectorSettings.kt` implements `PersistentStateComponent`
  - [ ] Auto-check on startup (on/off)
  - [ ] Manifest path (custom, default: `.run-requirements.yaml`)
  - [ ] Quick-fix preferences
- [ ] `DriftDetectorConfigurable.kt` — Settings → Tools → Run Config Drift Detector
- [ ] Unit testy

## Fáze 10: Polish & Release

- [ ] Ukázkový `.run-requirements.yaml` v docs/
- [ ] README.md
- [ ] `./gradlew verifyPlugin`, `./gradlew buildPlugin`
- [ ] Manuální testování (JDK, env vars, Docker, porty)
- [ ] Cross-IDE, cross-OS test (macOS, Linux)
- [ ] Marketplace listing
- [ ] Git tag `v2026.2.1`

---

## Budoucí fáze (Paid)

- [ ] Neomezené požadavky (free: max 10)
- [ ] Auto-generate manifest
- [ ] Onboarding mode (step-by-step průvodce, progress tracker)
- [ ] Team sync (anonymizovaný environment stav)
- [ ] .env.example auto-sync z manifestu
- [ ] Run config diff (VCS vs. local)
- [ ] README.md parsing (setup sekcí)
