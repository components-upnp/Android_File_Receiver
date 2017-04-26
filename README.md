# Android_File_Receiver
Composant UPnP permettant à un terminal Android de recevoir un fichier

<strong>Descirption:</strong>

Ce composant permet à un utilisateur de recevoir un fichier provenant d'un serveur distant, lui aussi composant UPnP.
La transmission se fera grâce à l'utilisation des sockets JAVA.

On peut imaginer que ce composant soit utilisé par des étudiants afin de suivre un cours à distance, par exemple pour recevoir
les explications du professeur via un flux/fichier audio.

L'application se présente sous la fomre d'un service Android, il n'y a donc pas d'interfaces graphique. L'utilisateur recevra des popups lors de la connexion à un serveur et lors de la réception d'un fichier (l'avertissant du début/fin de transmission).

<strong>Lancement de l'application:</strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composant sur wcomp en suivant la méthode décrite sur le wiki oppocampus.

Cette application ne présente pas d'interface graphique, c'est un service en arrière plan. Si le service n'est pas éteint manuellement, il sera automatiquement au bout de deux heures.

<strong>Spécification UPnP: </strong>

Ce composant offre le service FileReceiverCOntroller dont voici la description:

  1) setReceiving(boolean newReceivingValue): permet d'avertir le composant du début/fin de la transmission
  2) setAddresse(String newAddresseValue): permet de transmettre l'addresse du serveur distant au composant.
  
Ce service n'envoie aucun événement UPnP.

Voici le schéma correspondant à ce composant:

![alt tag](https://github.com/components-upnp/Android_File_Receiver/blob/master/File_Receiver.png)

<strong>Maintenance: </strong>

Le projet de l'application est un projet gradle.
