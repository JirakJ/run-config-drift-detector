# Run Config Drift Detector — Implementační plán

**Verze:** 2026.2.1
**Status:** Připraveno k implementaci

---

## Fáze 0: Plugin Ikona & Branding

- [ ] Navrhnout plugin ikonu (SVG + PNG)
  - [ ] Koncept: ⚙️ Ozubené kolo s drift indikátorem
  - [ ] Hlavní ikona: 40×40 SVG pro Marketplace listing
  - [ ] Toolbar ikona: 16×16 + 13×13 SVG (light + dark varianta)
  - [ ] Tool window ikona: 13×13 SVG
  - [ ] `src/main/resources/META-INF/pluginIcon.svg` (40×40, light theme)
  - [ ] `src/main/resources/META-INF/pluginIcon_dark.svg` (40×40, dark theme)
  - [ ] Action ikony pro toolbar/menu (16×16 SVG, light + dark)
- [ ] Barevná paleta konzistentní s DX brandem
- [ ] Marketplace banner obrázek (1280×800)
- [ ] Screenshoty pro Marketplace listing (min 3, 1280×800 PNG)

## Fáze 1: Projekt scaffolding

- [ ] Inicializovat IntelliJ Platform Plugin projekt (Gradle Kotlin DSL)
  - [ ] `build.gradle.kts` — IntelliJ Platform 2024.3+, JVM 17
  - [ ] `settings.gradle.kts`, `gradle.properties` (verze `2026.2.1`)
  - [ ] Gradle wrapper (8.x)
  - [ ] Dependency: SnakeYAML (manifest parsing)
- [ ] `src/main/resources/META-INF/plugin.xml`
  - [ ] Plugin ID: `com.jakubjirak.driftdetector`
  - [ ] Závislosti: `com.intellij.modules.platform`
- [ ] Balíčková struktura: `com.jakubjirak.driftdetector.*`
  - [ ] `.model` — datové třídy
  - [ ] `.manifest` — manifest parsing
  - [ ] `.validators` — requirement validátory
  - [ ] `.health` — health check orchestrace
  - [ ] `.ui` — tool window, notifikace, dialogy
  - [ ] `.quickfix` — quick-fix actions
  - [ ] `.settings` — konfigurace
- [ ] `src/main/resources/messages/DriftDetectorBundle.properties`
- [ ] `.gitignore` (build/, .idea/, .gradle/, .kotlin/, out/)
- [ ] `gradle/libs.versions.toml` — version catalog
- [ ] `LICENSE` (Apache-2.0)
- [ ] `.editorconfig` (Kotlin style conventions)
- [ ] Detekt konfigurace (`detekt.yml`)
- [ ] GitHub Actions CI workflow (`.github/workflows/build.yml`)
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

## Fáze 10: Freemium & Licensing Infrastructure

- [ ] Integrace JetBrains Marketplace Licensing API
  - [ ] `LicenseChecker.kt` — ověření licence přes `com.intellij.ide.plugins.PluginManager` a Marketplace API
  - [ ] `LicenseStatus.kt` — enum: FREE, TRIAL, PAID, EXPIRED
  - [ ] `FreemiumGate.kt` — centrální guard pro paid features
    - [ ] `fun isPaidFeatureAvailable(featureId: String): Boolean`
    - [ ] Graceful degradation — paid UI elementy skryté/disabled ve free tieru
  - [ ] Trial period: 30 dní plný přístup
- [ ] `plugin.xml` — `<product-descriptor>` element pro Marketplace licensing
  - [ ] `code` atribut (unique product code)
  - [ ] `release-date`, `release-version`
- [ ] Nag screen / upgrade prompt
  - [ ] Non-intrusive balloon notification při pokusu o paid feature
  - [ ] „Upgrade to Pro" action → Marketplace stránka
  - [ ] Max 1× denně (ne spam)
- [ ] Feature flags
  - [ ] `FeatureRegistry.kt` — definice free vs. paid features
  - [ ] Každý paid feature má `@PaidFeature("feature.id")` anotaci nebo guard check
- [ ] Unit testy pro licensing logic (mock Marketplace API)

## Fáze 11: Production-Grade Quality

- [ ] Error Handling & Resilience
  - [ ] Globální exception handler (`ErrorReportSubmitter` implementace)
  - [ ] Graceful degradation — plugin nesmí crashnout IDE
  - [ ] Try-catch na všech EP entry pointech
  - [ ] Timeout na všech I/O operacích (network, git, file system)
  - [ ] Rate limiting na API volání
- [ ] Logging
  - [ ] `Logger` instance v každé třídě (IntelliJ `Logger.getInstance()`)
  - [ ] Structured logging: DEBUG pro vývoj, INFO pro operace, WARN/ERROR pro problémy
  - [ ] Žádné citlivé údaje v logech
- [ ] Telemetrie (opt-in, anonymní)
  - [ ] `PluginUsageTracker.kt` — FUS (Feature Usage Statistics) integrace
  - [ ] Metriky: aktivace featury, error rate, latence
  - [ ] Registrace FUS group v `plugin.xml`
