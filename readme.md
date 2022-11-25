# AMT-team-06 - AWS-find-label

Auteurs : Maëlle Vogel, Olivier Tissot-Daguette

## Collaborateurs

Enseignant : Nicolas Glassey

Assistant : Adrien Allemand

## Utilisation du projet

Tout d'abord il vous faudra :

- java version 17 (disponible [ici](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- Maven version 3.8 (disponible [ici](https://maven.apache.org/download.cgi))

### Utilisation de Maven

Clone le projet en locale puis, afin de récupérer les dépendances utiliser la commande : ```mvn dependency:resolve``` à la racine du projet.

Avant d'installer le projet il est nécessaire d'avoir un fichier `.env` à la racine du projet, voici les champs qu'il doit contenir :

```
AWS_ACCESS_KEY_ID =
AWS_SECRET_ACCESS_KEY =
REGION = 
BUCKET =
URL_DURATION =                  # Durée en minutes
```

Pour la région, il faut qu'elle corresponde à la colonne `Region` du tableau disponible [ici](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.RegionsAndAvailabilityZones.html#Concepts.RegionsAndAvailabilityZones.Regions).

Le fichier nommé `.env.tmp`, contient un exemple du `.env` nécessaire.

Ensuite on peut installer le projet en utilisant la commande : ```mvn install```

Cette commande lancera aussi les tests, si vous ne désirez pas les lancer, utilisez plutôt cette variante : ```mvn install -DskipTests```

### Création d'un package

Pour créer un package du projet avec maven-assembly, il faut utiliser la commande ```mvn package``` qui créera un .jar dans le dossier ```target``` qui se trouve à la racine du projet.

De nouveau il sera nécessaire d'avoir le `.env` correctement configuré à la racine du projet.

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

Comme dit précédemment afin de pouvoir utiliser les services de AWS (AWS S3 et AWS Rekognition Label) nous avons décidé d'utiliser un fichier d'environnement pour load les credentials d'AWS.

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

Voici les commandes à exécuter sur la machine Debian:

```
cd app/
java -jar awsApp.jar
```