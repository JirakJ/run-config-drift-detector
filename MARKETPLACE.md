# Run Config Drift Detector — JetBrains Marketplace Listing

## Plugin Name

Run Config Drift Detector

## Tagline

Stop "works on my machine" — validate environment requirements before you run.

## Short Description

Validates your dev environment against a shared manifest. Checks JDK, env vars, tools, and ports — flags drift before it breaks your build.

## Full Description

```html
<h2>Run Config Drift Detector</h2>

<p>Every team has that one developer who spends half a day debugging a failing build — only to discover
a missing environment variable. <b>Run Config Drift Detector</b> eliminates "works on my machine" by
letting you declare environment requirements in a simple <code>.run-requirements.yaml</code> manifest
and validating every developer's setup against it automatically.</p>

<h3>Key Features</h3>
<ul>
    <li><b>Environment Manifest</b> — declare JDK version &amp; vendor, environment variables, CLI tools, required ports, and working directory in a single YAML file</li>
    <li><b>Health Check Dashboard</b> — visual ✅/❌/⚠️/🔴 status indicators with an overall health score</li>
    <li><b>JDK Validation</b> — verifies both version and vendor (e.g., Temurin 17) match the project requirements</li>
    <li><b>Environment Variable Verification</b> — checks required and optional env vars are set</li>
    <li><b>Port Conflict Detection</b> — flags ports already in use before you hit "Run"</li>
    <li><b>Working Directory Validation</b> — ensures the expected project structure is in place</li>
    <li><b>Quick-Fix Suggestions</b> — one-click actions to set env vars, download the correct JDK, or kill a process occupying a port</li>
    <li><b>Auto-Scan on Startup</b> — validates your environment every time you open the project</li>
    <li><b>Manual Re-scan</b> — trigger a check anytime via Tools → Re-scan for Drift</li>
    <li><b>Onboarding Mode (Pro)</b> — step-by-step setup guide with a progress tracker for new team members</li>
    <li><b>Auto-Generate Manifest (Pro)</b> — create <code>.run-requirements.yaml</code> from existing run configurations</li>
    <li><b>README Integration (Pro)</b> — keep your README in sync with environment requirements</li>
    <li><b>Team Sync (Team)</b> — anonymized environment comparison across team members</li>
    <li><b>Common Issues Dashboard (Team)</b> — see which setup problems hit your team most often</li>
    <li><b>Auto-Sync .env.example (Team)</b> — keep <code>.env.example</code> files up to date automatically</li>
</ul>

<h3>Free Tier</h3>
<ul>
    <li>Full environment manifest support (up to 10 requirements)</li>
    <li>Health check dashboard with status indicators</li>
    <li>JDK, env var, working directory, and port validation</li>
    <li>Quick-fix suggestions</li>
    <li>Auto-scan on project open</li>
</ul>

<h3>Pro Tier ($2.90/month)</h3>
<ul>
    <li>Unlimited requirements per manifest</li>
    <li>Onboarding mode with step-by-step progress tracking</li>
    <li>Auto-generate manifest from run configurations</li>
    <li>README.md integration</li>
</ul>

<h3>Team Tier ($5.90/user/month)</h3>
<ul>
    <li>All Pro features</li>
    <li>Team sync — anonymized environment comparison</li>
    <li>Common issues dashboard</li>
    <li>Auto-sync <code>.env.example</code> files</li>
</ul>

<h3>Getting Started</h3>
<ol>
    <li><b>Install</b> — search "Run Config Drift Detector" in Settings → Plugins → Marketplace</li>
    <li><b>Create manifest</b> — add a <code>.run-requirements.yaml</code> to your project root declaring JDK, env vars, tools, and ports</li>
    <li><b>Check health</b> — open the Drift Report tool window to see your environment status at a glance</li>
</ol>

<h3>Compatibility</h3>
<p>IntelliJ IDEA 2024.3+ and all compatible JetBrains IDEs. Requires Java 17+.</p>

<h3>Part of Developer Productivity Suite</h3>
<p>Run Config Drift Detector works alongside other developer productivity plugins to streamline
your development workflow and reduce environment-related friction.</p>
```

## Tags

`developer-tools`, `devops`, `configuration`, `onboarding`, `environment`

## Category

Build & DevOps

## Pricing

| Tier | Price | Features |
|------|-------|----------|
| Free | $0 | Manifest support (10 requirements), health dashboard, JDK/env var/port/working dir validation, quick-fixes, auto-scan |
| Pro | $2.90/month or $29/year | Unlimited requirements, onboarding mode, auto-generate manifest, README integration |
| Team | $5.90/user/month | All Pro + team sync, common issues dashboard, auto-sync .env.example |

## Screenshots Needed

1. **Health Check Dashboard** — the Drift Report tool window showing ✅/❌/⚠️ status indicators and overall health score
2. **Manifest File** — `.run-requirements.yaml` with syntax highlighting showing JDK, env vars, tools, and ports
3. **Quick-Fix in Action** — editor popup offering to set a missing environment variable or download JDK
4. **Onboarding Mode (Pro)** — step-by-step progress tracker guiding a new developer through setup
5. **Port Conflict Detection** — notification showing a port conflict with a "Kill Process" quick-fix
