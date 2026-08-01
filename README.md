# Yoga App

Application de réservation pour un studio de yoga, composée d'un front Angular
19 et d'une API REST Spring Boot 3 sécurisée par JWT.

## Structure du dépôt

```text
msm-projet-04-fullstack/
├── front/  Application Angular, tests Jest et scénarios Cypress
└── back/   API Spring Boot, tests JUnit/Mockito et configuration MySQL
```

## Prérequis

- Node.js 22 et npm 10
- Angular CLI 19, facultatif pour les commandes `npm` du projet
- Java 21
- Maven 3.9.3 ou supérieur
- Docker, Docker Compose et Docker Desktop

## Installation

```bash
git clone https://github.com/msm-oc-projects/msm-projet-04-fullstack.git
cd msm-projet-04-fullstack
```

Toutes les commandes suivantes partent de la racine du dépôt. Installer les
dépendances verrouillées du front :

```bash
cd front
npm ci
cd ..
```

Les dépendances Maven du back sont téléchargées automatiquement lors de la
première commande Maven.

## Configuration du back

Le fichier `back/.env` fourni contient la configuration locale utilisée par
Docker Compose et l'application :

```dotenv
DB_USER=user_test
DB_PASSWORD=test_password
DB_HOST=localhost
DB_PORT=3306
DB_NAME=test
TOKEN_SECRET=une-cle-secrete
```

Ces valeurs sont réservées au développement. Le mot de passe MySQL et la clé
JWT doivent être remplacés par des secrets robustes hors de l'environnement
local et ne doivent pas être réutilisés en production.

Docker Desktop doit être démarré avant le lancement du back.

## Lancement

Dans un premier terminal :

```bash
cd back
mvn spring-boot:run
```

Spring Boot démarre l'API sur `http://localhost:8080`, initialise le conteneur
MySQL décrit dans `back/compose.yaml` et lui attribue automatiquement son port
local.

Dans un second terminal :

```bash
cd front
npm start
```

L'application est disponible sur `http://localhost:4200`.

Les appels du front commençant par `/api` sont transmis à
`http://localhost:8080` par `front/proxy.conf.json`. Arrêter chaque serveur avec
`Ctrl+C` ; le conteneur MySQL géré par Spring Boot est alors arrêté.

## Synthèse des tests

| Suite | Tests | Statements / instructions | Branches | Fonctions / méthodes | Lignes |
| --- | ---: | ---: | ---: | ---: | ---: |
| Jest front | 47 | 100 % | 100 % | 100 % | 100 % |
| Cypress E2E | 18 | 96,17 % | 95,19 % | 96,10 % | 95,80 % |
| JUnit/Mockito back | 69 | 92,39 % | 96,88 % | 87,96 % | 92,37 % |

Chaque indicateur dépasse le seuil obligatoire de 80 %. Les commandes de
couverture échouent automatiquement si un seuil n'est plus respecté.

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

La suite contient 47 tests : 20 tests unitaires et 27 tests d'intégration de
composants. Les tests d'intégration représentent donc 57,45 % du total, au-delà
du minimum demandé de 30 %. Ils vérifient ensemble les composants, leurs
templates, les formulaires, la navigation et leurs interactions avec des
services substitués.

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
| Statements | 96,17 % |
| Branches | 95,19 % |
| Fonctions | 96,10 % |
| Lignes | 95,80 % |

Les trois fichiers de spécifications contiennent 18 scénarios E2E, tous
réussis. Ils couvrent les écrans d'authentification, les sessions, le compte et
la page 404, ainsi que les variantes administrateur et utilisateur.

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

Sous Bash, l'équivalent est :

```bash
env -u ELECTRON_RUN_AS_NODE npm run e2e:test
```

## Tests du back

Exécuter les tests unitaires Mockito, les tests d'intégration Spring MVC,
générer le rapport JaCoCo et contrôler les seuils :

```bash
cd back
mvn clean verify
```

Le seuil global est fixé à 80 % pour les instructions, branches, lignes et
méthodes. Le package `dto` est exclu du calcul. Le rapport HTML est généré dans :

```text
back/target/site/jacoco/index.html
```

Résultats vérifiés :

| Indicateur | Couverture |
| --- | ---: |
| Instructions | 92,39 % |
| Branches | 96,88 % |
| Lignes | 92,37 % |
| Méthodes | 87,96 % |

La suite back contient 69 tests, dont 22 tests d'intégration MockMvc, soit
31,88 %. Le package `dto` est exclu du rapport et ne possède aucune classe de
test dédiée.

## Build du front

```bash
cd front
npm run build
```

La sortie de production est générée dans `front/dist/yoga`.

## Build du back

```bash
cd back
mvn clean package
```

Le JAR exécutable est généré dans `back/target/`.

## Résolution des problèmes courants

- Vérifier `java -version` et `mvn -version` si Maven n'utilise pas Java 21.
- Vérifier que Docker Desktop est démarré si le back ne peut pas créer MySQL.
- Vérifier que les ports `4200` et `8080` sont libres avant le lancement.
- Si Cypress interprète ses options comme celles de Node, supprimer
  `ELECTRON_RUN_AS_NODE` uniquement pour la commande concernée avec
  `env -u ELECTRON_RUN_AS_NODE npm run e2e:test` sous Bash ou
  `$env:ELECTRON_RUN_AS_NODE=$null` sous PowerShell.
- Les rapports sont régénérés par les commandes de couverture ; il n'est pas
  nécessaire de les modifier manuellement.
