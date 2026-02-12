# Run Config Drift Detector — Software Requirements Specification (SRS)

**Verze:** 2026.2.1
**Autor:** Jakub Jirák
**Datum:** 12. února 2026
**Model:** Freemium JetBrains Plugin

---

## 1. Úvod

### 1.1 Účel
Run Config Drift Detector porovnává „spustitelnost" projektu mezi stroji a členy týmu — detekuje chybějící env vars, JDK mismatch, working dir drift — a zobrazuje health report s actionable fixy.

### 1.2 Rozsah
Plugin validuje run configurations oproti deklarovaným požadavkům (manifest soubor) a reportuje nesrovnalosti.

---

## 2. Celkový popis

### 2.1 Perspektiva produktu
Existující pluginy (EnvFile, Environment Manager) řeší načítání env vars, ale žádný nevaliduje kompletní „environment readiness" — zda má vývojář vše potřebné ke spuštění projektu.

### 2.2 Uživatelské skupiny
- **Noví členové týmu** — „proč mi to nejde spustit?"
- **Vývojáři přepínající projekty** — drift po delší době
- **Tech leads** — chtějí eliminovat „works on my machine"

---

## 3. Funkční požadavky

### FR-1: Environment Manifest
- **FR-1.1:** Manifest soubor `.run-requirements.yaml`:
  ```yaml
  jdk:
    version: "17+"
    vendor: "temurin" # optional
  env_vars:
    required:
      - DATABASE_URL
      - API_KEY
    optional:
      - DEBUG_MODE
  tools:
    - docker: "24+"
    - node: "18+"
  ports:
    - 8080
    - 5432
  ```
- **FR-1.2:** Auto-generace manifestu z existujících run configurations
- **FR-1.3:** Manifest verzovaný v repo (VCS)

### FR-2: Health Check
- **FR-2.1:** Automatická validace při otevření projektu
- **FR-2.2:** Health report v tool window:
  - ✅ JDK 17 — OK
  - ❌ DATABASE_URL — not set
  - ⚠️ Docker 23.0 — minimum 24+ doporučeno
  - ❌ Port 5432 — obsazený (PID 12345)
- **FR-2.3:** Quick-fix actions:
  - „Set environment variable" → otevře dialog
  - „Download JDK" → IntelliJ JDK manager
  - „Kill process on port" → terminace
- **FR-2.4:** Re-check action (manuální spuštění)

### FR-3: Run Configuration Validation
- **FR-3.1:** Inspection pro run configurations — chybějící env vars
- **FR-3.2:** Diff mezi run configs ve VCS a lokální stav
- **FR-3.3:** Warning při vytvoření run config bez required env vars

### FR-4: Team Sync (Paid)
- **FR-4.1:** Porovnání environment stavu mezi členy týmu (anonymizované)
- **FR-4.2:** „Common issues" dashboard — co nejčastěji chybí
- **FR-4.3:** Auto-sync .env.example z manifestu

### FR-5: Onboarding Mode (Paid)
- **FR-5.1:** Step-by-step průvodce: „Set up your environment"
- **FR-5.2:** Progress tracker (kolik z požadavků splněno)
- **FR-5.3:** Integrace s README.md (parsování setup sekcí)

---

## 4. Nefunkční požadavky

### NFR-1: Výkon
- Health check < 3s na projektu s 20 požadavky
- Non-blocking — vývojář může pracovat během validace

### NFR-2: Kompatibilita
- IntelliJ IDEA 2024.3+
- JDK, env vars, systémové nástroje, porty

### NFR-3: Privacy
- Žádné env var hodnoty se neodesílají nikam
- Team sync anonymizovaný (jen „chybí/nechybí")

---

## 5. Datový model

### 5.1 EnvironmentRequirement
```
EnvironmentRequirement {
  type: Enum (JDK, ENV_VAR, TOOL, PORT, SERVICE)
  name: String
  constraint: String? (version range, regex)
  required: Boolean
  description: String?
}
```

### 5.2 HealthCheckResult
```
HealthCheckResult {
  requirement: EnvironmentRequirement
  status: Enum (OK, MISSING, INCOMPATIBLE, WARNING)
  actualValue: String?
  message: String
  quickFix: QuickFixAction?
}
```

---

## 6. Freemium model

### Free tier
- Manifest definice + validace (max 10 požadavků)
- Health check tool window
- Basic quick-fixes (set env var, JDK download link)

### Paid tier
- Neomezené požadavky
- Team sync dashboard
- Onboarding mode
- Run config diff
- Port conflict resolution
- Auto-generate manifest
