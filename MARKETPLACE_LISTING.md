# JetBrains Marketplace Listing — Run Config Drift Detector

---

## 📝 Plugin Information

| Field       | Value                                  |
|-------------|----------------------------------------|
| **Name**    | Run Config Drift Detector              |
| **Plugin ID** | `com.jakubjirak.runconfigdrift`     |
| **Version** | 2026.2.12                              |
| **Category** | Build & Deployment / Developer Tools  |
| **Tags**    | Environment Validation, Drift Detection, Onboarding, DevOps, Configuration |

---

## 🎯 Short Description

> Eliminates "Works on My Machine" — validates JDK, env vars, ports, and tools against a project manifest before you hit Run, with a health check dashboard. (155 chars)

---

## 📖 Full Description

### 🚀 Never Hear "Works on My Machine" Again

**The Problem:** New team members spend hours debugging environment issues. Missing environment variables, wrong JDK versions, occupied ports, and misconfigured working directories cause silent failures that waste everyone's time.

**The Solution:** Run Config Drift Detector lets you declare your project's environment requirements in a `.run-requirements.yaml` manifest and automatically validates them every time you open the project. A health check dashboard gives you instant ✅/❌/⚠️ visibility into every requirement.

---

### ✨ Key Features

#### ✨ Features (all free)

- 📋 **Environment Manifest** — Declare project requirements in `.run-requirements.yaml` (JDK, env vars, tools, ports)
- 📊 **Health Check Dashboard** — See all drift issues at a glance with ✅/❌/⚠️ status indicators and a health score
- ☕ **JDK Validation** — Checks project SDK version and vendor against requirements
- 🔑 **Environment Variable Check** — Verifies required env vars are set in run configurations
- 📂 **Working Directory Validation** — Ensures the correct working directory is configured
- 🔌 **Port Conflict Detection** — Checks for port availability before running
- 🔧 **Fix Suggestions** — Actionable quick-fix recommendations (set env var, download JDK, kill process on port)
- 🔄 **Auto-scan on Startup** — Automatically validates environment when the project opens
- 🛠️ **Manual Re-scan** — Via **Tools → Re-scan for Drift**
- 📖 **Onboarding Mode** — Step-by-step setup guide with progress tracker for new team members
- ⚙️ **Auto-generate Manifest** — Create `.run-requirements.yaml` from existing run configurations
- 📄 **README.md Integration** — Embed onboarding status in project README

---

### 💰 Pricing Model

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

---

### 🎯 Perfect For

- **Team leads** onboarding new developers to complex projects
- **DevOps engineers** standardizing development environments
- **Open-source maintainers** reducing contributor setup friction
- **Full-stack developers** juggling multiple services with port and env var requirements

---

### ⚙️ Requirements

| Requirement        | Value                    |
|--------------------|--------------------------|
| **Minimum IDE Build** | 232 (IntelliJ 2023.2+) |
| **Java Version**   | 17+                      |
| **Optional**       | Git4Idea plugin (for team sync features) |

---

### 🚀 Quick Start

1. **Install** the plugin from JetBrains Marketplace
2. **Create** a `.run-requirements.yaml` file in your project root defining JDK, env vars, tools, and ports
3. **Open** the project — the plugin auto-scans and shows the Drift Report tool window
4. **Review** the health check dashboard with ✅/❌/⚠️ indicators
5. **Apply fixes** using the actionable quick-fix suggestions
6. **Re-scan** anytime via **Tools → Re-scan for Drift**

---

## 📸 Screenshots & Media

1. **Health Check Dashboard** — Drift Report tool window showing ✅/❌/⚠️ status for all requirements with health score
2. **Manifest File** — `.run-requirements.yaml` with JDK, env vars, tools, and port definitions
3. **Fix Suggestions** — Quick-fix panel with actionable recommendations (set env var, download JDK, kill port)
4. **JDK Validation** — SDK version and vendor mismatch detection with download link
5. **Port Conflict** — Port availability check showing occupied ports and the processes using them
6. **Settings Panel** — Plugin configuration page under Settings → Tools
7. **Onboarding Mode** — Step-by-step environment setup wizard with progress tracker
8. **Auto-generate Manifest** — One-click manifest creation from existing run configurations

---

## 🏷️ Marketplace Categories

| Type          | Category                  |
|---------------|---------------------------|
| **Primary**   | Build & Deployment        |
| **Secondary** | Developer Tools           |

---

## 🔍 Keywords (SEO)

`environment validation`, `drift detection`, `run configuration`, `onboarding`, `works on my machine`, `JDK validation`, `environment variables`, `port conflicts`, `developer experience`, `team sync`, `project setup`, `dev environment`, `configuration drift`, `health check`, `.run-requirements.yaml`

---

## 📋 Compatibility

| IDE                          | Compatible |
|------------------------------|------------|
| IntelliJ IDEA Community      | ✅          |
| IntelliJ IDEA Ultimate       | ✅          |
| PyCharm                      | ✅          |
| WebStorm                     | ✅          |
| GoLand                       | ✅          |
| PhpStorm                     | ✅          |
| CLion                        | ✅          |
| Rider                        | ✅          |
| RubyMine                     | ✅          |
| Android Studio               | ✅          |

**Minimum Build:** 232 (IntelliJ Platform 2023.2+)

---

## 📞 Vendor Information

| Field        | Value                        |
|--------------|------------------------------|
| **Vendor**   | Jakub Jirak                  |
| **Email**    | jirak@jakubjirak.com         |
| **Website**  | https://jakubjirak.com       |
| **Support**  | support@jakubjirak.com       |

---

## ✅ Pre-Launch Checklist

- [ ] Plugin icon (40×40 SVG and 80×80 PNG) prepared
- [ ] Screenshots (1280×800 minimum) captured for all key features
- [ ] Short description within 255-character limit
- [ ] Full description reviewed and proofread
- [ ] Product descriptor code `PRUNCONFIGDRIFT` verified
- [ ] Plugin signed with JetBrains Marketplace certificate
- [ ] License agreement (EULA) uploaded
- [ ] CHANGELOG.md up to date with latest version notes
- [ ] Compatibility tested on minimum build (232)
- [ ] `.run-requirements.yaml` schema documented
- [ ] Support email monitored and responsive
