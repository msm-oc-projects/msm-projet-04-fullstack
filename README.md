# Yoga App

Application full-stack composée d'un front Angular 19 et d'une API Spring Boot 3.

## Prérequis

- Node.js et npm
- Angular CLI 19
- Java 21
- Maven 3.9.3 ou supérieur
- Docker, Docker Compose et Docker Desktop

## Installation

Cloner le dépôt puis installer les dépendances du front :

```bash
git clone https://github.com/msm-oc-projects/msm-projet-04-fullstack.git
cd msm-projet-04-fullstack/front
npm install
```

Les dépendances Maven du back sont téléchargées automatiquement à sa première
exécution.

## Lancement

Dans un premier terminal, démarrer Docker Desktop puis l'API :

```bash
cd back
mvn spring-boot:run
```

L'API et sa base MySQL sont disponibles sur le port `8080`.

Dans un second terminal, démarrer Angular :

```bash
cd front
npm start
```

L'application est disponible sur `http://localhost:4200`.

## Tests du front

Tests unitaires et d'intégration Jest avec contrôle du seuil de 80 % :

```bash
cd front
npm run test:coverage
```

Le rapport HTML Jest est généré dans
`front/coverage/jest/lcov-report/index.html`.

Tests E2E Cypress en mode headless avec API simulée, puis génération du rapport :

```bash
cd front
npm run e2e:test
```

Le rapport HTML E2E est généré dans `front/coverage/lcov-report/index.html`.

Si Cypress ne démarre pas sous PowerShell parce que la variable
`ELECTRON_RUN_AS_NODE` vaut `1`, la désactiver uniquement dans le terminal courant :

```powershell
$env:ELECTRON_RUN_AS_NODE=$null
npm run e2e:test
```

Pour ouvrir Cypress en mode interactif :

```bash
npm run e2e
```

## Tests du back

Les tests JUnit et Mockito ainsi que le contrôle JaCoCo sont exécutés avec :

```bash
cd back
mvn verify
```

Le rapport HTML JaCoCo est généré dans
`back/target/site/jacoco/index.html`. Le package DTO est exclu de la couverture.

## Build

```bash
cd front
npm run build
```

La sortie de production Angular est générée dans `front/dist/yoga`.
