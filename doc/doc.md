# 1. Présentation du projet

***Décrivez la solution proposée. Incluez le public cible et les avantages commerciaux de la solution.***

Notre solution permet de rendre simple et ludique les visites touristiques.

En effet, TourGuide propose des attractions, des lieux touristiques et des billets de spectacles 
proche de votre position actuelle. En allant à ces attractions, les utilisateurs gagnent des réductions
sur leurs prochaines visites.

Grâce à notre réseau d'agence de voyage, nous proposons des séjours et activités qui correspondent
à tous les critères des clients. (Nombres d'enfants, Prix, Durée du séjour, ...)

## 1.1 Objectifs du projet

***Décrivez les objectifs du projet (2 à 3 phrases), y compris le(s) problème(s) résolu(s).***

Suite à une importante augmentation du nombre de utilisateurs, l'ancienne architecture
ne pouvait plus supporter le grand nombre de requêtes.

Les principaux objectifs ont été de supporter les accès concurrents aux différentes données et
le changement d'architecture. Nous avons opté pour une séparation des différentes parties
de l'application avec des micro-services. 

Le dernier objectif était d'utiliser Docker, qui est maintenant la norme pour ce type de projet.
L'application a été séparée en 4 parties distinctes, qui communiquent entre elles.
 
## 1.2 Hors du champ d’application

***Découvrez les objectifs qui ont été envisagés, mais ne sont pas couverts par ce projet.***
 
Nous avons envisagé de passer le code existant en asynchrone. En effet, le potentiel gain en
performance peut être considérable mais cela nécessite de refondre une très grande partie de
la base de code actuelle. Le problème est que l'asynchrone fonctionne mal si le projet est un
mixe entre du code sync/async.
En finalement, Java n'est pas le langage de choix pour ce type de problème.

(...)

## 1.3 Mesures du projet

***Indiquez comment vous allez mesurer le succès du projet.***

### 1.3.1 Mesurer le succès commerciale

Les principaux facteurs à surveiller sont :
 - Le nombre d'utilisateur récurents (qui visitent au moins une attractions tous les mois)
 - Le nombre de nouvels inscrits par mois
 - Le pourcentage des nouvels inscrits qui deviennent des utilisateurs récurrents.

### 1.3.2 Mesurer le succès technique
 
Les différents problèmes ont été des erreurs de concurrences d'accès
et une lenteur quand le service recevait trop de requêtes.

Pour les erreurs de concurrences, il faut bien gérer les erreurs et les enregistrer (avec le contexte).
Cela permettra de corriger les derniers points critiques. Si aucune erreur de concurrence n'apparaît
en 1 mois, alors nous pouvons conclure que le code est robuste à tous les cas, notamment :
 - nombre d'utilisateurs simultanés
 - écriture et lecture de la même donnée dans plusieurs requêtes.

Pour les problèmes de performances, il faut tracer un graphique chaque semaine avec au cours du temps :
 - le nombre d'utilisateurs connectés
 - le nombre de requêtes par minutes
 - les temps de réponse moyen

Le temps de réponse moyen ne doit pas faire de **"pics"** et doit rester cohérents avec le 
nombre de requêtes et d'utilisateurs.

Il est surement nécessaire de comparer ces graphiques entres différentes versions de l'applications.
Cela permet de mesurer l'impact des nouveaux changements.

# 2. Caractéristiques
 
Faites figurer ici une liste de fonctionnalités, de user stories et de critères d'acceptation.
 
 
# 3. Solution proposée

## 3.1 Schémas de conception technique
 
 
## 3.2 Glossaire
 
Tout le vocabulaire du domaine se trouve ici.
## 3.3 Spécifications techniques
Expliquez quelles technologies et quels langages peuvent être utilisés comme solution.
 
## 3.4 Solutions alternatives
Expliquez les autres options envisagées pour la solution et pourquoi elles n’ont pas été choisies.
 
 
## 3.5 Calendrier prévisionnel et exigences
