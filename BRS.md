# Run Config Drift Detector — Business Requirements Specification (BRS)

**Verze:** 2026.2.1
**Autor:** Jakub Jirák
**Datum:** 12. února 2026
**Model:** Freemium JetBrains Plugin

---

## 1. Executive Summary

Run Config Drift Detector eliminuje „works on my machine" problém. Plugin definuje a validuje environment požadavky pro projekt a okamžitě ukáže, co vývojáři chybí ke spuštění — JDK, env vars, nástroje, porty.

---

## 2. Obchodní cíle

### 2.1 KPI
| Metrika | Cíl (6 měsíců) | Cíl (12 měsíců) |
|---------|:---:|:---:|
| Celkové instalace | 6 000 | 24 000 |
| MAU | 2 500 | 10 000 |
| Paid konverze | 3 % | 5 % |
| Marketplace rating | ≥ 4.0 | ≥ 4.2 |

---

## 3. Cílový trh

- **Týmy s onboarding bolestí** (40 %) — setup nového člena
- **Multi-project vývojáři** (35 %) — přepínání kontextů
- **DevRel / Open source** (25 %) — contributor onboarding

---

## 4. Konkurenční prostředí

| Řešení | Omezení |
|--------|---------|
| EnvFile (3M+ downloads) | Jen načítání, ne validace |
| Docker / devcontainer | Těžkotonážní, ne IDE-native |
| Dokumentace (README) | Manuální, zastarává |

**Diferenciace:** Automatická validace + actionable quick-fixes přímo v IDE.

---

## 5. Revenue model

| Tier | Cena |
|------|------|
| **Free** | $0 — manifest, health check, max 10 požadavků |
| **Pro** | $2.90/měsíc nebo $29/rok — neomezeno, onboarding, auto-generate |
| **Team** | $5.90/user/měsíc — Pro + team sync, common issues |

---

## 6. Go-to-Market

1. Blog: „'Works on My Machine' Ends Today"
2. DevRel community (open source maintainer skupiny)
3. Cross-promo: „Pair with EnvFile for complete env management"

---

## 7. Milníky

| Milestone | Popis |
|-----------|-------|
| **M1: MVP** | Manifest, health check, basic quick-fixes |
| **M2: Auto** | Auto-generate manifest z run configs |
| **M3: Onboarding** | Step-by-step průvodce, progress tracker |
| **M4: Team** | Team sync, common issues dashboard |
| **M5: Integration** | .env.example sync, README parsing |

---

## 8. Success Criteria

1. 12 000+ instalací do 9 měsíců
2. ≥ 3 % paid konverze
3. Viditelná redukce onboarding času u adopting týmů
4. Open source projekty adoptují `.run-requirements.yaml`
