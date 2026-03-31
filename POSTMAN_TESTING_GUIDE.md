# 🚀 Guide Complet de Test Postman - AppEvent API

## 📋 Configuration Initiale

### 1. Importer la Collection
- Téléchargez la collection Postman fournie
- Importez-la dans Postman : `File > Import`
- Configurez les variables d'environnement

### 2. Variables d'Environnement
Créez un environnement avec les variables suivantes :

```
baseUrl: http://localhost:8082
adminToken: {{votre_token_admin}}
userToken: {{votre_token_user}}
userId: {{id_utilisateur_cree}}
```

---

## 🔐 Sprint 1: Connexion Utilisateur

### 1.1 Connexion Admin
**Méthode**: POST  
**URL**: `{{baseUrl}}/api/auth/login`

**Headers**:
```
Content-Type: application/json
```

**Body (raw JSON)**:
```json
{
    "email": "admin@iset.tn",
    "password": "admin123"
}
```

**Réponse Attendue (200)**:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBpc2V0LnRuIiwi...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@iset.tn",
    "roles": ["ADMIN"]
}
```

**Actions**:
- Copiez le token reçu
- Mettez à jour la variable `adminToken` dans Postman

### 1.2 Test d'Accès non Autorisé
**Méthode**: GET  
**URL**: `{{baseUrl}}/api/users/profile`

**Headers**:
```
Content-Type: application/json
```

**Réponse Attendue (401)**:
```json
{
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource"
}
```

### 1.3 Accès avec Token
**Méthode**: GET  
**URL**: `{{baseUrl}}/api/users/profile`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (200)**:
```json
{
    "id": 1,
    "firstname": "Admin",
    "lastname": "System",
    "email": "admin@iset.tn",
    "phone": null,
    "roles": ["ADMIN"],
    "createdAt": "2024-01-01T10:00:00"
}
```

---

## 📝 Sprint 2: Inscription Utilisateur

### 2.1 Inscription d'un Nouveau Utilisateur
**Méthode**: POST  
**URL**: `{{baseUrl}}/api/auth/register`

**Headers**:
```
Content-Type: application/json
```

**Body (raw JSON)**:
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

**Réponse Attendue (200)**:
```json
{
    "id": 2,
    "firstname": "Jean",
    "lastname": "Dupont",
    "email": "jean.dupont@email.com",
    "phone": "12345678",
    "role": "PARTICIPANT",
    "createdAt": "2024-01-01T10:00:00",
    "message": "Utilisateur inscrit avec succès"
}
```

**Actions**:
- Copiez l'ID de l'utilisateur créé
- Mettez à jour la variable `userId` dans Postman

### 2.2 Test de Validation - Mots de Passe Différents
**Méthode**: POST  
**URL**: `{{baseUrl}}/api/auth/register`

**Body (raw JSON)**:
```json
{
    "firstname": "Marie",
    "lastname": "Martin",
    "email": "marie.martin@email.com",
    "password": "password123",
    "confirmPassword": "password456",
    "phone": "87654321"
}
```

**Réponse Attendue (400)**:
```
Les mots de passe ne correspondent pas
```

### 2.3 Test de Validation - Email Déjà Utilisé
**Méthode**: POST  
**URL**: `{{baseUrl}}/api/auth/register`

**Body (raw JSON)**:
```json
{
    "firstname": "Jean",
    "lastname": "Dupont2",
    "email": "jean.dupont@email.com",
    "password": "password123",
    "confirmPassword": "password123",
    "phone": "11223344"
}
```

**Réponse Attendue (400)**:
```
Email déjà utilisé
```

### 2.4 Connexion du Nouvel Utilisateur
**Méthode**: POST  
**URL**: `{{baseUrl}}/api/auth/login`

**Body (raw JSON)**:
```json
{
    "email": "jean.dupont@email.com",
    "password": "password123"
}
```

**Actions**:
- Copiez le token reçu
- Mettez à jour la variable `userToken` dans Postman

---

## 👨‍💼 Sprint 3: Gestion des Comptes Administrateur

### 3.1 Lister Tous les Utilisateurs
**Méthode**: GET  
**URL**: `{{baseUrl}}/api/users/all`

**Headers**:
```
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (200)**:
```json
[
    {
        "id": 1,
        "firstname": "Admin",
        "lastname": "System",
        "email": "admin@iset.tn",
        "phone": null,
        "roles": ["ADMIN"],
        "createdAt": "2024-01-01T10:00:00"
    },
    {
        "id": 2,
        "firstname": "Jean",
        "lastname": "Dupont",
        "email": "jean.dupont@email.com",
        "phone": "12345678",
        "roles": ["PARTICIPANT"],
        "createdAt": "2024-01-01T10:00:00"
    }
]
```

