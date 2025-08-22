```markdown
# API Villes

Cette API permet de gérer des villes et leurs départements, avec des opérations CRUD et des fonctionnalités
supplémentaires sur la population des villes.

## Base URL

```json

http://localhost:8080

```

## Endpoints

### Villes

| Action                                              | Méthode | URL                                   | Body / Params                                                                 | Description                                                        |
|-----------------------------------------------------|---------|---------------------------------------|-------------------------------------------------------------------------------|--------------------------------------------------------------------|
| Lister toutes les villes                            | GET     | `/villes`                             | Aucun                                                                         | Récupère toutes les villes avec leur département                   |
| Récupérer une ville par ID                          | GET     | `/villes/{id}`                        | id (path)                                                                     | Récupère une ville spécifique                                      |
| Ajouter une ville                                   | POST    | `/villes/add`                         | JSON Body : `{"nom": "Montpellier", "nbHabitants": 483000, "departement": {"code": "34"}}` | Ajoute une nouvelle ville                                          |
| Modifier une ville                                  | PUT     | `/villes/update/{id}`                 | JSON Body : `{"nom": "Montpellier_modifie", "nbHabitants": 483000, "departement": {"code": "34"}}` | Met à jour une ville existante                                     |
| Supprimer une ville                                 | DELETE  | `/villes/delete/{id}`                 | id (path)                                                                     | Supprime une ville par son ID                                      |
| N villes les plus peuplées d’un département        | GET     | `/villes/mostpopulated`               | Query params : `codeDepartement`, `n`<br>Ex : `/villes/mostpopulated?codeDepartement=01&n=10` | Retourne les N villes les plus peuplées d’un département           |
| Villes d’un département dans une fourchette de population | GET     | `/villes/rangedpopulated`             | Query params : `codeDepartement`, `min`, `max`<br>Ex : `/villes/rangedpopulated?codeDepartement=01&min=4000&max=483000` | Retourne les villes dont la population est comprise entre `min` et `max` |

## Conseils pour les tests

- Pour les requêtes POST et PUT, ajouter l’en-tête :  
  `Content-Type: application/json`
- Vérifier que le département existe ou qu’il sera créé automatiquement si inexistant.
- Pour GET avec query params, s’assurer de bien passer tous les paramètres requis (`codeDepartement`, `n` ou `min`/`max`).
- Pour tester les erreurs, essayer des valeurs invalides ou absentes (ex : `id` négatif, `codeDepartement` vide, population négative).

## Exemples

### Ajouter une ville

**POST** `/villes/add`

Body :
```json
{
  "nom": "Montpellier",
  "nbHabitants": 483000,
  "departement": { "code": "34" }
}
```

### Obtenir les 10 villes les plus peuplées du département 01

**GET** `/villes/mostpopulated?codeDepartement=01&n=10`

### Obtenir les villes entre 4000 et 500000 habitants dans le département 01

**GET** `/villes/rangedpopulated?codeDepartement=01&min=4000&max=500000`

```