- [ ] Performance
  - [ ] Profiling na velkých projektech (10k+ souborů)
  - [ ] Background thread pro všechny I/O operace (nesmí blokovat EDT)
  - [ ] Read/write action správně (ReadAction, WriteAction, smart mode check)
  - [ ] Disposal pattern: `Disposable` pro všechny listeners a services
  - [ ] Memory leak check (WeakReference kde potřeba)
- [ ] Testing — production úroveň
  - [ ] Unit testy: ≥80 % coverage core logiky
  - [ ] Integrační testy: `BasePlatformTestCase` pro PSI, inspections, EP
  - [ ] UI testy: `HeavyPlatformTestCase` pro tool windows, dialogy
  - [ ] Performance testy: benchmark na velkých souborech
- [ ] CI/CD Pipeline
  - [ ] GitHub Actions workflow:
    - [ ] `./gradlew check` — kompilace + testy
    - [ ] `./gradlew verifyPlugin` — plugin strukturální validace
    - [ ] `./gradlew verifyPluginProjectConfiguration` — metadata validace
    - [ ] `./gradlew runPluginVerifier` — binární kompatibilita s target IDE verzemi
    - [ ] Qodana scan (JetBrains code quality)
  - [ ] Release workflow:
    - [ ] Tag-based release (push tag → build → publish)
    - [ ] `./gradlew publishPlugin` — automatický upload na Marketplace
    - [ ] Changelog generace z git commitů
  - [ ] Dependabot / Renovate pro dependency updates
- [ ] Kódová kvalita
  - [ ] Kotlin coding conventions (official JetBrains style)
  - [ ] Detekt nebo ktlint pro statickou analýzu
  - [ ] Žádné deprecated API (IntelliJ Platform compatibility)
  - [ ] `@ApiStatus.Experimental` / `@ApiStatus.Internal` anotace
- [ ] Dokumentace
  - [ ] KDoc pro všechny public API
  - [ ] README.md: instalace, konfigurace, screenshoty, FAQ
  - [ ] CHANGELOG.md (Keep a Changelog format)
  - [ ] CONTRIBUTING.md (pokud open source)

## Fáze 12: Polish & Release

- [ ] Ukázkový `.run-requirements.yaml` v docs/
- [ ] README.md
- [ ] `./gradlew verifyPlugin`, `./gradlew buildPlugin`
- [ ] Manuální testování (JDK, env vars, Docker, porty)
- [ ] Cross-IDE, cross-OS test (macOS, Linux)
- [ ] Marketplace listing
- [ ] Git tag `v2026.2.1`

## Fáze 13: Git Enhancement — Config Versioning

- [ ] `ConfigChangeWatcher.kt` — VFS listener na `.idea/runConfigurations/`
- [ ] `ConfigAutoCommitter.kt` — automatický `git add` + `git commit` přes Git4Idea API
- [ ] `ConfigHistoryService.kt` — `git log -- .idea/runConfigurations/{file}` wrapper
- [ ] `ConfigRollbackAction.kt` — `git checkout {hash} -- {file}` pro rollback
- [ ] `ConfigVersion.kt`, `ConfigChangeType.kt`, `ConfigVersionHistory.kt` — datový model
- [ ] Konfigurovatelný commit message template: `chore(run-config): update {configName}`
- [ ] Auto-commit vs. prompt mód (výchozí: prompt)
- [ ] Unit testy pro change detection a history parsing

## Fáze 14: Git Enhancement — Team Config Sync via Git

- [ ] `GitIgnoreAnalyzer.kt` — parsování `.gitignore`, detekce run config tracking
- [ ] `ConfigSyncService.kt` — monitoring `git pull` events, diff detection
- [ ] `ConfigMergeResolver.kt` — custom merge handler pro run config XML
- [ ] `ConfigSharingAction.kt` — toggle shared/personal s `.gitignore` update
- [ ] `SharedConfigStatus.kt`, `SharingMode.kt`, `SyncStatus.kt` — datový model
- [ ] Notifikace při `git pull` o nových/změněných konfiguracích
- [ ] Diff viewer pro porovnání lokální vs. remote konfigurace
- [ ] Unit testy pro gitignore analýzu a sync detection

## Fáze 15: Git Enhancement — Branch-Specific Configs

- [ ] `BranchChangeListener.kt` — Git4Idea `GitRepositoryChangeListener` pro checkout event
- [ ] `BranchConfigResolver.kt` — pattern matching branch name → config set
- [ ] `ConfigOverlayEngine.kt` — XML merge: base config + branch overrides
- [ ] `BranchConfigStatusBarWidget.kt` — status bar zobrazení aktuálního mappingu
- [ ] `BranchConfigMapping.kt`, `BranchConfigOverride.kt`, `BranchConfigSnapshot.kt` — datový model
- [ ] Auto-switch konfigurace při `git checkout` (<2s latence)
- [ ] Snapshot: uložení aktuální config sady jako branch preset
- [ ] Fallback: default sada pokud branch nemá mapping
- [ ] Unit testy pro pattern matching a overlay engine

---

## Budoucí fáze (Paid)

- [ ] Neomezené požadavky (free: max 10)
- [ ] Auto-generate manifest
- [ ] Onboarding mode (step-by-step průvodce, progress tracker)
- [ ] Team sync (anonymizovaný environment stav)
- [ ] .env.example auto-sync z manifestu
- [ ] Run config diff (VCS vs. local)
- [ ] README.md parsing (setup sekcí)