### 3.2 Voir les Statistiques des Utilisateurs
**Méthode**: GET  
**URL**: `{{baseUrl}}/api/users/statistics`

**Headers**:
```
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (200)**:
```json
{
    "totalUsers": 2,
    "adminCount": 1,
    "organisateurCount": 0,
    "participantCount": 1,
    "activeUsers": 2,
    "inactiveUsers": 0,
    "message": "Statistiques des utilisateurs"
}
```

### 3.3 Mettre à Jour un Utilisateur
**Méthode**: PUT  
**URL**: `{{baseUrl}}/api/users/{{userId}}`

**Headers**:
```
Authorization: Bearer {{adminToken}}
Content-Type: application/json
```

**Body (raw JSON)**:
```json
{
    "firstname": "Jean Michel",
    "lastname": "Dupont",
    "email": "jean.michel.dupont@email.com",
    "phone": "98765432",
    "role": "ORGANISATEUR"
}
```

**Réponse Attendue (200)**:
```json
{
    "id": 2,
    "firstname": "Jean Michel",
    "lastname": "Dupont",
    "email": "jean.michel.dupont@email.com",
    "phone": "98765432",
    "roles": ["ORGANISATEUR"],
    "updatedAt": "2024-01-01T10:00:00",
    "message": "Utilisateur mis à jour avec succès"
}
```

### 3.4 Changer le Rôle d'un Utilisateur
**Méthode**: PATCH  
**URL**: `{{baseUrl}}/api/users/{{userId}}/role`

**Headers**:
```
Authorization: Bearer {{adminToken}}
Content-Type: application/json
```

**Body (raw JSON)**:
```json
{
    "role": "PARTICIPANT",
    "reason": "Changement de rôle suite à demande utilisateur"
}
```

**Réponse Attendue (200)**:
```json
{
    "userId": 2,
    "email": "jean.michel.dupont@email.com",
    "newRole": "PARTICIPANT",
    "changedBy": "admin@iset.tn",
    "reason": "Changement de rôle suite à demande utilisateur",
    "changedAt": "2024-01-01T10:00:00",
    "message": "Rôle de l'utilisateur modifié avec succès"
}
```

### 3.5 Réinitialiser le Mot de Passe
**Méthode**: POST  
**URL**: `{{baseUrl}}/api/users/reset-password`

**Headers**:
```
Authorization: Bearer {{adminToken}}
Content-Type: application/json
```

**Body (raw JSON)**:
```json
{
    "email": "jean.michel.dupont@email.com",
    "newPassword": "newpassword123",
    "confirmPassword": "newpassword123",
    "adminReason": "Réinitialisation suite à oubli du mot de passe"
}
```

**Réponse Attendue (200)**:
```json
{
    "userId": 2,
    "email": "jean.michel.dupont@email.com",
    "resetBy": "admin@iset.tn",
    "reason": "Réinitialisation suite à oubli du mot de passe",
    "resetAt": "2024-01-01T10:00:00",
    "message": "Mot de passe réinitialisé avec succès"
}
```

### 3.6 Désactiver un Utilisateur
**Méthode**: PATCH  
**URL**: `{{baseUrl}}/api/users/{{userId}}/toggle-status`

**Headers**:
```
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (200)**:
```json
{
    "userId": 2,
    "email": "jean.michel.dupont@email.com",
    "status": "Désactivé",
    "changedBy": "admin@iset.tn",
    "changedAt": "2024-01-01T10:00:00",
    "message": "Statut de l'utilisateur modifié avec succès"
}
```

### 3.7 Réactiver un Utilisateur
**Méthode**: PATCH  
**URL**: `{{baseUrl}}/api/users/{{userId}}/toggle-status`

