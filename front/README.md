# Front Yoga App

Application Angular 19 du studio de yoga.

## Installation et lancement

Depuis le dossier `front` :

```bash
npm ci
npm start
```

L'application est disponible sur `http://localhost:4200`. Les requêtes `/api`
sont redirigées vers le back sur `http://localhost:8080`.

## Tests Jest

Exécuter les tests unitaires et d'intégration avec le rapport de couverture :

```bash
npm run test:coverage
```

Le seuil est fixé à 80 % pour les statements, branches, fonctions et lignes.
Le rapport HTML est disponible dans :

```text
coverage/jest/lcov-report/index.html
```

Résultats vérifiés :

```text
16 suites réussies sur 16
47 tests réussis sur 47
100 % statements, branches, fonctions et lignes
```

La suite comprend 20 tests unitaires et 27 tests d'intégration de composants,
soit 57,45 % de tests d'intégration. Ces derniers rendent les templates avec
`TestBed` et vérifient notamment les formulaires, les rôles utilisateur, la
navigation et les interactions avec les services substitués.

Pour exécuter les tests en continu :

```bash
npm run test:watch
```

## Tests E2E Cypress

Exécuter les tests E2E en mode headless et générer le rapport de couverture :

```bash
npm run e2e:test
```

Cette commande lance successivement l'application instrumentée, les scénarios
Cypress puis le contrôle des seuils Istanbul. Le rapport HTML est disponible
dans :

```text
coverage/lcov-report/index.html
```

Résultats vérifiés :

```text
3 spécifications réussies sur 3
18 scénarios réussis sur 18
```

| Indicateur | Couverture |
| --- | ---: |
| Statements | 96,17 % |
| Branches | 95,19 % |
| Fonctions | 96,10 % |
| Lignes | 95,80 % |

Si `ELECTRON_RUN_AS_NODE` est défini dans l'environnement Bash, le neutraliser
uniquement pour la commande Cypress :

```bash
env -u ELECTRON_RUN_AS_NODE npm run e2e:test
```

Pour ouvrir Cypress en mode interactif :

```bash
npm run e2e
```

## Build

```bash
npm run build
```

La sortie de production est générée dans `dist/yoga`.
