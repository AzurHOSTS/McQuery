# Contribuer à MCQuery

Merci de lire ce guide avant de soumettre une contribution.

---

## Table des matières
- [Code de conduite](#code-de-conduite)
- [Démarrage](#démarrage)
- [Convention de commits](#convention-de-commits)
- [Pull Requests](#pull-requests)
- [Signaler un bug](#signaler-un-bug)

---

## Code de conduite

Sois respectueux, constructif et inclusif. Toute forme de harcèlement sera immédiatement sanctionnée.

---

## Démarrage

1. Forke le dépôt
2. Clone ton fork : `git clone https://github.com/AzurHOSTS/McQuery/new/main`
3. Crée une branche : `git checkout -b feat/ma-fonctionnalite`
4. Effectue tes modifications
5. Commite en suivant la convention ci-dessous
6. Push et ouvre une Pull Request

---

## Convention de commits

Nous suivons la spécification de la conventions des commits.

### Format
```
<type>(<scope>): <description courte>

[corps optionnel]

[footer optionnel]
```

### Types

| Type       | Usage                                          |
|------------|------------------------------------------------|
| `feat`     | Nouvelle fonctionnalité                        |
| `fix`      | Correction de bug                              |
| `docs`     | Documentation uniquement                       |
| `refactor` | Refacto sans ajout de feature ni fix           |
| `test`     | Ajout ou modification de tests                 |
| `chore`    | Maintenance (dépendances, config, build)       |
| `perf`     | Amélioration de performance                    |
| `style`    | Formatage, espaces (aucun changement logique)  |

### Règles
- Description courte en **minuscules**, sans point final, max **72 caractères**
- Utiliser l'**impératif** : `add`, `fix`, `remove` (pas `added`, `fixed`)
- Le corps explique le **pourquoi**, pas le comment
- Un commit = **une seule chose**

> Utilise `!` après le scope et `BREAKING CHANGE:` dans le footer pour tout changement cassant.

---

## Pull Requests

- Cible la branche `dev`, **jamais** `main` directement
- Remplis entièrement le template de PR
- Vérifie que ton code compile et passe tous les tests avant de soumettre
- Les attributions aux mainteneurs doivent être conservées dans tous les fichiers (GPL v3 Section 7b)

---

## Licence

En contribuant, tu acceptes que tes contributions soient publiées sous la **GNU General Public License v3.0**.  
Copyright (C) 2026 AzurHOSTS.com Maintainer : RedSavant, OxiWan.
