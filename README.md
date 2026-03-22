# AppEvent - Application de Gestion d'Événements

Projet Spring Boot pour l'authentification avec JWT et gestion des rôles.

## 🚀 Fonctionnalités

- **Authentification JWT** sécurisée
- **Gestion des rôles** : ADMIN, ORGANISATEUR, PARTICIPANT
- **Base de données PostgreSQL** avec création automatique
- **API REST** pour l'authentification
- **Compte admin par défaut** prêt à l'emploi

## 📋 Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ, Eclipse, VS Code)

## 🛠️ Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd AppEvent
```

### 2. Configurer PostgreSQL

**Option 1 : Configuration automatique (recommandée)**

Exécutez le script SQL fourni pour créer la base de données et l'utilisateur :

```bash
# Connectez-vous à PostgreSQL en tant que superutilisateur
psql -U postgres

# Exécutez le script de configuration
\i database-setup.sql
```

**Option 2 : Configuration manuelle**

```sql
-- Connectez-vous à PostgreSQL
CREATE DATABASE iset_events_db;
CREATE USER iset_events_db WITH PASSWORD 'maram';
GRANT ALL PRIVILEGES ON DATABASE iset_events_db TO iset_events_db;
```

Assurez-vous que PostgreSQL est installé et en cours d'exécution sur localhost:5432.

### 3. Compiler et lancer

```bash
# Compiler le projet
mvn clean compile

# Lancer l'application
mvn spring-boot:run
```

L'application démarrera sur `http://localhost:8082`

## 🔐 Authentification

### Compte Admin par défaut

- **Email** : `admin@iset.tn`
- **Mot de passe** : `admin123`

### API d'Authentification

#### POST /api/auth/login

**Request Body :**
```json
{
    "email": "admin@iset.tn",
    "password": "admin123"
}
```

**Response :**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@iset.tn",
    "roles": ["ADMIN"]
}
```

#### Utilisation du Token

Ajoutez le token dans les en-têtes HTTP pour les requêtes sécurisées :

```
Authorization: Bearer <votre-token-jwt>
```

## 📊 Structure du Projet

```
src/main/java/tn/iset/AppEvent/
├── AppEventApplication.java     # Classe principale
├── config/
│   └── DataInitializer.java     # Initialisation des données
├── controller/
│   └── AuthController.java      # Controller authentification
├── dto/
│   ├── LoginRequest.java        # DTO requête login
│   └── LoginResponse.java       # DTO réponse login
├── model/
│   ├── Role.java               # Énumération des rôles
│   └── User.java               # Entité utilisateur
├── repository/
│   └── UserRepository.java     # Repository JPA
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityConfig.java     # Configuration sécurité
└── service/
    └── AuthService.java         # Service authentification
```

## 🔧 Configuration

La configuration se trouve dans `src/main/resources/application.yml` :

- **Port serveur** : 8082
- **Base de données** : PostgreSQL sur localhost:5432
- **Nom de la BDD** : iset_events_db
- **Secret JWT** : Configurable dans `jwt.secret`
- **Expiration JWT** : 24 heures

## 🧪 Tests

```bash
# Lancer les tests
mvn test

# Tests avec coverage
mvn clean test jacoco:report
```

## 📝 Notes importantes

- La base de données se crée automatiquement au premier démarrage
- Les tables sont créées avec `ddl-auto: create-drop` (pour le développement)
- Le compte admin est créé automatiquement s'il n'existe pas
- Les mots de passe sont hashés avec BCrypt

## 🤝 Contribuer

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/amazing-feature`)
3. Commit les changements (`git commit -m 'Add amazing feature'`)
4. Push vers la branche (`git push origin feature/amazing-feature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT.
