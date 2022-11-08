# AMT-team-06 - AWS-find-label

Auteurs : Maëlle Vogel, Olivier Tissot-Daguette

## Collaborateurs

Enseignant : Nicolas Glassey

Assistant : Adrien Allemand

## Utilisation du projet

### Utilisation de Maven

Pour ce projet, l'utilisation de Maven est nécessaire, il faut donc l'installer. Lien vers la documentation officielle pour installer Maven : https://maven.apache.org/install.html

Après l'installation de Maven, il faut aussi installer les différentes dépendances nécessaires à l'aide de la commande : ```mvn clean install```

Pour créer un package du projet avec maven-assembly, il faut utiliser la commande ```mvn package``` qui créera un .jar dans le dossier ```target``` qui se trouve à la racine du projet.

Il est ensuite possible de lancer l'application via la commande ```java -jar AwsApp.jar HEIG.vd.Main```.

Pour l'instant le .jar effectue une suite de commande prédéfinie :
- On se connecte à AWS
- Création d'un objet
- Utilisatoin de rekognition pour analyser l'image disponible dans le dossier filesTest
- Affichage des labels
- Suppression de l'objet image créé et de l'objet de résultat (contenant le json) créé (pour permettre de relancer plusieurs fois le .jar)

## Utilisation de l'application sur l'architecture fournie

Voici les commandes à exécuter sur la machine debian:

```
cd app/
java -jar awsApp.jar
```

### AWS

Afin de pouvoir utiliser les services de AWS (AWS S3 et AWS Rekognition Label) nous avons décidé d'utiliser un fichier d'environnement pour load les credentials d'AWS. Il faut donc avoir un fichier ```.env``` à la racine du repo avec les variables ```AWS_ACCESS_KEY``` et ```AWS_SECRET_KEY``` contenant respectivement la clé d'accès et la clé d'accès secrète.

En ce qui concerne la configuration nous avons utilisé comme région ```eu-west-2``` et le format ```json``` pour l'envoi des données.

### Tests

Nous utilisons JUnit pour tester notre programme, il est possible de lancer les tests automatiquement à l'aide de Maven en utilisant la commande ```mvn test```.

Nous avons mis en pratique une approche BDD (given, when, then) pour l'écriture des tests.

### Annexes

Un wiki est disponible dans le repository avec ces différentes rubriques :
- stratégie de branche
- convention de nommage
- choix des technologies
- bibliographie
