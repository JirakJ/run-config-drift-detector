# Run Config Drift Detector

[![Build](https://github.com/JirakJ/run-config-drift-detector/actions/workflows/verify.yml/badge.svg)](https://github.com/JirakJ/run-config-drift-detector/actions/workflows/verify.yml)
[![Version](https://img.shields.io/badge/version-2026.8.0-blue.svg)](https://github.com/JirakJ/run-config-drift-detector/releases)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)

> **Free & open source** — every feature is unlocked for everyone, no license needed. Licensed under [Apache-2.0](LICENSE).

> Eliminates "Works on My Machine" — validate environment requirements before you hit Run

## ✨ Features

- **Requirements manifest** — declare the required JDK version, environment variables and working directory in `.run-requirements.yaml`
- **JDK check** — compares the project SDK's major version with the required one (also flags a missing SDK)
- **Environment variable check** — reports required variables missing from the environment the IDE runs with
- **Working directory check** — verifies the directory exists and is a directory
- **Drift Report tool window** — every issue with a ❌/⚠️ icon, a fix suggestion and an overall health score (errors −25, warnings −10)
- **Auto-scan on project open** with a notification when drift is found, plus manual **Tools → Re-scan for Drift**

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
   jdk: 17                 # major version of the project SDK
   env_vars:               # must be set in the environment the IDE was started with
     - DATABASE_URL
     - API_KEY
   working_dir: backend    # relative to the project root, or absolute
   ```
2. Open the project — the plugin scans on startup and shows results in the **Drift Report** tool window
3. Fix the reported issues and click **Re-scan** (or **Tools → Re-scan for Drift**)

## ⚙️ Configuration

**Settings → Tools → Run Config Drift Detector**

| Setting | Default | Description |
|---------|---------|-------------|
| Enable drift detection | `true` | Master toggle for drift detection |
| Requirements file path | `.run-requirements.yaml` | Manifest location, relative to the project root or absolute |
| Auto-scan on project open | `true` | Run drift detection when the project opens |

## 🎯 Usage

### Drift Report
Open **View → Tool Windows → Drift Report**. Each issue shows its severity (❌ error, ⚠️ warning), a description and a
fix suggestion; the header shows the health score (100 minus 25 per error and 10 per warning) and the scan time.

### Manifest in version control
Commit `.run-requirements.yaml` so every team member is checked against the same requirements.

## 🔌 Keyboard Shortcuts

No default keyboard shortcuts are assigned. You can configure a custom shortcut for "Re-scan for Drift" via **Settings → Keymap**.

## 📋 Requirements

- IntelliJ IDEA 2023.2+ (or another IntelliJ-based IDE, build 232+)

## 🏗️ Building from Source

```bash
git clone https://github.com/JirakJ/run-config-drift-detector.git
cd run-config-drift-detector
./gradlew build
```

## License

Apache License 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE). Copyright 2026 Jakub Jirák.

## Contributing

Issues and pull requests are welcome at https://github.com/JirakJ/run-config-drift-detector.

## 👤 Author

**Jakub Jirák** — [@JirakJ](https://github.com/JirakJ)
