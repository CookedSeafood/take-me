# TakeMe

TakeMe is a lightweight player riding mod runs server-side. It is quite technically vanilla and it achives its main function by:

1. Use force riding. (This normally unforced)
2. Send vihicle event network packets to the ridden player when start and stop riding. (This normally would only be sent to passengers)

## Usage

Its functions can only be triggered with both hands empty.

### Ride Player

Right click on the player who you would like to ride. If:

1. You are with in 1 block distance with the player's eye.
2. The player has no passenger.

### Mount Player

Right click on the player who you would like to mount. If:

1. You have no passenger.
2. You are with in 1 block distance with the player.

### Dismount Mounted Player

Right click on your mounted player who you would like to dismount. If:

1. Your pitch < 0.

### Transfer the First Mounted Player

Right click on the player who you would like to transfer to. If:

1. Your first mounted player is with in 1 block distance with the player's eye.
2. The player has no passenger.

## FAQ

### I can't click on any player except the one on my head / I can't transfer players

It's not the thing a server-side mod can deal with(except moving the riding players around from this perfect aesthetic riding position). Client-side mods (eg. [Shoulder Surfing Reloaded](https://modrinth.com/mod/shoulder-surfing-reloaded), [Freecam](https://modrinth.com/mod/freecam)) can be a workaround.
