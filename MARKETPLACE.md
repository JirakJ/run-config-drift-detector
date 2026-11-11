# Run Config Drift Detector — JetBrains Marketplace Listing

## Plugin Name

Run Config Drift Detector

## Tagline

Stop "works on my machine" — validate environment requirements before you run.

## Short Description

Validates your dev environment against a shared manifest. Checks the JDK version, environment variables and working directory — flags drift before it breaks your build.

## Full Description

```html
<h2>Run Config Drift Detector</h2>

<p>Every team has that one developer who spends half a day debugging a failing build — only to discover
a missing environment variable. <b>Run Config Drift Detector</b> eliminates "works on my machine" by
letting you declare environment requirements in a simple <code>.run-requirements.yaml</code> manifest
and validating every developer's setup against it automatically.</p>

<h3>Key Features</h3>
<ul>
    <li><b>Requirements Manifest</b> — declare the JDK version, environment variables and working directory in <code>.run-requirements.yaml</code></li>
    <li><b>JDK Check</b> — project SDK major version vs. the requirement; a missing SDK is flagged</li>
    <li><b>Environment Variable Check</b> — required variables missing from the IDE environment are reported</li>
    <li><b>Working Directory Check</b> — the directory must exist and be a directory</li>
    <li><b>Drift Report</b> — tool window with severity, fix suggestion per issue and a health score</li>
    <li><b>Auto-Scan on Startup</b> and <b>Manual Re-scan</b> via Tools → Re-scan for Drift</li>
</ul>

<h3>Free &amp; Open Source</h3>
<p>Every feature is free for everyone — no license, no limits. The source code is available under the Apache-2.0 license at <a href="https://github.com/JirakJ/run-config-drift-detector">github.com/JirakJ/run-config-drift-detector</a>. Issues and pull requests are welcome.</p>

<h3>Getting Started</h3>
<ol>
    <li><b>Install</b> — search "Run Config Drift Detector" in Settings → Plugins → Marketplace</li>
    <li><b>Create manifest</b> — add a <code>.run-requirements.yaml</code> to your project root declaring <code>jdk</code>, <code>env_vars</code> and <code>working_dir</code></li>
    <li><b>Check health</b> — open the Drift Report tool window to see your environment status at a glance</li>
</ol>

<h3>Compatibility</h3>
<p>IntelliJ IDEA 2023.2+ and other IntelliJ-based IDEs.</p>

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

1. **Drift Report** — the tool window with issues, fix suggestions and the health score
2. **Manifest file** — a `.run-requirements.yaml` with `jdk`, `env_vars` and `working_dir`
3. **Startup notification** — drift detected when the project opens
