# AMT-team-06 - AWS-find-label

Auteurs : Maëlle Vogel, Olivier Tissot-Daguette

## Collaborateurs

Enseignant : Nicolas Glassey

Assistant : Adrien Allemand

## Utilisation du projet

Tout d'abord il vous faudra :

- java version 17 (disponible ici : https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Maven version 3.8 (disponible ici : https://maven.apache.org/download.cgi)

### Utilisation de Maven

Après avoir clone le repository, il faut installer les différentes dépendances via la commande : ```mvn clean install```

Pour créer un package du projet avec maven-assembly, il faut utiliser la commande ```mvn package``` qui créera un .jar dans le dossier ```target``` qui se trouve à la racine du projet.

Pour l'instant le .jar effectue une suite de commande prédéfinie :
- On se connecte à AWS
- Création d'un objet à partir d'une image qui se trouve dans le dossier ```filesTest``` et qui s'appel ```file1.jpg```
- Affichage de l'url de l'image disponible pendant 10min (mais comme nous supprimons l'objet après coup, ce n'est pas possible d'y accéder depuis là...)
- Utilisatoin de rekognition pour analyser l'image disponible dans le dossier filesTest
- Affichage des labels (une fois depuis la commande rekognition qui retourne les labels et une fois avec une récupération de l'objet qui contient le json)
- Suppression de l'objet de l'image créé et de l'objet de résultat (contenant le json) créé (pour permettre de relancer plusieurs fois le .jar)

Afin de lancer le .jar sans encombre, voici à quoi doit ressembler le répertoire :

```
.
├── .env        # fichier contenant les variables d'environnement nécessaire
├── awsApp.jar
├── filesTest
    └─── file1.jpg
```

### AWS

Afin de pouvoir utiliser les services de AWS (AWS S3 et AWS Rekognition Label) nous avons décidé d'utiliser un fichier d'environnement pour load les credentials d'AWS. 
Il faut donc avoir un fichier ```.env``` à la racine du projet qui doit ressembler à cela :
```
AWS_ACCESS_KEY=...
AWS_SECRET_KEY=...
REGION=...
BUCKET=...
```

Il est aussi nécessaire d'avoir un utilisateur IAM possédant ces droits :

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            //Permission de lister tous les data objects de notre organisation
            "Effect": "Allow",
            "Action": "s3:ListAllMyBuckets",
            "Resource": "arn:aws:s3:::*"
        },
        {
            //Permission totale sur les data objects respectant cette nomenclature
            //[XX] Etant la référence de votre équipe
            "Effect": "Allow",
            "Action": "s3:*",
            "Resource": [
                "arn:aws:s3:::amt.team[XX].diduno.education",
                "arn:aws:s3:::amt.team[XX].diduno.education/*"
            ]
        }
    ]
}

```

### Tests

Nous utilisons JUnit pour tester notre programme, il est possible de lancer les tests automatiquement à l'aide de Maven en utilisant la commande ```mvn test```.

Nous avons mis en pratique une approche BDD (given, when, then) pour l'écriture des tests.

### Annexes

Un wiki est disponible dans le repository avec ces différentes rubriques :
- stratégie de branche
- convention de nommage
- choix des technologies
- bibliographie

## Utilisation du .jar dans une instance Amazone EC2

Voici les commandes à exécuter sur la machine debian:

```
cd app/
java -jar awsApp.jar
```