# Run Config Drift Detector

[![Build](https://github.com/JirakJ/run-config-drift-detector/actions/workflows/build.yml/badge.svg)](https://github.com/JirakJ/run-config-drift-detector/actions/workflows/build.yml)
[![Version](https://img.shields.io/badge/version-2026.2.2-blue.svg)](https://github.com/JirakJ/run-config-drift-detector/releases)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

> Eliminates "Works on My Machine" — validate environment requirements before you hit Run

## ✨ Features

- **Environment manifest** — declare project requirements in `.run-requirements.yaml` (JDK, env vars, tools, ports)
- **Health check dashboard** — see all drift issues at a glance with ✅/❌/⚠️ status indicators and a health score
- **JDK validation** — checks project SDK version and vendor against requirements
- **Environment variable check** — verifies required env vars are set in run configurations
- **Working directory validation** — ensures the correct working directory is configured
- **Port conflict detection** — checks for port availability before running
- **Fix suggestions** — actionable quick-fix recommendations for each drift issue (set env var, download JDK, kill process on port)
- **Auto-scan on startup** — automatically validates environment when the project opens
- **Manual re-scan** via **Tools → Re-scan for Drift**
- **Unlimited requirements** per manifest
- **Onboarding mode** — step-by-step setup guide with progress tracker
- **Auto-generate manifest** from existing run configurations
- **README.md integration** for onboarding
- **Team sync** — compare environment state between team members
- **Common issues dashboard**
- **Auto-sync `.env.example`** files

## 📦 Installation

### From JetBrains Marketplace
1. Open **Settings → Plugins → Marketplace**
2. Search for **"Run Config Drift Detector"**
3. Click **Install**

### From ZIP
1. Download the latest release from [Releases](https://github.com/JirakJ/run-config-drift-detector/releases)
2. Open **Settings → Plugins → ⚙️ → Install Plugin from Disk**
3. Select the downloaded ZIP file

## 🚀 Quick Start

1. Create a `.run-requirements.yaml` file in your project root:
   ```yaml
   jdk:
     version: "17"
     vendor: "temurin"    # optional
   env:
     - name: DATABASE_URL
       required: true
       description: "PostgreSQL connection string"
     - name: API_KEY
       required: false
   tools:
     - name: docker
       version: "20+"
     - name: node
       version: "18+"
   ports:
     - 8080
     - 5432
   ```
2. Open the project — the plugin auto-scans and shows results in the **Drift Report** tool window
3. Click on any ❌ issue to see the fix suggestion and apply the quick-fix

## ⚙️ Configuration

**Settings → Tools → Run Config Drift Detector**

| Setting | Default | Description |
|---------|---------|-------------|
| Enable drift detection | `true` | Master toggle for drift detection |
| Requirements file path | `.run-requirements.yaml` | Path to the requirements manifest file |
| Auto-scan on project open | `true` | Automatically run drift detection when the project opens |

## 🎯 Usage

### Health Check Dashboard
Open **View → Tool Windows → Drift Report** to see the health check results. Each requirement shows:
- ✅ **OK** — requirement is satisfied
- ❌ **MISSING** — requirement is not met
- ⚠️ **WARNING** — potential issue detected
- 🔴 **INCOMPATIBLE** — version mismatch or conflict

### Quick Fixes
Click on any failed check to see actionable suggestions:
- **Set environment variable** — opens run configuration editor
- **Download JDK** — triggers JDK download via IDE
- **Kill process on port** — frees up the required port

### Manual Re-scan
Go to **Tools → Re-scan for Drift** to manually re-run drift detection after making changes to your environment.

### VCS Integration
The `.run-requirements.yaml` manifest is designed to be committed to version control, ensuring all team members share the same environment requirements.

## 🔌 Keyboard Shortcuts

No default keyboard shortcuts are assigned. You can configure a custom shortcut for "Re-scan for Drift" via **Settings → Keymap**.

## 📋 Requirements

- IntelliJ IDEA 2024.3+ (build 243+)
- Java 17+

## 🏗️ Building from Source

```bash
git clone https://github.com/JirakJ/run-config-drift-detector.git
cd run-config-drift-detector
./gradlew build
```

## 📝 License

MIT License — see [LICENSE](LICENSE)

## 👤 Author

**Jakub Jirák** — [@JirakJ](https://github.com/JirakJ)
