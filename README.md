# TakeMe

TakeMe is a no-compromises lightweight player riding mod runs server-side.

## Why Another Player Riding Mod

Takeme does **not** move the riding position for player vehicles. For the following reasons:

1. Aesthetic and physical looking
2. Less ceilling hight requirement when indoors

Takeme is quite technically vanilla and it achives its main function by:

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
