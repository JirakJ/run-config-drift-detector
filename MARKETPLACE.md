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
    <li><b>Onboarding Mode</b> — step-by-step setup guide with a progress tracker for new team members</li>
    <li><b>Auto-Generate Manifest</b> — create <code>.run-requirements.yaml</code> from existing run configurations</li>
    <li><b>README Integration</b> — keep your README in sync with environment requirements</li>
    <li><b>Team Sync (Team)</b> — anonymized environment comparison across team members</li>
    <li><b>Common Issues Dashboard (Team)</b> — see which setup problems hit your team most often</li>
    <li><b>Auto-Sync .env.example (Team)</b> — keep <code>.env.example</code> files up to date automatically</li>
</ul>

<h3>Free &amp; Open Source</h3>
<p>Every feature is free for everyone — no license, no limits. The source code is available under the Apache-2.0 license at <a href="https://github.com/JirakJ/run-config-drift-detector">github.com/JirakJ/run-config-drift-detector</a>. Issues and pull requests are welcome.</p>

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

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

## Screenshots Needed

1. **Health Check Dashboard** — the Drift Report tool window showing ✅/❌/⚠️ status indicators and overall health score
2. **Manifest File** — `.run-requirements.yaml` with syntax highlighting showing JDK, env vars, tools, and ports
3. **Quick-Fix in Action** — editor popup offering to set a missing environment variable or download JDK
4. **Onboarding Mode** — step-by-step progress tracker guiding a new developer through setup
5. **Port Conflict Detection** — notification showing a port conflict with a "Kill Process" quick-fix
