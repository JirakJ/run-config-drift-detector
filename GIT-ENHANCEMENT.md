# Run Config Drift Detector — Git Enhancement Specifikace

**Verze:** 2026.2.1
**Status:** Plánováno
**Závislosti:** Git4Idea API, `.idea/` directory tracking

---

## Přehled

Rozšíření pluginu o Git integraci pro verzování run konfigurací, sdílení mezi týmem
přes Git a automatické přepínání konfigurací podle aktuální větve.

---

## FR-GE-001: Config Versioning

### Popis

Automatický commit run konfigurací do `.idea/runConfigurations/` s trasovatelnou historií změn.
Každá změna run configu je verzována v Gitu s popisným commit message.

### Funkční požadavky

- [ ] **FR-GE-001.1** — Detekce změn v `.idea/runConfigurations/*.xml` přes VFS listener
- [ ] **FR-GE-001.2** — Automatický `git add` + `git commit` při uložení run configu
- [ ] **FR-GE-001.3** — Generování deskriptivního commit message: `chore(run-config): update {configName}`
- [ ] **FR-GE-001.4** — Konfigurovatelné: auto-commit vs. manuální (prompt before commit)
- [ ] **FR-GE-001.5** — Historie změn: zobrazení `git log` pro konkrétní run config soubor
- [ ] **FR-GE-001.6** — Rollback: obnovení předchozí verze konfigurace z Git historie

### Acceptance Criteria

1. Změna run configu vytvoří commit s jasným message identifikujícím konfiguraci
2. Auto-commit lze vypnout v settings (default: prompt)
3. Historie změn je dostupná přes context menu na run configu
4. Rollback obnoví přesný stav XML souboru z vybraného commitu
5. Plugin neprovádí commit pokud jsou jiné unstaged changes v `.idea/`

### Datový model — rozšíření

```kotlin
data class ConfigVersion(
    val commitHash: String,
    val commitDate: Instant,
    val author: String,
    val message: String,
    val configName: String,
    val configFile: String,
    val changeType: ConfigChangeType // CREATED, MODIFIED, DELETED
)

enum class ConfigChangeType {
    CREATED,
    MODIFIED,
    DELETED
}

data class ConfigVersionHistory(
    val configName: String,
    val versions: List<ConfigVersion>,
    val currentVersion: ConfigVersion?
)
```

### Technická implementace

- `ConfigChangeWatcher.kt` — VFS listener na `.idea/runConfigurations/`
- `ConfigAutoCommitter.kt` — automatický `git add` + `git commit` přes Git4Idea API
- `ConfigHistoryService.kt` — `git log -- .idea/runConfigurations/{file}` wrapper
- `ConfigRollbackAction.kt` — `git checkout {hash} -- {file}` pro rollback

---

## FR-GE-002: Team Config Sync via Git

### Popis

Sdílení run konfigurací mezi členy týmu přes `.idea/` directory trackovaný v Gitu.
Plugin zajistí správný `.gitignore` setup a řeší konflikty při merge.

### Funkční požadavky

- [ ] **FR-GE-002.1** — Analýza `.gitignore`: detekce zda `.idea/runConfigurations/` je tracked
- [ ] **FR-GE-002.2** — Quick-fix: přidání/odebrání `.idea/runConfigurations/` z `.gitignore`
- [ ] **FR-GE-002.3** — Merge conflict resolver pro run config XML soubory
- [ ] **FR-GE-002.4** — Notifikace při `git pull`: „Nové/změněné run konfigurace dostupné"
- [ ] **FR-GE-002.5** — Selektivní sharing: označení konfigurací jako „shared" vs. „personal"
- [ ] **FR-GE-002.6** — Diff viewer pro porovnání lokální vs. remote konfigurace

### Acceptance Criteria

1. Plugin správně detekuje stav `.gitignore` pro run config directory
2. Quick-fix upraví `.gitignore` bez poškození ostatních pravidel
3. Merge conflict resolver zobrazí side-by-side diff XML konfigurací
4. Po `git pull` se zobrazí balloon s počtem nových/změněných konfigurací
5. Personal konfigurace nejsou nikdy commitovány (`.gitignore` pattern)
6. Diff viewer zobrazuje čitelný diff (ne raw XML) s highlighted změnami

### Datový model — rozšíření