**Headers**:
```
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (200)**:
```json
{
    "userId": 2,
    "email": "jean.michel.dupont@email.com",
    "status": "Activé",
    "changedBy": "admin@iset.tn",
    "changedAt": "2024-01-01T10:00:00",
    "message": "Statut de l'utilisateur modifié avec succès"
}
```

### 3.8 Supprimer un Utilisateur
**Méthode**: DELETE  
**URL**: `{{baseUrl}}/api/users/{{userId}}`

**Headers**:
```
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (200)**:
```json
{
    "deletedUserId": 2,
    "deletedEmail": "jean.michel.dupont@email.com",
    "deletedUserName": "Jean Michel Dupont",
    "deletedBy": "admin@iset.tn",
    "deletedAt": "2024-01-01T10:00:00",
    "message": "Utilisateur supprimé avec succès"
}
```

---

## 🚨 Tests d'Erreur et Sécurité

### 4.1 Tentative de Suppression d'un Admin
**Méthode**: DELETE  
**URL**: `{{baseUrl}}/api/users/1`

**Headers**:
```
Authorization: Bearer {{adminToken}}
```

**Réponse Attendue (400)**:
```
Impossible de supprimer un administrateur
```

### 4.2 Accès Non Autorisé aux Fonctions Admin
**Méthode**: GET  
**URL**: `{{baseUrl}}/api/users/all`

**Headers**:
```
Authorization: Bearer {{userToken}}
```

**Réponse Attendue (403)**:
```json
{
    "error": "Forbidden",
    "message": "Access is denied"
}
```

### 4.3 Validation des Champs
**Méthode**: PUT  
**URL**: `{{baseUrl}}/api/users/{{userId}}`

**Headers**:
```
Authorization: Bearer {{adminToken}}
Content-Type: application/json
```

**Body (raw JSON)**:
```json
{
    "firstname": "",
    "lastname": "",
    "email": "email-invalide",
    "phone": "123"
}
```

**Réponse Attendue (400)**:
```json
{
    "firstname": "Le prénom est obligatoire",
    "lastname": "Le nom est obligatoire",
    "email": "Email invalide",
    "phone": "Le téléphone doit contenir 8 chiffres"
}
```

---

## 📊 Scénarios de Test Complets

### Scénario 1: Workflow Complet
1. **Connexion admin** → Obtenir token admin
2. **Inscription nouvel utilisateur** → Créer compte participant
3. **Connexion nouvel utilisateur** → Obtenir token utilisateur
4. **Lister tous les utilisateurs** (admin) → Voir la liste
5. **Mettre à jour l'utilisateur** (admin) → Changer infos
6. **Changer le rôle** (admin) → Promouvoir en organisateur
7. **Réinitialiser mot de passe** (admin) → Nouveau mot de passe
8. **Désactiver le compte** (admin) → Désactivation
9. **Réactiver le compte** (admin) → Réactivation
10. **Supprimer l'utilisateur** (admin) → Suppression finale

### Scénario 2: Tests de Sécurité
1. **Accès sans token** → 401 Unauthorized
2. **Accès avec token invalide** → 401 Unauthorized
3. **Accès utilisateur aux fonctions admin** → 403 Forbidden
4. **Tentative de suppression admin** → 400 Bad Request
5. **Validation des données** → Messages d'erreur appropriés

---

## 🔧 Dépannage

### Problèmes Courants
1. **Token expiré**: Reconnectez-vous pour obtenir un nouveau token
2. **CORS**: Vérifiez que le frontend est autorisé
3. **Base de données**: Assurez-vous que PostgreSQL est démarré
4. **Port**: Vérifiez que l'application tourne sur le port 8082

### Vérification de l'Application
```bash
# Démarrer l'application
mvn spring-boot:run

# Vérifier les logs
curl http://localhost:8082/api/auth/test
```

---

## ✅ Checklist de Validation

- [ ] Connexion admin fonctionnelle
- [ ] Inscription utilisateur avec validation
- [ ] Gestion complète des utilisateurs par admin
- [ ] Sécurité des endpoints respectée
- [ ] Messages d'erreur clairs
- [ ] Audit des actions administratives
- [ ] Tests d'intégration passés

**🎉 Tous les sprints sont maintenant complètement développés et testables !**
