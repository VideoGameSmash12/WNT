# WNT
WNT is a client mod containing multiple utilities for the technically-inclined and administrative-inclined. It is the successor to W95, a mod I wrote ages ago with the same goal in mind.

## Features
Unlike W95, WNT is split into multiple different submodules containing various features.

### Supervisor
Supervisor is the main component of WNT. It works even if the game is totally frozen. It allows you to do the following:
* Disable certain things from rendering
* Disable the processing of certain packets
* Disconnect from whatever server you are connected to
* Forcibly exit your game

Currently, there is no way to interface with it outside of the Blackbox.

### Blackbox
Blackbox is a separate window heavily inspired by the Windows Task Manager that provides a graphical user interface for the Supervisor. It provides details like what usually shows in the F3 screen, a list of players on the server you are connected to (along with their UUID and ping), entities that are currently in memory, and map data that is currently in memory. It also allows you to send chat messages and run commands.

For reasons unknown, this feature only works on Windows and Linux.

### CFX
CFX is an anti-exploit module that actively patches multiple client-side exploits. It even patches some exploits that have yet to be fixed in the very latest version of the game. Here's a list of some of the exploits it patches or mitigates:
* Lag caused by rendering too many hearts
* Lag caused by entities with names that are too long
* Clickable text that somehow causes commands to be run or chat messages to be sent

### Dumper
Dumper allows you to dump loaded map data and nearby entity data to disk. To use it, the command usage is `/dump <entity [id] | entities | maps | map [id]>` 

### Poker
Poker hooks into certain client mods in a very limited fashion to add options to the Blackbox's "Hooks" menu.

### Toolbox
Toolbox is a pack of miscellaneous administrative tools and commands that don't have a place anywhere else in WNT.

Commands included in the Toolbox are:
* **/dqs**: Interfaces with the DataQueryStorage module
* **/ltellraw**: Client-side /tellraw command
* **/name**: Retrieves the username associated with a given UUID
* **/qe**: F3 + I keybind, but in a command for querying entities
* **/uuid**: Retrieves the UUID associated with a given username
* **/wntmm**: Command for managing modules in the mod

Modules included in the Toolbox are:
* **DataQueryStorage**: A rudimentary way of storing entity and block data obtained by using the F3 + I keybind.
* **LockupProtection**: A module that blocks a specific exploit one server uses where it spams the "open inventory packet".<br>***NOTE: May interfere with plugins that open a specific size of inventory menu. Disable it with `/wntmm toggle LockupProtection` if you experience issues with inventory menus.***
