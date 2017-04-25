# Android_File_Receiver
Composant UPnP permettant à un terminal Android de recevoir un fichier

<strong>Descirption:</strong>

Ce composant permet à un utilisateur de recevoir un fichier provenant d'un serveur distant, lui aussi composant UPnP.
La transmission se fera grâce à HTTP.

On peut imaginer qu ce composant soit utilisé par des étudiants afin de suivre un cours à distance, par exemple pour recevoir
les explications du professeur via un flux/fichier audio.

<strong>Lancement de l'application:</strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composantsur wcomp en suivant la méthode décrite sur le wiki oppocampus.

Cette application ne présente pas d'interface graphique, c'est un service en arrière plan. TODO: éteindre le service après une certaine période.

