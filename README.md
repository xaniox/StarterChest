###########################
## StarterChest - README ##
###########################

Info:
  - This plugin allows you to set multiple starterchests for users. After defining a starterchest you can put starting items into the chests that
    will be given to new users if they open the chest. Every user has his own inventory in this chest.

Features:
  - Every item accepted in the starterchest! Including enchanted items and books (Rule book for example).
  - Compatible with every bukkit superperms permissions plugin
  - Yaml and MySQL database supported!
  - Protection for starterchests (Including explosions).
  - Effects when opening a chest.
  
Commands:

  - Basic command: '/sc', '/starterchest' or '/schest'
  - <> = Must be given
  - [] = Can be given
  
  - /sc set <ID> --> Creates a new starterchest on a chest you are looking.
  - /sc remove [ID] --> Removes a starterchest with the id. If there is no given id you are removing the starterchest you are looking.
  - /sc list --> List all starterchests.
  - /sc info --> Displays information about the starterchest you are looking.
  - /sc help --> Displays the help.
  
Permissions:

  - starterchest.set --> Allows to define a new starterchest
  - starterchest.remove --> Allows to remove a starterchest
  - starterchest.list --> Allows to list all starterchests
  - starterchest.info --> Allows to display information about a starterchest
  - starterchest.help --> Allows to display the help
  
  - starterchest.admin --> Allows to use starterchests as admin
  - starterchest.use --> Allows to use starterchests for normal users
  
Changelog:
  - 1.0
    - Intial release :D
