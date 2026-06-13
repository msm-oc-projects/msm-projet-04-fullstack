# Yoga App

Application full-stack composée d'un front Angular 19 et d'une API Spring Boot 3.

## Prérequis

- Node.js et npm
- Angular CLI 19
- Java 21
- Maven 3.9.3 ou supérieur
- Docker, Docker Compose et Docker Desktop

## Installation

```bash
git clone https://github.com/msm-oc-projects/msm-projet-04-fullstack.git
cd msm-projet-04-fullstack
```

Installer les dépendances du front :

```bash
cd front
npm install
cd ..
```

Les dépendances Maven du back sont téléchargées automatiquement lors de la
première commande Maven.

## Configuration du back

Le fichier `back/.env` contient les variables nécessaires à MySQL et au JWT :

```dotenv
DB_USER=user_test
DB_PASSWORD=test_password
DB_HOST=localhost
DB_PORT=3306
DB_NAME=test
TOKEN_SECRET=une-cle-secrete
```

Docker Desktop doit être démarré avant le lancement du back.

## Lancement

Dans un premier terminal :

```bash
cd back
mvn spring-boot:run
```

Spring Boot démarre l'API sur `http://localhost:8080` et initialise le conteneur
MySQL avec Docker Compose.

Dans un second terminal :

```bash
cd front
npm start
```

L'application est disponible sur `http://localhost:4200`.

## Tests Jest du front

Exécuter les tests unitaires et d'intégration et générer la couverture :

```bash
cd front
npm run test:coverage
```

Le seuil global est fixé à 80 % pour les statements, branches, fonctions et
lignes. Le rapport HTML est généré dans :

```text
front/coverage/jest/lcov-report/index.html
```

Résultats vérifiés :

| Indicateur | Couverture |
| --- | ---: |
| Statements | 100 % |
| Branches | 100 % |
| Fonctions | 100 % |
| Lignes | 100 % |

## Tests E2E Cypress

Exécuter les scénarios Cypress en mode headless, collecter la couverture puis
générer le rapport :

```bash
cd front
npm run e2e:test
```

Les appels API sont interceptés par Cypress afin de tester les parcours du
front de manière déterministe. Le rapport HTML est généré dans :

```text
front/coverage/lcov-report/index.html
```

Résultats vérifiés :

| Indicateur | Couverture |
| --- | ---: |
| Statements | 96,72 % |
| Branches | 96,15 % |
| Fonctions | 96,10 % |
| Lignes | 96,40 % |

Pour ouvrir Cypress en mode interactif :

```bash
cd front
npm run e2e
```

Sous PowerShell, si `ELECTRON_RUN_AS_NODE` vaut `1`, la désactiver uniquement
dans le terminal courant avant de lancer Cypress :

```powershell
$env:ELECTRON_RUN_AS_NODE=$null
npm run e2e:test
```

## Tests du back

Exécuter les tests unitaires Mockito, les tests d'intégration Spring MVC,
générer le rapport JaCoCo et contrôler les seuils :

```bash
cd back
mvn verify
```

Le seuil global est fixé à 80 % pour les instructions, branches, lignes et
méthodes. Le package `dto` est exclu du calcul. Le rapport HTML est généré dans :

```text
back/target/site/jacoco/index.html
```

Résultats vérifiés :

| Indicateur | Couverture |
| --- | ---: |
| Instructions | 92,78 % |
| Branches | 96,88 % |
| Lignes | 92,37 % |
| Méthodes | 88,89 % |

La suite back contient 69 tests, dont 22 tests d'intégration, soit 31,88 %.

## Build du front

```bash
cd front
npm run build
```

La sortie de production est générée dans `front/dist/yoga`.
