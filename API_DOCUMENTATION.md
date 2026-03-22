# Documentation API - Authentification AppEvent

## 📋 Endpoints d'Authentification

### 1. Inscription (Registration)
**POST** `/api/auth/register`

**Request Body:**
```json
{
    "firstname": "Jean",
    "lastname": "Dupont", 
    "email": "jean.dupont@email.com",
    "password": "password123",
    "confirmPassword": "password123",
    "phone": "12345678",
    "role": "PARTICIPANT"
}
```

**Response (201):**
```json
{
    "id": 1,
    "firstname": "Jean",
    "lastname": "Dupont",
    "email": "jean.dupont@email.com",
    "phone": "12345678",
    "role": "PARTICIPANT",
    "createdAt": "2024-01-01T10:00:00",
    "message": "Utilisateur inscrit avec succès"
}
```

**Rôles disponibles:**
- `PARTICIPANT` (par défaut si non spécifié)
- `ORGANISATEUR`
- `ADMIN`

### 2. Connexion (Login)
**POST** `/api/auth/login`

**Request Body:**
```json
{
    "email": "jean.dupont@email.com",
    "password": "password123"
}
```

**Response (200):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZWFuLmR1cG9udEBlbWFpbC5jb20i...",
    "type": "Bearer",
    "id": 1,
    "email": "jean.dupont@email.com",
    "roles": ["PARTICIPANT"]
}
```

### 3. Déconnexion (Logout)
**POST** `/api/auth/logout`

**Response (200):**
```json
{
    "message": "Déconnexion réussie",
    "success": true
}
```

## 🔐 Endpoints Sécurisés (Nécessitent un Token JWT)

### 4. Redirection après connexion
**GET** `/api/redirect/after-login`

**Headers:**
```
Authorization: Bearer <votre-token-jwt>
```

**Response (200):**
```json
{
    "redirect": "/participant/events",
    "role": "PARTICIPANT", 
    "message": "Redirection vers la consultation des événements",
    "user": {
        "id": 1,
        "firstname": "Jean",
        "lastname": "Dupont",
        "email": "jean.dupont@email.com"
    }
}
```

**Redirections selon le rôle:**
- `ADMIN` → `/admin/dashboard`
- `ORGANISATEUR` → `/organisateur/events`
- `PARTICIPANT` → `/participant/events`

### 5. Tableau de bord
**GET** `/api/events/dashboard`

**Response (200):**
```json
{
    "message": "Bienvenue sur votre tableau de bord",
    "user": {
        "id": 1,
        "email": "jean.dupont@email.com",
        "firstname": "Jean",
        "lastname": "Dupont",
        "roles": ["PARTICIPANT"]
    },
    "features": [
        "Consultation des événements",
        "Inscription aux événements", 
        "Profil personnel"
    ]
}
```

### 6. Profil utilisateur
**GET** `/api/users/profile`

**Response (200):**
```json
{
    "id": 1,
    "firstname": "Jean",
    "lastname": "Dupont",
    "email": "jean.dupont@email.com",
    "phone": "12345678",
    "roles": ["PARTICIPANT"],
    "createdAt": "2024-01-01T10:00:00"
}
```

## 🚨 Messages d'Erreur

### Erreur de validation (400)
```json
"Les mots de passe ne correspondent pas"
```

### Email déjà utilisé (400)
```json
"Email déjà utilisé"
```

### Identifiants invalides (400)
```json
"Invalid email or password"
```

### Accès non autorisé (401)
```json
{
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource"
}
```

## 🔧 Utilisation du Token JWT

1. **Récupérez le token** après connexion
2. **Ajoutez-le aux headers** pour les requêtes sécurisées:
   ```
   Authorization: Bearer <votre-token-jwt>
   ```

3. **Durée de vie**: 24 heures
4. **Déconnexion**: Supprimez simplement le token côté client

## 📊 Structure de la Base de Données

### Table `users`
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(120) NOT NULL,
    phone VARCHAR(8),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 🎯 Workflow d'Authentification

1. **Inscription**: Formulaire avec validation email unique et confirmation mot de passe
2. **Connexion**: Vérification identifiants + génération token JWT
3. **Redirection**: Selon le rôle de l'utilisateur
4. **Accès**: Token JWT requis pour les fonctionnalités avancées
5. **Déconnexion**: Suppression token côté client
