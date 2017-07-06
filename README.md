# Android_File_Receiver
Composant UPnP permettant à un terminal Android de recevoir un fichier

<strong>Descirption:</strong>

Ce composant permet à un utilisateur de recevoir un fichier provenant d'un serveur distant, lui aussi composant UPnP.
La transmission se fera grâce à l'utilisation des sockets JAVA.

On peut imaginer que ce composant soit utilisé par des étudiants afin de suivre un cours à distance, par exemple pour recevoir
les explications du professeur via un flux/fichier audio.

L'application se présente sous la fomre d'un service Android, il n'y a donc pas d'interfaces graphique. L'utilisateur recevra des popups lors de la connexion à un serveur et lors de la réception d'un fichier (l'avertissant du début/fin de transmission).
L'application commencera à recevoir le fichier après avroir reçue un message XML contenant l'adresse IP de l'émetteur ainsi 
que le nom du fichier.

<strong>Lancement de l'application:</strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composant sur wcomp en suivant la méthode décrite sur le wiki oppocampus.

Cette application ne présente pas d'interface graphique, c'est un service en arrière plan. Si le service n'est pas éteint manuellement, il sera automatiquement au bout de deux heures.

<strong>Spécification UPnP: </strong>

Ce composant offre deux services dont voici la description:

  a) FileReceiverService :
  
    1) SetFile(String NewFileValue): action UPnP qui reçoit un message XML contenant l'adresse IP de l'émetteur ainsi que le nom du fichier à recevoir, ces paramètres sont ensuite transmis à l'application.
    
  b) FileReceivedService :
  
    1) SetPathFileReceived(String PathFileReceived) : action UPnP qui envoie un message XML contenant le chemin du fichier
    reçu via un événement UPnP dont le nom est "PathFileReceived".

Voici le schéma correspondant à ce composant:

![alt tag](https://github.com/components-upnp/Android_File_Receiver/blob/master/Android_File_Receiver.png)

<strong>Maintenance: </strong>

Le projet de l'application est un projet gradle.
