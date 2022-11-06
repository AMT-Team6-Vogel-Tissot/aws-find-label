# AMT-team-06 - AWS-find-label

Auteurs : Maëlle Vogel, Olivier Tissot-Daguette

## Collaborateurs

Enseignant : Nicolas Glassey

Assistant : Adrien Allemand 

## Utilisation du projet

### Utilisation de Maven

Pour ce projet, l'utilisation de Maven est nécessaire, il faut donc l'installer. Lien vers la documentation officielle pour installer Maven : https://maven.apache.org/install.html

Après l'installation de Maven, il faut aussi installer les différentes dépendances nécessaires à l'aide de la commande : ```mvn clean install```

Pour créer un package du projet, il faut utiliser la commande ```mvn package``` qui créera un .jar dans le dossier ```target``` qui se trouve à la racine du projet.

### AWS

Afin de pouvoir utiliser les services de AWS (AWS S3 et AWS Rekognition Label) nous avons décidé d'utiliser les profiles pour gérer la connexion. Il est donc nécessaire de posséder un profile nommé ```amt06``` sur la machine où le programme fonctionnera.

Il est possible d'utiliser AWS CLI afin de faire la configuration des profiles (disponible ici https://aws.amazon.com/fr/cli/). En ce qui concerne la configuration nous avons utilisé comme région ```eu-west-2``` et le format ```json``` pour l'envoi des données.

Des liens pour utiliser et configurer plusieurs profiles sur un même ordinateur sont disponibles dans la bibliographie.

### Tests

Nous utilisons JUnit pour tester notre programme, il est possible de lancer les tests automatiquement à l'aide de Maven en utilisant la commande ```mvn test```. 

Nous avons mis en pratique une approche BDD (given, when, then) pour l'écriture des tests.

### Annexes

Une wiki est disponible dans le repository avec ces différentes rubriques :
- stratégie de branche
- convention de nommage
- choix des technologies
- bibliographie