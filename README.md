# KingGunGame

Arena-GunGame für das KING-Netzwerk: Join über Schild, Lobby mit Shop und Rankings, Map-Voting, **Level pro Kill**, Gadgets, Buffs, Teams und Kopfgeld.

**Version:** 1.0.0  
**Minecraft:** Paper **1.16.5** (`api-version: 1.16`)  
**Java:** 8  
**Autor:** Felix Payne

**Runtime-Pflicht:** [HolographicDisplays](https://www.spigotmc.org/resources/holographic-displays.4924/) — ohne HD startet das Plugin nicht.

---

## Wofür ist das Plugin da?

Ein eigener GunGame-Server: Spieler joinen per Schild in die laufende Runde, steigen mit Kills im Level auf (bessere Waffen/Rüstung aus `level.yml`), kaufen Gadgets in der Lobby, können Teams bilden und Kopfgeld aussetzen. Stats liegen in MySQL.

Kein Survival-Addon, sondern ein voller Minigame-Stack.

---

## So läuft das Spiel

1. Spieler kommt auf den Server → **Lobby** (Adventure, Scoreboard, Shop-Villager, Top-Rankings).
2. **Join-Schild** (Eiche): Zeile 1 `[GunGame]`, Zeile 2 `join` — wird vom Plugin formatiert. Rechtsklick mit `gungame.user.signclick` → Teleport auf die aktuelle Map, Level-Inventar, Aufnahme in die Ingame-Liste.
3. **Kampf:** Kein Vanilla-Death. Kill → Level + Tokens (Standard +100). Tod → ca. **33 % Levelverlust**, Respawn am Map-Spawn, Spawnschutz.
4. Extra-Level, wenn das Opfer deutlich höher war (+2 / +3 / +5).
5. **Leave** nur im Spawnschutz: `/leave`.
6. Optional **Map-Vote** (`/vote`) oder Admin-`/forcemap`.

---

## Features

| Feature | Was es macht |
|--------|----------------|
| Join-Schild | ONLINE / Spielerzahl / Mapname |
| Maps | Registrieren, löschen, votable, Teams ja/nein, Spawnschutz Kreis/Viereck |
| Level-Progression | `level.yml` (Beispiel ~30 Stufen Stein → besser) |
| Shop | Lobby-Villager „Gadget - Shop“, Kauf gegen Tokens |
| Gadgets | Hook, InstantLevel I–III, Shockwave, Joe (Silberfisch), Backporter |
| Buffs | Zufalls-Pads auf Obsidian/Glas (Stärke, Speed, Jump, Extra-HP, Wasserläufer, Instant-Level) |
| Teams | 2er-Teams, Friendly Fire aus (außer Wasser/Lava) |
| Kopfgeld | Tokens aussetzen, Kill kassiert, GUI |
| Ranking | Armorstands Platz 1–3, Hologramme 4–10 |
| Sprache | DE/EN in `messages.yml` + DB-Feld (Command zum Wechseln ist deaktiviert) |
| Scoreboard | Map, Tokens, HighestLevel, K/D, Rang, Kopfgeld |
| Stats | MySQL + `/rank` / `/stats` |

**Eingeschränkt / kaputt:**

- `/setlanguage` steht in `plugin.yml`, Executor ist auskommentiert.
- Auto-Mapswitch-Timer ist als nicht funktionierend markiert.
- `game.points.remove` in der Config wird nicht verwendet.
- Multiverse-Core ist nur softdepend, **kein API-Code**.
- InstantLevel-Gadgets brauchen Level 25/45/65 — die Example-`level.yml` geht nur bis ~30.

---

## Commands

| Command | Permission (Code) | Funktion |
|---------|-------------------|----------|
| `/help` | `gungame.user.help` / `admin.help` | Hilfe |
| `/leave` (`l`, `verlassen`) | `user.leave` | Runde verlassen (nur Spawnschutz) |
| `/vote` `[Map]` | `user.vote` | Abstimmung starten oder voten |
| `/rank` (`stats`) | `user.rank` | Stats |
| `/kopfgeld` | `user.kopfgeld` | `menu` oder `aussetzen <Spieler> <Betrag> <Sekunden>` |
| `/team` | `user.team` | `einladen` / `annehmen` / `aufheben` |
| `/forcemap` | `admin.forcemap` | Mapwechsel mit Countdown |
| `/build` | `admin.build` | Creative auf Map |
| `/setlobby` | `admin.setlobby` | Lobby-Spawn |
| `/setshop` | `admin.setshop` | Shop-Villager |
| `/adminmenu` | `admin.adminmenu` | Level/Tokens setzen, adden, resetten |
| `/registermap` | `admin.registermap` | Neue Map (8 Argumente) |
| `/deletemap` | `admin.deletemap` | Map löschen |
| `/setbufflocation` | `admin.setbufflocation` | Auf Obsidian stehen, Location ID |
| `/createranking` | `admin.createranking` | Top 1–3 Armorstand |
| `/createlowerranking` | `admin.createlowerranking` | Holos 4–10 |
| `/setlanguage` | — | **nicht aktiv** |

In `plugin.yml` sind nur wenige Permissions definiert. Die meisten müssen manuell vergeben werden (`gungame.user.*` / `gungame.admin.*`). Schild schreiben: `gungame.admin.signwrite`.

---

## Gadgets (Shop)

| Gadget | Item | Kosten | Effekt |
|--------|------|--------|--------|
| Hook | Angel | 500 | Pull zum getroffenen Spieler |
| InstantLevel I–III | Gold/Eisen/Diamant | 2500 / 4500 / 6500 | setzt Level 25 / 45 / 65 |
| Shockwave | Schneeball | 500 | Knockback im Radius |
| Joe | Silberfisch-Ei | 500 | Mob jagt den Gegner |
| Backporter | Uhr | 500 | 3 s stillstehen → Spawn |

---

## Technik

| | |
|--|--|
| API | Paper 1.16.5 (Adventure Components) |
| Build | Maven, Artifact `KingGunGame` |
| Java | 8 |
| Soft-Depend | Multiverse-Core (Welten laden), HolographicDisplays |
| DB | MySQL 8 — Tabellen `kgmg_gungame`, `kgmg_gungame_gadgets`, `kgmg_gungame_kopfgeld` |
| Extra | Lombok, Authlib (Skulls) |

Voraussetzung: `kgmg_players` (KingBungeeCore). Default `connectionAllowed: false`.

**API:**

```java
GunGameAPI api = new GunGameAPI();
GunGamePlayer data = api.getCachedPlayerData(player);
api.updateScoreboard(player);
```

Config-Highlights: `points.add` (100), Vote-Zeiten, Prefix. Level-Sets in `level.yml` (`use: example`). Texte in `messages.yml` (DE/EN).

---

## Setup (kurz)

1. Paper 1.16.5 + HolographicDisplays.
2. MySQL + `kgmg_players`, `connectionAllowed: true`.
3. `/setlobby` → Maps `/registermap` → Join-Schild → `/setshop` → Rankings → Buffs auf Obsidian.
4. Permissions und ggf. eigene `level.yml` für hohe InstantLevel-Stufen.

Verwandte Plugins: **KingCoins** / **KINGEconGUI** (Token-Tausch), **databaseregistration**.
