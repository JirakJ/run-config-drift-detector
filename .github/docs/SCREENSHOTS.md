# Run Config Drift Detector — Screenshot Plan

## Screenshots

### 1. Health Check Dashboard
**Title:** Visual drift report at a glance
**Capture:** The Drift Report tool window showing a list of requirements with green check marks for passing items, red crosses for failures (wrong JDK version, missing env var), and yellow warnings.

### 2. Requirements Manifest
**Title:** Define your environment in YAML
**Capture:** A `.run-requirements.yaml` file open in the editor showing JDK version, required environment variables, tool requirements, and port definitions with syntax highlighting.

### 3. JDK Mismatch Detection
**Title:** Catch JDK version mismatches instantly
**Capture:** The drift report showing a JDK validation failure: expected JDK 17 (Temurin) but found JDK 11 (Oracle), with a quick-fix button to switch JDK.

### 4. Quick-Fix in Action
**Title:** One-click fixes for every drift issue
**Capture:** A drift report entry for a missing environment variable with the quick-fix popup showing "Set DATABASE_URL in run configuration" action.

### 5. Port Conflict Detection
**Title:** Know about port conflicts before running
**Capture:** The drift report showing a port conflict: port 8080 required but already in use by another process, with process name and PID displayed.

### 6. Auto-Scan Notification
**Title:** Automatic validation on project open
**Capture:** A balloon notification showing "Run Config Drift Detector: 2 issues found" with a link to open the drift report.

---

## Video Demo Outline (40 seconds)

| Time | Scene | Description |
|------|-------|-------------|
| 0-5s | Intro | Show plugin name with a developer looking confused at a "works on my machine" error |
| 5-12s | Manifest | Show a .run-requirements.yaml file with JDK, env vars, and port definitions |
| 12-22s | Dashboard | Open the Drift Report tool window showing mixed results: some passing, some failing |
| 22-30s | Quick Fix | Click quick-fix on a JDK mismatch, show it resolving to green |
| 30-37s | Team | Show the manifest committed in git, another developer opening the project with auto-scan |
| 37-40s | CTA | "Install Run Config Drift Detector — free on JetBrains Marketplace" |
