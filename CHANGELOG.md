# Changelog

## [2026.3.6] - 2026-04-17

### Changed
- Enabled freemium licensing via `<product-descriptor optional="true">`. Plugin remains fully functional without a license; premium features may be introduced in future minor releases.


## [2026.2.6] - 2026-02-14

### Fixed
- Removed deprecated API usage (postStartupActivity → projectActivity, StatusBarWidget.getPresentation)
- No experimental API usage


## [2026.2.5] - 2026-02-13

### Added
- Added ProGuard obfuscation for code protection


## [2026.2.4] - 2026-02-13

### Changed
- Version bump for JetBrains Marketplace publication


## [2026.2.3] - 2026-02-13

### Changed
- Expanded compatibility range: sinceBuild 232.0+ (IntelliJ 2023.2+)
- Removed upper version bound for future IDE compatibility


## [2026.2.2] - 2026-02-13

### Added
- Initial release
- Environment manifest support via `.run-requirements.yaml` (JDK version/vendor, environment variables, tools, ports)
- Health check dashboard in Drift Report tool window with ✅/❌/⚠️/🔴 status indicators
- Health score overview for all drift issues at a glance
- JDK version and vendor validation against manifest requirements
- Environment variable validation for run configurations (required and optional)
- Working directory validation
- Port conflict detection and availability checking
- Quick-fix actions: set environment variable, download JDK, kill process on port
- Auto-scan on project open with startup activity
- Manual re-scan action via Tools menu
- Configurable settings: enable/disable detection, requirements file path, auto-scan toggle
- Auto-generate manifest from existing run configurations
- Onboarding mode with step-by-step setup guide and progress tracker
- Unlimited requirements
- README.md integration for onboarding
- Team sync for comparing environment state between team members
- Common issues dashboard
- Auto-sync `.env.example` files
- VCS-friendly manifest designed for version control
- Run configuration diff between VCS and local state
- Balloon notification support
- Privacy-first design: no environment variable values sent externally
