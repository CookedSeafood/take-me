# TakeMe

TakeMe is a no-compromises lightweight player riding mod runs server-side.

## Why Another Player Riding Mod

Takeme does **not** move the riding position for player vehicles. For the following reasons:

1. Aesthetic and physical looking.
2. Less ceilling hight requirement when indoors.
3. Align to vanilla.

Takeme is quite technically vanilla and it achives its main function by:

1. Use force riding. (This normally unforced)
2. Send vihicle event network packets to the ridden player when start and stop riding. (This normally would only be sent to passengers)

## Usage

Its functions can only be triggered with both hands empty.

### Ride Player

Right click on the player who you would like to ride. If:

1. The player has no passenger.

### Mount Player

Right click on the player who you would like to mount. If:

1. You are sneaking.
2. You have no passenger.

### Dismount Mounted Player

Right click on your mounted player who you would like to dismount. If:

1. You are sneaking.
2. Your pitch == 90.

### Transfer the First Mounted Player

Right click on the player who you would like to transfer to. If:

1. You are sneaking.
2. The player has no passenger.

## FAQ

### I can't click on any player except the one on my head / I can't transfer players

Use client-side mods (eg. [Shoulder Surfing Reloaded](https://modrinth.com/mod/shoulder-surfing-reloaded)) to workaround this.
