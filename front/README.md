# Front Yoga App

Application Angular 19 du studio de yoga.

## Installation et lancement

Depuis le dossier `front` :

```bash
npm install
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

Pour ouvrir Cypress en mode interactif :

```bash
npm run e2e
```

## Build

```bash
npm run build
```

La sortie de production est générée dans `dist/yoga`.
