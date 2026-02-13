# Changelog

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
- Auto-generate manifest from existing run configurations (Pro)
- Onboarding mode with step-by-step setup guide and progress tracker (Pro)
- Unlimited requirements (Pro)
- README.md integration for onboarding (Pro)
- Team sync for comparing environment state between team members (Team)
- Common issues dashboard (Team)
- Auto-sync `.env.example` files (Team)
- VCS-friendly manifest designed for version control
- Run configuration diff between VCS and local state
- Balloon notification support
- Privacy-first design: no environment variable values sent externally
