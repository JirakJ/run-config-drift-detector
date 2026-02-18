# Run Config Drift Detector

## Eliminate "Works on My Machine" Before It Starts

---

## The Problem

"It works on my machine" is the most expensive sentence in software development. Run configurations drift silently — wrong JDK version, missing environment variables, occupied ports, incorrect working directories. New team members spend hours debugging setup issues that have nothing to do with the code.

## The Solution

Run Config Drift Detector compares your IDE run configurations against a `.run-requirements.yaml` manifest, catching JDK mismatches, missing environment variables, and incorrect settings **before** they cause mysterious failures.

---

## Key Features

### 1. Environment Manifest
Define your project's expected environment in a simple `.run-requirements.yaml` file: JDK version and vendor, required environment variables, external tools, and port requirements.

### 2. Health Check Dashboard
A visual drift report with clear pass, fail, and warning indicators for each requirement. See at a glance what is correct and what needs attention.

### 3. JDK Validation
Verify that the JDK version and vendor configured in your IDE match what your project actually requires. Catch version mismatches before they cause subtle compilation or runtime issues.

### 4. Environment Variable Checks
Ensure all required environment variables are set with the correct values. No more "API key not found" errors after cloning a repo.

### 5. Port Conflict Detection
Detect ports already in use before running your application. Know immediately if another process is blocking the port your server needs.

### 6. Quick-Fix Suggestions
Every detected drift issue comes with a one-click fix. Set environment variables, switch JDK versions, and resolve port conflicts directly from the drift report.

### 7. Auto-Scan on Startup
Automatically validate environment requirements when the project opens. Disable it if you prefer manual scanning.

### 8. Auto-Generate Manifest
Generate a `.run-requirements.yaml` file from your existing run configurations with one click. No need to write it from scratch.

---

## How It Works

1. **Define requirements** — Create a `.run-requirements.yaml` file in your project root describing the expected JDK, env vars, tools, and ports.
2. **Open the project** — The plugin automatically scans on startup and shows the health check dashboard.
3. **Review the drift report** — Green checks for passing requirements, red crosses for failures, yellow warnings for potential issues.
4. **Fix with one click** — Use quick-fix suggestions to resolve every detected drift directly from the dashboard.

---

## Pricing

### Free
- Full drift detection for up to 10 requirements
- Health check dashboard with pass/fail/warning indicators
- JDK, environment variable, working directory, and port validation
- Quick-fix suggestions for each drift issue
- Auto-scan on startup
- Manual re-scan via Tools menu

### Pro — $2.90/month or $29/year
- Unlimited requirements per manifest
- Onboarding mode for new team members
- Auto-generate manifest from existing run configurations
- README.md integration

### Team — $5.90/user/month
- All Pro features
- Team configuration sync
- Common issues dashboard
- Auto-sync .env.example files

---

## Testimonials

> *"Onboarding new developers went from half a day to 15 minutes."*
> — [Your name here], Engineering Manager

> *"We added the manifest to our repo and never had a 'works on my machine' issue again."*
> — [Your name here], Senior Developer

> *"The port conflict detection alone saved me from a frustrating debugging session."*
> — [Your name here], Backend Developer

---

## FAQ

### What is .run-requirements.yaml?
It is a simple YAML file that describes the expected environment for your project: JDK version, required environment variables, external tools, and port requirements. You commit it to version control so the whole team uses the same requirements.

### Does it modify my run configurations?
Only when you explicitly click a quick-fix. The plugin never changes your settings automatically.

### Which JetBrains IDEs are supported?
Run Config Drift Detector works with IntelliJ IDEA, PyCharm, WebStorm, and any JetBrains IDE that supports the platform plugin.

### Can I disable auto-scan on startup?
Yes. Go to Settings > Tools > Run Config Drift Detector and disable the auto-scan option.

### Does the free tier have any feature limitations?
The free tier includes all detection and quick-fix features but is limited to 10 requirements per manifest. Upgrade to Pro for unlimited requirements.

---

## Get Started

Install Run Config Drift Detector from the [JetBrains Marketplace](https://plugins.jetbrains.com) and add a `.run-requirements.yaml` to your project. Free features are available immediately — no account required.