```kotlin
data class SharedConfigStatus(
    val configName: String,
    val sharingMode: SharingMode,     // SHARED, PERSONAL
    val syncStatus: SyncStatus,        // IN_SYNC, LOCAL_CHANGES, REMOTE_CHANGES, CONFLICT
    val localVersion: ConfigVersion?,
    val remoteVersion: ConfigVersion?
)

enum class SharingMode {
    SHARED,     // tracked v Gitu, sdílená s týmem
    PERSONAL    // v .gitignore, pouze lokální
}

enum class SyncStatus {
    IN_SYNC,
    LOCAL_CHANGES,
    REMOTE_CHANGES,
    CONFLICT
}
```

### Technická implementace

- `GitIgnoreAnalyzer.kt` — parsování `.gitignore`, detekce run config tracking
- `ConfigSyncService.kt` — monitoring `git pull` events, diff detection
- `ConfigMergeResolver.kt` — custom merge handler pro run config XML
- `ConfigSharingAction.kt` — toggle shared/personal s `.gitignore` update

---

## FR-GE-003: Branch-Specific Configs

### Popis

Různé run konfigurace per Git branch s automatickým přepínáním při `git checkout`.
Umožňuje mít jiné prostředí pro feature branch vs. main vs. release.

### Funkční požadavky

- [ ] **FR-GE-003.1** — Mapování branch pattern → config set (glob: `feature/*`, `release/*`)
- [ ] **FR-GE-003.2** — Auto-switch: při `git checkout` aktivace příslušných konfigurací
- [ ] **FR-GE-003.3** — Config overlay: branch-specific overrides na base konfigurace
- [ ] **FR-GE-003.4** — UI: indikátor aktuální branch-config vazby ve status baru
- [ ] **FR-GE-003.5** — Snapshot: uložení aktuální config sady jako branch preset
- [ ] **FR-GE-003.6** — Fallback: pokud branch nemá mapping, použije se default sada

### Acceptance Criteria

1. Při `git checkout feature/xyz` se aktivují konfigurace mapované na `feature/*`
2. Auto-switch proběhne do 2 sekund po checkout
3. Overlay systém: branch config přepisuje jen definované hodnoty, zbytek dědí z base
4. Status bar widget zobrazuje aktuální branch → config mapping
5. Snapshot uloží kompletní stav run konfigurací asociovaný s branch patternem
6. Bez mapování se použije default sada (žádná ztráta konfigurací)

### Datový model — rozšíření

```kotlin
data class BranchConfigMapping(
    val branchPattern: String,       // glob: "feature/*", "release/*", "main"
    val configSetName: String,
    val configs: List<BranchConfigOverride>,
    val isDefault: Boolean = false
)

data class BranchConfigOverride(
    val configName: String,
    val overrides: Map<String, String>,  // XML xpath → value overrides
    val enabled: Boolean = true
)

data class BranchConfigSnapshot(
    val branchPattern: String,
    val createdAt: Instant,
    val createdBy: String,
    val configFiles: Map<String, String>  // filename → XML content
)
```

### Technická implementace

- `BranchChangeListener.kt` — Git4Idea `GitRepositoryChangeListener` pro checkout event
- `BranchConfigResolver.kt` — pattern matching branch name → config set
- `ConfigOverlayEngine.kt` — XML merge: base config + branch overrides
- `BranchConfigStatusBarWidget.kt` — status bar zobrazení aktuálního mappingu
- Uložení v `.idea/branchConfigs/` nebo `.run-config-branches.yaml`

---

## Společné technické požadavky

### Performance

- Branch change detection: event-driven (ne polling), latence <100ms
- Config file I/O na background thread
- XML parsing cache s invalidací při file change

### Konfigurace — rozšíření `DriftDetectorSettings`

```kotlin
// Nová pole v existujícím Settings
val configVersioningEnabled: Boolean = false
val autoCommitMode: AutoCommitMode = AutoCommitMode.PROMPT  // AUTO, PROMPT, OFF
val commitMessageTemplate: String = "chore(run-config): update {configName}"
val teamSyncEnabled: Boolean = true
val branchConfigEnabled: Boolean = false
val branchMappings: List<BranchConfigMapping> = emptyList()
val defaultConfigSet: String = "default"
```

### Freemium rozdělení

| Feature | Free | Paid |
|---------|------|------|
| Config versioning (basic history) | ✅ | ✅ |
| Auto-commit | ❌ | ✅ |
| Team config sync | ❌ | ✅ |
| Branch-specific configs | ❌ | ✅ |
| Merge conflict resolver | ❌ | ✅ |
| Config rollback | ❌ | ✅ |
